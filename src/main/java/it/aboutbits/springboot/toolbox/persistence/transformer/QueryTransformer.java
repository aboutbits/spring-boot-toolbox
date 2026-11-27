package it.aboutbits.springboot.toolbox.persistence.transformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;
import java.util.function.LongSupplier;

@NullMarked
public final class QueryTransformer<T> {
    private static final String CRITERIA_QUERY_STRING = "<criteria>";

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
        var query = unwrappedQuery;
        if (query == null) {
            throw new IllegalStateException("Query not set!");
        }

        // Outside a transaction, the shared EntityManager of Spring closes the EntityManager of a query after its first terminating call, so one query cannot give the content and the count. Spring Data JPA has the same rule for a paged repository query: inside a transaction it counts on the same query, outside it counts on a second query.
        LongSupplier count = TransactionSynchronizationManager.isActualTransactionActive()
                ? query::getResultCount
                : countOnCopy(query);

        var content = asList(pageNumber, pageSize);

        // The count runs only when the content does not show the total already.
        return PageableExecutionUtils.getPage(content, Pageable.ofSize(pageSize).withPage(pageNumber), count);
    }

    // Reads the query string and the bound parameters at once, because the query is closed after the content call. The second query is created only when the count is needed, so no EntityManager stays open. Hibernate derives the count query from the parsed query, so subqueries, aliases and the order by do not matter, and it counts the groups of a "group by" and the rows of a "select distinct". Only the value of a parameter is copied, not an explicit bind type or temporal precision.
    private LongSupplier countOnCopy(Query<?> query) {
        var queryString = query.getQueryString();
        // The Javadoc of getQueryString() names null for a criteria query, Hibernate returns the marker.
        if (queryString == null || CRITERIA_QUERY_STRING.equals(queryString)) {
            throw new IllegalStateException(
                    "A criteria query can be paged in a transaction only. Add a transaction or use an HQL query."
            );
        }

        var bindings = query.getParameters().stream()
                .filter(query::isBound)
                .map(parameter -> new Binding(
                        parameter.getName(),
                        parameter.getPosition(),
                        query.getParameterValue(parameter)
                ))
                .toList();

        return () -> {
            var countQuery = entityManager.createQuery(queryString);
            for (var binding : bindings) {
                if (binding.name() != null) {
                    countQuery.setParameter(binding.name(), binding.value());
                } else if (binding.position() != null) {
                    countQuery.setParameter(binding.position(), binding.value());
                }
            }
            return countQuery.unwrap(Query.class).getResultCount();
        };
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

    private record Binding(
            @Nullable String name,
            @Nullable Integer position,
            @Nullable Object value
    ) {
    }
}
