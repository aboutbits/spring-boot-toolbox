package it.aboutbits.springboot.toolbox.boot.persistence;

import it.aboutbits.springboot.toolbox.boot.persistence.impl.jpa.CustomTypeTestModel;
import it.aboutbits.springboot.toolbox.boot.persistence.impl.jpa.CustomTypeTestModelRepository;
import it.aboutbits.springboot.toolbox.support.ApplicationTest;
import it.aboutbits.springboot.toolbox.type.EmailAddress;
import it.aboutbits.springboot.toolbox.type.Iban;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
public class CustomTypeJpaTest {
    @Autowired
    CustomTypeTestModelRepository repository;

    @Nested
    class EmailAddressType {
        @Test
        void inAndOut_shouldSucceed() {
            var item = new CustomTypeTestModel();
            item.setEmail(new EmailAddress("sepp@aboutbits.it"));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByEmail(savedItem.getEmail());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class IbanType {
        @Test
        void inAndOut_shouldSucceed() {
            var item = new CustomTypeTestModel();
            item.setIban(new Iban("NL63ABNA7864733042"));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByIban(savedItem.getIban());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class ScaledBigDecimalType {
        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void inAndOut_shouldSucceed(double doubleValue) {
            var item = new CustomTypeTestModel();
            item.setAccountBalance(new ScaledBigDecimal(doubleValue));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByAccountBalance(savedItem.getAccountBalance());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }
}
