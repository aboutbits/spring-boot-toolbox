package it.aboutbits.springboot.toolbox.persistence.transformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
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

        var selectPattern = "(?i)select.*?[ \\t]*from ";
        var queryString = unwrappedQuery.getQueryString().trim().replaceAll("\\R", " ");
        var countQueryString = queryString.replaceFirst(selectPattern, "select count(*) from ");
        countQueryString = countQueryString.replaceAll("(?i)\\s+order\\s+by\\s+.*$", "");

        if (queryString.toLowerCase().contains("select distinct")) {
            throw new IllegalStateException(
                    "Pagination is not possible, if SELECT DISTINCT is present. Remove DISTINCT and use GROUP BY instead!");
        }

        if (countQueryString.equals(queryString)) {
            throw new IllegalStateException("Unable to find SELECT ... FROM in query string!");
        }

        var parameters = unwrappedQuery.getParameters();
        var countQuery = entityManager.createQuery(countQueryString, Long.class);
        for (var parameter : parameters) {
            var value = unwrappedQuery.getParameterValue(parameter.getName());
            countQuery.setParameter(parameter.getName(), value);
        }

        var count = getCount(countQuery, queryString);

        var content = asList(pageNumber, pageSize);

        return new PageImpl<>(content, Pageable.ofSize(pageSize).withPage(pageNumber), count);
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

    /**
     * A "group by" clause generates a count for each group, counting the members of that group.
     * So, if we find a "group by" inside the query string we just count the groups and do not sum the count within
     * them.
     */
    private static long getCount(TypedQuery<Long> countQuery, String queryString) {
        var countQueryResults = countQuery.getResultList();
        if (countQueryResults == null || countQueryResults.isEmpty()) {
            return 0L;
        }

        // Grouping query: count the groups and do not sum the count within them
        if (queryString.toLowerCase().contains("group by")) {
            return countQueryResults.size();
        }

        // Non-grouping query: return the first element, which is the result of count(*)
        return countQueryResults.getFirst();
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
