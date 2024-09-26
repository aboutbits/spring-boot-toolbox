package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa.QueryTransformerTestModel;
import it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa.QueryTransformerTestModelRepository;
import it.aboutbits.springboot.toolbox.support.ApplicationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ApplicationTest
public class QueryTransformerTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    QueryTransformerTestModelRepository repository;

    @Test
    void givenQueryWithNoResults_asSingleResult_shouldPass() {
        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asSingleResult();

        assertThat(result).isNotPresent();
    }

    @Test
    void givenQueryWithOneResult_asSingleResult_shouldPass() {
        createTestModel("abc");

        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asSingleResult();

        assertThat(result).isPresent();
        assertThat(result.get().someText).isEqualTo("xxx");
        assertThat(result.get().testModel.getName()).isEqualTo("abc");
    }

    @Test
    void givenQueryWithOneResult_asSingleResultOrFail_shouldPass() {
        createTestModel("abc");

        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asSingleResultOrFail();

        assertThat(result.someText).isEqualTo("xxx");
    }

    @Test
    void givenQueryWithOneResult_asSingleResultOrFail_shouldFail() {
        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        assertThrows(EntityNotFoundException.class, () -> QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asSingleResultOrFail());
    }

    @Test
    void givenQueryWithMultipleResults_asSingleResult_shouldFail() {
        createTestModel("A");
        createTestModel("B");
        createTestModel("C");

        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        assertThrows(
                IllegalStateException.class,
                () -> QueryTransformer
                        .of(entityManager, UserContainer.class)
                        .withQuery(query)
                        .asSingleResult()
        );
    }

    @Test
    void givenQuery_singleUnboxedEntityResult_asList_shouldPass() {
        var user = createTestModel("abc");

        var query = entityManager.createQuery("select q from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, QueryTransformerTestModel.class)
                .withQuery(query)
                .asList();

        assertThat(result.getFirst().getId()).isEqualTo(user.getId());
        assertThat(result.getFirst().getName()).isEqualTo(user.getName());
    }


    @Test
    void givenQuery_asList_shouldPass() {
        var user = createTestModel("abc");

        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asList();

        assertThat(result.getFirst().testModel().getId()).isEqualTo(user.getId());
        assertThat(result.getFirst().testModel().getName()).isEqualTo(user.getName());
        assertThat(result.getFirst().someText).isEqualTo("xxx");
    }


    @Test
    void givenQuery_asPage_shouldPass() {
        var user = createTestModel("abc");

        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asPage(0, 2);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().testModel().getId()).isEqualTo(user.getId());
        assertThat(result.getContent().getFirst().testModel().getName()).isEqualTo(user.getName());
        assertThat(result.getContent().getFirst().someText()).isEqualTo("xxx");
    }

    @Test
    void givenQueryWithNoResults_asPage_shouldPass() {
        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asPage(0, 2);

        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).hasSize(0);
    }

    @Test
    void givenQueryWithNoResults2_asPage_shouldPass() {
        var query = entityManager.createQuery("select q from QueryTransformerTestModel q");

        var result = QueryTransformer
                .of(entityManager, UserContainer.class)
                .withQuery(query)
                .asPage(0, 2);

        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).hasSize(0);
    }

    @Test
    void givenQueryWithGroupBy_asPage_shouldPass() {
        createTestModel("A");
        createTestModel("B");
        createTestModel("A");
        createTestModel("C");

        var query = entityManager.createQuery(
                "select count(q.email), q.name from QueryTransformerTestModel q group by q.name order by q.name desc"
        );

        var resultPage0 = QueryTransformer
                .of(entityManager, UserCount.class)
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
                .of(entityManager, UserCount.class)
                .withQuery(sameQuery)
                .asPage(1, 2);

        assertThat(resultPage1.getTotalElements()).isEqualTo(3);
        assertThat(resultPage1.getTotalPages()).isEqualTo(2);
        assertThat(resultPage1.getContent()).hasSize(1);
    }


    @Test
    void givenQuery_asList_wrongTargetClass_shouldFail() {
        createTestModel("abc");

        var query = entityManager.createQuery("select q, 'xxx' from QueryTransformerTestModel q");

        assertThrows(
                TransformerRuntimeException.class,
                () -> QueryTransformer
                        .of(entityManager, WrongContainer.class)
                        .withQuery(query)
                        .asList()
        );
    }

    protected record UserCount(
            long count,
            String familyName
    ) {
    }

    protected record UserContainer(
            QueryTransformerTestModel testModel,
            String someText
    ) {
    }


    protected record WrongContainer(
            Long someNumber,
            String someText
    ) {
    }

    private QueryTransformerTestModel createTestModel(String name) {
        var item = new QueryTransformerTestModel();
        item.setName(name);
        item.setEmail(null);
        return repository.save(item);
    }
}
