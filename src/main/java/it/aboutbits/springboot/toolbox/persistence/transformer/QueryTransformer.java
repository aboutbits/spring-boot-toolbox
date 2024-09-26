package it.aboutbits.springboot.toolbox.persistence.transformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"rawtypes"})
public final class QueryTransformer<T> {

    private final EntityManager entityManager;
    private final TupleTransformer<T> tupleTransformer;
    private org.hibernate.query.Query unwrappedQuery;
    private boolean isNative = false;

    private QueryTransformer(final EntityManager entityManager, final Class<T> outputClass) {
        this.entityManager = entityManager;
        this.tupleTransformer = new TupleTransformer<>(outputClass);
    }

    public static <T> QueryTransformer<T> of(final EntityManager entityManager, final Class<T> outputClass) {
        return new QueryTransformer<>(entityManager, outputClass);
    }

    public QueryTransformer<T> withQuery(final Query query) {
        if (query instanceof NativeQuery<?>) {
            this.isNative = true;
        }
        this.unwrappedQuery = query.unwrap(org.hibernate.query.Query.class);
        return this;
    }

    public Page<T> asPage(Pageable pageable) {
        return asPage(pageable.getPageNumber(), pageable.getPageSize());
    }

    public Page<T> asPage(final int pageNumber, final int pageSize) {
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
        return Optional.of(result.get(0));
    }

    public T asSingleResultOrFail() {
        return asSingleResult()
                .orElseThrow(EntityNotFoundException::new);
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    private List<T> asList(final Integer pageNumber, final Integer pageSize) {
        unwrappedQuery.setResultTransformer(
                (ResultTransformer) (objects, aliases) -> tupleTransformer.transform(objects)
        );

        if (pageSize != null && pageNumber != null) {
            unwrappedQuery
                    .setMaxResults(pageSize)
                    .setFirstResult(pageSize * pageNumber);
        }

        return unwrappedQuery.getResultList();
    }

    private Page<T> asPageQuery(final int pageNumber, final int pageSize) {
        var selectPattern = "(?i)select.*?[ \\t]*from ";
        var queryString = unwrappedQuery.getQueryString().trim().replaceAll("\\R", " ");
        var countQueryString = queryString.replaceFirst(selectPattern, "select count(*) from ");
        countQueryString = countQueryString.replaceAll("(?i)\\s+order\\s+by\\s+.*$", "");

        if (queryString.toLowerCase().contains("select distinct")) {
            throw new IllegalStateException("Pagination is not possible, if SELECT DISTINCT is present");
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

    private Page<T> asPageNativeQuery(final int pageNumber, final int pageSize) {
        var queryString = unwrappedQuery.getQueryString().trim().replaceAll("\\R", " ");
        var countQueryString = "select count(*) from (" + queryString + ") as count";
        var parameters = unwrappedQuery.getParameters();
        var countQuery = isNative ? entityManager.createNativeQuery(countQueryString, Long.class) : entityManager.createQuery(countQueryString, Long.class);
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
    private static long getCount(final TypedQuery<Long> countQuery, final String queryString) {
        var countQueryResults = countQuery.getResultList();
        if (countQueryResults == null || countQueryResults.isEmpty()) {
            return 0L;
        }

        // Grouping query: count the groups and do not sum the count within them
        if (queryString.toLowerCase().contains("group by")) {
            return countQueryResults.size();
        }

        // Non-grouping query: return the first element, which is the result of count(*)
        return countQueryResults.get(0);
    }

    private static long getCount(final Query countQuery) {
        var countQueryResults = countQuery.getResultList();
        if (countQueryResults == null || countQueryResults.isEmpty()) {
            return 0L;
        }
        // Non-grouping query: return the first element, which is the result of count(*)
        return (long) countQueryResults.get(0);
    }
}
