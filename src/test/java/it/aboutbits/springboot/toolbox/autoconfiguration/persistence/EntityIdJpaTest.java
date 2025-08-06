package it.aboutbits.springboot.toolbox.autoconfiguration.persistence;

import it.aboutbits.springboot.toolbox._support.ApplicationTest;
import it.aboutbits.springboot.toolbox._support.WithPersistence;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModel;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModelRepository;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.ReferencedTestModel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
@WithPersistence
public class EntityIdJpaTest {
    @Autowired
    CustomTypeTestModelRepository repository;

    @Nested
    class OwnId {
        @Test
        void inAndOut_shouldSucceed() {
            var item = new CustomTypeTestModel();

            var savedItem = repository.save(item);

            var retrievedItem = repository.findById(savedItem.getId());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class ReferencedId {
        @Test
        void inAndOut_shouldSucceed() {
            var item = new CustomTypeTestModel();
            item.setReferencedId(new ReferencedTestModel.ID(1234L));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByReferencedId(savedItem.getReferencedId());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }
}
