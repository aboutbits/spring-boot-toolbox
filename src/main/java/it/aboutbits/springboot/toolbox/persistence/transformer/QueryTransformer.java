package it.aboutbits.springboot.toolbox.persistence.transformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.spi.QueryImplementor;
import org.hibernate.query.spi.QueryParameterBinding;
import org.hibernate.query.spi.SqmQuery;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@NullMarked
public final class QueryTransformer<T> {
    private final EntityManager entityManager;
    private final TupleTransformer<T> tupleTransformer;
    private @Nullable Query<?> unwrappedQuery = null;
    private boolean isNative = false;

    private QueryTransformer(EntityManager entityManager, Class<T> outputClass) {
        this.entityManager = entityManager;
        this.tupleTransformer = new TupleTransformer<>(outputClass);
    }

    /// Pass the `EntityManager` that created the query, because [#asPage(int, int)] runs the count query on it.
    public static <T> QueryTransformer<T> of(EntityManager entityManager, Class<T> outputClass) {
        return new QueryTransformer<>(entityManager, outputClass);
    }

    public QueryTransformer<T> withQuery(jakarta.persistence.Query query) {
        if (query instanceof NativeQuery<?>) {
            this.isNative = true;
        }
        this.unwrappedQuery = query.unwrap(org.hibernate.query.Query.class);
        return this;
    }

    public Page<T> asPage(Pageable pageable) {
        return asPage(pageable.getPageNumber(), pageable.getPageSize());
    }

    /// Runs the count query on the `EntityManager` given to [#of(EntityManager, Class)], so it must be the `EntityManager` that created the query.
    public Page<T> asPage(int pageNumber, int pageSize) {
        return isNative ? asPageNativeQuery(pageNumber, pageSize) : asPageQuery(pageNumber, pageSize);
    }

    public List<T> asList() {
        return asList(null, null);
    }

    public Optional<T> asSingleResult() {
        var result = asList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        if (result.size() > 1) {
            throw new IllegalStateException("Single result query returned multiple results!");
        }
        return Optional.of(result.getFirst());
    }

    public T asSingleResultOrFail() {
        return asSingleResult()
                .orElseThrow(EntityNotFoundException::new);
    }

    private List<T> asList(@Nullable Integer pageNumber, @Nullable Integer pageSize) {
        if (unwrappedQuery == null) {
            throw new IllegalStateException("Query not set!");
        }

        unwrappedQuery.setTupleTransformer(
                tupleTransformer
        );

        if (pageSize != null && pageNumber != null) {
            unwrappedQuery
                    .setMaxResults(pageSize)
                    .setFirstResult(pageSize * pageNumber);
        }

        // noinspection unchecked
        return (List<T>) unwrappedQuery.getResultList();
    }

    private Page<T> asPageQuery(int pageNumber, int pageSize) {
        if (unwrappedQuery == null) {
            throw new IllegalStateException("Query not set!");
        }

        var count = count(unwrappedQuery);

        var content = asList(pageNumber, pageSize);

        return new PageImpl<>(content, Pageable.ofSize(pageSize).withPage(pageNumber), count);
    }

    // Outside a transaction, the shared EntityManager of Spring closes the EntityManager of a query after its first terminating call. Since Spring Framework 7.0.11, getResultCount() is such a call, so the count runs on a new query and the original query stays open for the page content. The count does what getResultCount() of Hibernate does: it derives the count query from the parsed query (SQM), for HQL and criteria queries alike. So subqueries, aliases and the order by do not matter, and it counts the groups of a "group by" and the rows of a "select distinct". The count query shares the parameters of the parsed query, and each binding is copied with its type. Hints, the lock mode, the flush mode and the entity graph are not copied, because getResultCount() ignores them too. SqmQuery, QueryImplementor and QueryParameterBinding are incubating SPI of Hibernate, not internal classes.
    private long count(Query<?> query) {
        var statement = query.unwrap(SqmQuery.class).getSqmStatement();
        if (!(statement instanceof JpaCriteriaQuery<?> selectStatement)) {
            throw new IllegalStateException("A page query must be a select query!");
        }

        var countQuery = entityManager.createQuery(selectStatement.createCountQuery()).unwrap(QueryImplementor.class);
        var countParameters = countQuery.getParameterMetadata();
        var countBindings = countQuery.getParameterBindings();
        query.unwrap(QueryImplementor.class).getParameterBindings().visitBindings((parameter, binding) -> {
            if (binding.isBound() && countParameters.containsReference(parameter)) {
                copyBinding(binding, countBindings.getBinding(parameter));
            }
        });

        return (long) countQuery.getSingleResult();
    }

    @SuppressWarnings("deprecation")
    private static <P> void copyBinding(QueryParameterBinding<P> source, QueryParameterBinding<?> target) {
        var bindType = source.getBindType();
        var temporalPrecision = source.getExplicitTemporalPrecision();
        if (source.isMultiValued()) {
            if (temporalPrecision != null) {
                target.setBindValues(source.getBindValues(), temporalPrecision);
            } else if (bindType != null) {
                target.setBindValues(source.getBindValues(), bindType);
            } else {
                target.setBindValues(source.getBindValues());
            }
        } else if (temporalPrecision != null) {
            target.setBindValue(source.getBindValue(), temporalPrecision);
        } else {
            target.setBindValue(source.getBindValue(), bindType);
        }
    }

    private Page<T> asPageNativeQuery(int pageNumber, int pageSize) {
        if (unwrappedQuery == null) {
            throw new IllegalStateException("Query not set!");
        }

        var queryString = unwrappedQuery.getQueryString().trim().replaceAll("\\R", " ");
        var countQueryString = "select count(*) from (" + queryString + ") as count";
        var parameters = unwrappedQuery.getParameters();
        var countQuery = isNative
                ? entityManager.createNativeQuery(countQueryString, Long.class)
                : entityManager.createQuery(countQueryString, Long.class);
        for (var parameter : parameters) {
            var value = unwrappedQuery.getParameterValue(parameter.getPosition());
            countQuery.setParameter(parameter.getPosition(), value);
        }

        var count = getCount(countQuery);
        var content = asList(pageNumber, pageSize);
        return new PageImpl<>(content, Pageable.ofSize(pageSize).withPage(pageNumber), count);
    }

    private static long getCount(jakarta.persistence.Query countQuery) {
        var countQueryResults = countQuery.getResultList();
        if (countQueryResults == null || countQueryResults.isEmpty()) {
            return 0L;
        }
        // Non-grouping query: return the first element, which is the result of count(*)
        return (long) countQueryResults.getFirst();
    }
}
