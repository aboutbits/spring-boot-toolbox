package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox._support.ApplicationTest;
import it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa.QueryTransformerTestModel;
import it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa.QueryTransformerTestModelRepository;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TemporalType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@ApplicationTest
@NullMarked
class QueryTransformerTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    QueryTransformerTestModelRepository repository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Nested
    class AsSingleResult {

        @Test
        void givenQueryWithNoResults_shouldPass() {
            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asSingleResult();

            assertThat(result).isNotPresent();
        }

        @Test
        void givenQueryWithPrimitiveResult_shouldPass() {
            var query = entityManager.createQuery("select 'abc'");

            var result = QueryTransformer
                    .of(entityManager, String.class)
                    .withQuery(query)
                    .asSingleResult();

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo("abc");

            query = entityManager.createQuery("select 4.44");

            var result2 = QueryTransformer
                    .of(entityManager, Double.class)
                    .withQuery(query)
                    .asSingleResult();

            assertThat(result2).isPresent();
            assertThat(result2.get()).isEqualTo(4.44);
        }

        @Test
        void givenQueryWithPrimitiveCustomTypedResult_shouldPass() {
            var query = entityManager.createQuery("select 3.14");

            var result = QueryTransformer
                    .of(entityManager, ScaledBigDecimal.class)
                    .withQuery(query)
                    .asSingleResult();

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualByComparingTo(ScaledBigDecimal.valueOf(3.14));
        }

        @Test
        void givenQueryWithOneResult_shouldPass() {
            createTestModel("abc");

            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asSingleResult();

            assertThat(result).isPresent();
            assertThat(result.get().someText()).isEqualTo("xxx");
            assertThat(result.get().testModel().getName()).isEqualTo("abc");
        }

        @Test
        void givenQueryWithOneResult_customJavaType_shouldPass() {
            var testModel = createTestModel("abc", "info@aboutbits.it", ScaledBigDecimal.valueOf(3.14));

            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asSingleResult();

            assertThat(result).isPresent();
            assertThat(result.get().someText()).isEqualTo("xxx");
            assertThat(result.get().testModel().getName()).isEqualTo(testModel.getName());
            assertThat(result.get().testModel().getEmail()).isEqualTo(testModel.getEmail());
            assertThat(result.get().testModel().getScaledBigDecimalValue()).isEqualByComparingTo(testModel.getScaledBigDecimalValue());
        }

        @Test
        void givenQueryWithNullableParameter_shouldPass() {
            createTestModel("abc", "info@aboutbits.it", ScaledBigDecimal.valueOf(3.14));

            // Testing https://linear.app/aboutbits/issue/AB-217/be-querytransformer-function-lowerbytea-does-not-exist
            var query = entityManager.createQuery(
                    // @formatter:off
                    """
                    select q
                    from QueryTransformerTestModel q
                    where :name is null or q.name ilike '%' || cast(:name as string) || '%'
                    """
                    // @formatter:on
            ).setParameter("name", null);

            var result = QueryTransformer
                    .of(entityManager, QueryTransformerTestModel.class)
                    .withQuery(query)
                    .asSingleResult();

            assertThat(result).isPresent();
        }

        @Test
        void givenQueryWithMultipleResults_shouldFail() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("C");

            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            assertThatIllegalStateException().isThrownBy(() -> QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asSingleResult()
            );
        }
    }

    @Nested
    class AsSingleResultOrFail {

        @Test
        void givenQueryWithOneResult_shouldPass() {
            createTestModel("abc");

            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asSingleResultOrFail();

            assertThat(result.someText()).isEqualTo("xxx");
        }

        @Test
        void givenQueryWithOneResult_shouldFail() {
            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asSingleResultOrFail()
            );
        }
    }

    @Nested
    class AsList {

        @Test
        void givenQuery_singleUnboxedEntityResult_shouldPass() {
            var testModel = createTestModel("abc");

            var query = entityManager.createQuery("select q from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, QueryTransformerTestModel.class)
                    .withQuery(query)
                    .asList();

            assertThat(result.getFirst().getId()).isEqualTo(testModel.getId());
            assertThat(result.getFirst().getName()).isEqualTo(testModel.getName());
        }

        @Test
        void givenQuery_shouldPass() {
            var testModel = createTestModel("abc");

            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asList();

            assertThat(result.getFirst().testModel().getId()).isEqualTo(testModel.getId());
            assertThat(result.getFirst().testModel().getName()).isEqualTo(testModel.getName());
            assertThat(result.getFirst().someText()).isEqualTo("xxx");
        }

        @Test
        void givenQuery_wrongTargetClass_shouldFail() {
            createTestModel("abc");

            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            assertThatExceptionOfType(TransformerRuntimeException.class).isThrownBy(() -> QueryTransformer
                    .of(entityManager, WrongContainer.class)
                    .withQuery(query)
                    .asList()
            );
        }
    }

    // The tests run without a transaction, unless they start one, so the shared EntityManager of Spring closes the EntityManager of a query after its first terminating call.
    @Nested
    class AsPage {

        @Test
        void givenQuery_shouldPass() {
            var testModel = createTestModel("abc");

            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getNumber()).isEqualTo(0);
            assertThat(result.getSize()).isEqualTo(2);
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().getFirst().testModel().getId()).isEqualTo(testModel.getId());
            assertThat(result.getContent().getFirst().testModel().getName()).isEqualTo(testModel.getName());
            assertThat(result.getContent().getFirst().someText()).isEqualTo("xxx");
        }

        @Test
        void givenQueryWithNoResults_shouldPass() {
            var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getContent()).hasSize(0);
        }

        @Test
        void givenQueryWithNoResults2_shouldPass() {
            var query = entityManager.createQuery("select q from QueryTransformerTestModel q");

            var result = QueryTransformer
                    .of(entityManager, TestModelContainer.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getContent()).hasSize(0);
        }

        @Test
        void givenQueryWithGroupBy_shouldPass() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");
            createTestModel("C");

            var query = entityManager.createQuery(
                    "select count(q.email), q.name from QueryTransformerTestModel q group by q.name order by q.name desc"
            );

            var resultPage0 = QueryTransformer
                    .of(entityManager, TestModelCount.class)
                    .withQuery(query)
                    .asPage(0, 2);

            // We have 3 groups: A, B, and C
            assertThat(resultPage0.getTotalElements()).isEqualTo(3);
            assertThat(resultPage0.getTotalPages()).isEqualTo(2);
            assertThat(resultPage0.getContent()).hasSize(2);

            var sameQuery = entityManager.createQuery(
                    "select count(q.email), q.name from QueryTransformerTestModel q group by q.name order by q.name desc"
            );

            var resultPage1 = QueryTransformer
                    .of(entityManager, TestModelCount.class)
                    .withQuery(sameQuery)
                    .asPage(1, 2);

            assertThat(resultPage1.getTotalElements()).isEqualTo(3);
            assertThat(resultPage1.getTotalPages()).isEqualTo(2);
            assertThat(resultPage1.getContent()).hasSize(1);
        }

        @Test
        void givenVariousQueries_shouldPassReturningTheRightTotalCount() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");
            createTestModel("A");

            var query = entityManager.createQuery(
                    "select q.name from QueryTransformerTestModel q group by q.name order by q.name desc"
            );

            var resultPage = QueryTransformer
                    .of(entityManager, TestModelNameOnly.class)
                    .withQuery(query)
                    .asPage(0, 2);

            // We have 2 groups: A, and B
            assertThat(resultPage.getTotalElements()).isEqualTo(2);

            // TEST: No grouping
            query = entityManager.createQuery(
                    "select q.name from QueryTransformerTestModel q"
            );

            resultPage = QueryTransformer
                    .of(entityManager, TestModelNameOnly.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(resultPage.getTotalElements()).isEqualTo(4);
        }

        @Test
        void givenQueryWithSelectDistinct_shouldCountTheDistinctRows() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");
            createTestModel("C");

            var query = entityManager.createQuery(
                    "select distinct q.name from QueryTransformerTestModel q order by q.name"
            );

            var result = QueryTransformer
                    .of(entityManager, TestModelNameOnly.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getContent()).extracting(TestModelNameOnly::name).containsExactly("A", "B");
        }

        @Test
        void givenQueryWithSubqueryInSelectAndOrderByAlias_shouldPass() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");

            var query = entityManager.createQuery(
                    // @formatter:off
                    """
                    select
                        q.name as name,
                        (select count(q2.id) from QueryTransformerTestModel q2 where q2.name = q.name)
                    from QueryTransformerTestModel q
                    order by name asc, q.id asc
                    """
                    // @formatter:on
            );

            var result = QueryTransformer
                    .of(entityManager, TestModelNameWithCount.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getTotalPages()).isEqualTo(2);
            assertThat(result.getContent()).containsExactly(
                    new TestModelNameWithCount("A", 2),
                    new TestModelNameWithCount("A", 2)
            );
        }

        @Test
        void givenQueryWithParameter_shouldCountWithTheParameter() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");

            var query = entityManager.createQuery(
                    "select q.name from QueryTransformerTestModel q where q.name = :name"
            ).setParameter("name", "A");

            var result = QueryTransformer
                    .of(entityManager, TestModelNameOnly.class)
                    .withQuery(query)
                    .asPage(0, 1);

            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getContent()).containsExactly(new TestModelNameOnly("A"));
        }

        @Test
        void givenQueryWithPositionalParameter_shouldCountWithTheParameter() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");
            createTestModel("C");

            var query = entityManager.createQuery(
                    "select q.name from QueryTransformerTestModel q where q.name <> ?1 order by q.name, q.id"
            ).setParameter(1, "B");

            var result = QueryTransformer
                    .of(entityManager, TestModelNameOnly.class)
                    .withQuery(query)
                    .asPage(1, 2);

            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getTotalPages()).isEqualTo(2);
            assertThat(result.getContent()).containsExactly(new TestModelNameOnly("C"));
        }

        @Test
        void givenQueryWithListParameterAndGroupBy_shouldCountTheGroups() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");
            createTestModel("C");
            createTestModel("D");

            var query = entityManager.createQuery(
                    "select count(q.id), q.name from QueryTransformerTestModel q where q.name in :names group by q.name order by q.name"
            ).setParameter("names", List.of("A", "B", "C"));

            var result = QueryTransformer
                    .of(entityManager, TestModelCount.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getContent()).containsExactly(
                    new TestModelCount(2, "A"),
                    new TestModelCount(1, "B")
            );
        }

        @Test
        void givenQueryWithExplicitlyTypedParameter_shouldCountWithTheType() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("C");

            // With TemporalType.DATE, the parameter is the date 2026-01-01, which equals the timestamp at midnight. Without the type, it is the timestamp 2026-01-01 15:30, and no row matches.
            var day = Date.from(LocalDateTime.of(2026, 1, 1, 15, 30).atZone(ZoneId.systemDefault()).toInstant());
            var query = entityManager.createQuery(
                    "select q.name from QueryTransformerTestModel q where :day = {ts '2026-01-01 00:00:00'} order by q.name"
            ).setParameter("day", day, TemporalType.DATE);

            var result = QueryTransformer
                    .of(entityManager, TestModelNameOnly.class)
                    .withQuery(query)
                    .asPage(0, 2);

            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getContent()).containsExactly(
                    new TestModelNameOnly("A"),
                    new TestModelNameOnly("B")
            );
        }

        @Test
        void givenCriteriaQuery_shouldCountWithTheParameters() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");
            createTestModel("C");
            createTestModel("D");

            var criteriaBuilder = entityManager.getCriteriaBuilder();
            var criteriaQuery = criteriaBuilder.createQuery(String.class);
            var root = criteriaQuery.from(QueryTransformerTestModel.class);
            var excludedName = criteriaBuilder.parameter(String.class);
            criteriaQuery
                    .select(root.get("name"))
                    .distinct(true)
                    .where(
                            criteriaBuilder.notEqual(root.get("name"), excludedName),
                            criteriaBuilder.notEqual(root.get("name"), "D")
                    )
                    .orderBy(criteriaBuilder.asc(root.get("name")));
            var query = entityManager.createQuery(criteriaQuery).setParameter(excludedName, "B");

            var result = QueryTransformer
                    .of(entityManager, TestModelNameOnly.class)
                    .withQuery(query)
                    .asPage(1, 1);

            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getContent()).containsExactly(new TestModelNameOnly("C"));
        }

        @Test
        void givenQueryInsideTransaction_shouldPass() {
            createTestModel("A");
            createTestModel("B");
            createTestModel("A");

            var result = transactionTemplate.execute(_ -> {
                var query = entityManager.createQuery(
                        "select distinct q.name from QueryTransformerTestModel q where q.name <> :name order by q.name"
                ).setParameter("name", "C");

                return QueryTransformer
                        .of(entityManager, TestModelNameOnly.class)
                        .withQuery(query)
                        .asPage(0, 1);
            });

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getContent()).containsExactly(new TestModelNameOnly("A"));
        }
    }

    protected record TestModelCount(
            long count,
            String familyName
    ) {
    }

    protected record TestModelNameOnly(
            String name
    ) {
    }

    protected record TestModelNameWithCount(
            String name,
            long count
    ) {
    }

    protected record TestModelContainer(
            QueryTransformerTestModel testModel,
            String someText
    ) {
    }

    protected record WrongContainer(
            Long someNumber,
            String someText
    ) {
    }

    private QueryTransformerTestModel createTestModel(
            String name,
            @Nullable String email,
            ScaledBigDecimal scaledBigDecimal
    ) {
        var item = new QueryTransformerTestModel();
        item.setName(name);
        item.setEmail(email);
        item.setScaledBigDecimalValue(scaledBigDecimal);
        return repository.save(item);
    }

    private QueryTransformerTestModel createTestModel(String name) {
        return createTestModel(name, null, ScaledBigDecimal.ZERO);
    }
}
