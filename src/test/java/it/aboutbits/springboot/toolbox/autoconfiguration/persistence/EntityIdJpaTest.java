package it.aboutbits.springboot.toolbox.autoconfiguration.persistence;

import com.tngtech.archunit.junit.ArchIgnore;
import it.aboutbits.springboot.toolbox._support.ApplicationTest;
import it.aboutbits.springboot.toolbox._support.WithPersistence;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeEnumTestModel;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeEnumTestModelRepository;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModel;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModelRepository;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.ReferencedTestModel;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@ArchIgnore(reason = "This test class has no matching counterpart in the production code.")
@ApplicationTest
@WithPersistence
@NullMarked
class EntityIdJpaTest {
    @Autowired
    CustomTypeTestModelRepository repository;

    @Autowired
    CustomTypeEnumTestModelRepository repositoryEnum;

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

    @Nested
    class EnumId {
        @Test
        void inAndOut_shouldSucceed() {
            var values = CustomTypeEnumTestModel.CustomTypeEnum.values();

            var item = new CustomTypeEnumTestModel();
            item.setId(new CustomTypeEnumTestModel.ID(
                    values[new Random().nextInt(values.length)]
            ));

            var savedItem = repositoryEnum.save(item);

            var retrievedItem = repositoryEnum.findById(savedItem.getId());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }
}
