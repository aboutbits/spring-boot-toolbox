package it.aboutbits.springboot.toolbox.persistence.javatype;

import it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa.WrapperTypesModel;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa.WrapperTypesModelRepository;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimal;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigInteger;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDouble;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloat;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapInteger;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLong;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimal;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShort;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapString;
import it.aboutbits.springboot.toolbox.support.ApplicationTest;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationTest
public class WrapperTypesJpaTest {
    @Autowired
    WrapperTypesModelRepository repository;

    @Nested
    class WrapBigDecimalType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setBigDecimalValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByBigDecimalValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void givenValues_inAndOut_shouldSucceed(double doubleValue) {
            var item = new WrapperTypesModel();
            item.setBigDecimalValue(new WrapBigDecimal(BigDecimal.valueOf(doubleValue)));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByBigDecimalValue(savedItem.getBigDecimalValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapBigIntegerType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setBigIntegerValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByBigIntegerValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0, 1, -100_000_000, 100_000_000})
        void givenValues_inAndOut_shouldSucceed(int intValue) {
            var item = new WrapperTypesModel();
            item.setBigIntegerValue(new WrapBigInteger(BigInteger.valueOf(intValue)));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByBigIntegerValue(savedItem.getBigIntegerValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapDoubleType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setDoubleValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByDoubleValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void givenValues_inAndOut_shouldSucceed(double doubleValue) {
            var item = new WrapperTypesModel();
            item.setDoubleValue(new WrapDouble(doubleValue));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByDoubleValue(savedItem.getDoubleValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapFloatType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setFloatValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByFloatValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(floats = {-1, 0, 1, -0.001f, 0.001f, -100_000_000, 100_000_000})
        void givenValues_inAndOut_shouldSucceed(float floatValue) {
            var item = new WrapperTypesModel();
            item.setFloatValue(new WrapFloat(floatValue));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByFloatValue(savedItem.getFloatValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapIntegerType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setIntegerValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByIntegerValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0, 1, -100_000_000, 100_000_000})
        void givenValues_inAndOut_shouldSucceed(int intValue) {
            var item = new WrapperTypesModel();
            item.setIntegerValue(new WrapInteger(intValue));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByIntegerValue(savedItem.getIntegerValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapLongType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setLongValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByLongValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(longs = {-1, 0, 1, -100_000_000, 100_000_000})
        void givenValues_inAndOut_shouldSucceed(long longValue) {
            var item = new WrapperTypesModel();
            item.setLongValue(new WrapLong(longValue));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByLongValue(savedItem.getLongValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapScaledBigDecimalType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setScaledBigDecimalValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByScaledBigDecimalValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, 0, 1, -0.001, 0.001, -100_000_000, 100_000_000})
        void givenValues_inAndOut_shouldSucceed(double doubleValue) {
            var item = new WrapperTypesModel();
            item.setScaledBigDecimalValue(new WrapScaledBigDecimal(new ScaledBigDecimal(doubleValue)));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByScaledBigDecimalValue(savedItem.getScaledBigDecimalValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapShortType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setShortValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByShortValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(shorts = {-1, 0, 1, -10_000, 10_000})
        void givenValues_inAndOut_shouldSucceed(short shortValue) {
            var item = new WrapperTypesModel();
            item.setShortValue(new WrapShort(shortValue));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByShortValue(savedItem.getShortValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }

    @Nested
    class WrapStringType {
        @Test
        void givenNull_inAndOut_shouldSucceed() {
            var item = new WrapperTypesModel();
            item.setStringValue(null);

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByStringValue(null);

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "test", "some longer test", "\r", "\n"})
        void givenValues_inAndOut_shouldSucceed(String stringValue) {
            var item = new WrapperTypesModel();
            item.setStringValue(new WrapString(stringValue));

            var savedItem = repository.save(item);

            var retrievedItem = repository.findByStringValue(savedItem.getStringValue());

            assertThat(retrievedItem).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(savedItem);
        }
    }
}
