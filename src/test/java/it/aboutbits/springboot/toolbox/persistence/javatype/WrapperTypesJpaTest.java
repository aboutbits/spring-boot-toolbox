package it.aboutbits.springboot.toolbox.persistence.javatype;

import it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa.WrapperTypesModel;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa.WrapperTypesModelRepository;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDoubleClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDoubleRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloatClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloatRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapIntegerClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapIntegerRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLongClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLongRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimalClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimalRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShortClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShortRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapStringClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapStringRecord;
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
    class RecordTypes {
        @Nested
        class WrapBigDecimalRecordType {
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
            @ValueSource(doubles = {-1, 0, 1, -0.001d, 0.001d, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(double doubleValue) {
                var item = new WrapperTypesModel();
                item.setBigDecimalValue(new WrapBigDecimalRecord(BigDecimal.valueOf(doubleValue)));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByBigDecimalValue(savedItem.getBigDecimalValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapBigIntegerRecordType {
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
                item.setBigIntegerValue(new WrapBigIntegerRecord(BigInteger.valueOf(intValue)));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByBigIntegerValue(savedItem.getBigIntegerValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapDoubleRecordType {
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
            @ValueSource(doubles = {-1, 0, 1, -0.001d, 0.001d, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(double doubleValue) {
                var item = new WrapperTypesModel();
                item.setDoubleValue(new WrapDoubleRecord(doubleValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByDoubleValue(savedItem.getDoubleValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapFloatRecordType {
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
                item.setFloatValue(new WrapFloatRecord(floatValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByFloatValue(savedItem.getFloatValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapIntegerRecordType {
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
                item.setIntegerValue(new WrapIntegerRecord(intValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByIntegerValue(savedItem.getIntegerValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapLongRecordType {
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
                item.setLongValue(new WrapLongRecord(longValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByLongValue(savedItem.getLongValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapScaledBigDecimalRecordType {
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
            @ValueSource(doubles = {-1, 0, 1, -0.001d, 0.001d, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(double doubleValue) {
                var item = new WrapperTypesModel();
                item.setScaledBigDecimalValue(new WrapScaledBigDecimalRecord(new ScaledBigDecimal(doubleValue)));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByScaledBigDecimalValue(savedItem.getScaledBigDecimalValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapShortRecordType {
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
                item.setShortValue(new WrapShortRecord(shortValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByShortValue(savedItem.getShortValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapStringRecordType {
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
                item.setStringValue(new WrapStringRecord(stringValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByStringValue(savedItem.getStringValue());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }
    }

    @Nested
    class ClassTypes {
        @Nested
        class WrapBigDecimalClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setBigDecimalValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByBigDecimalValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(doubles = {-1, 0, 1, -0.001d, 0.001d, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(double doubleValue) {
                var item = new WrapperTypesModel();
                item.setBigDecimalValueClass(new WrapBigDecimalClass(BigDecimal.valueOf(doubleValue)));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByBigDecimalValueClass(savedItem.getBigDecimalValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapBigIntegerClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setBigIntegerValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByBigIntegerValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(ints = {-1, 0, 1, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(int intValue) {
                var item = new WrapperTypesModel();
                item.setBigIntegerValueClass(new WrapBigIntegerClass(BigInteger.valueOf(intValue)));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByBigIntegerValueClass(savedItem.getBigIntegerValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapDoubleClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setDoubleValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByDoubleValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(doubles = {-1, 0, 1, -0.001d, 0.001d, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(double doubleValue) {
                var item = new WrapperTypesModel();
                item.setDoubleValueClass(new WrapDoubleClass(doubleValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByDoubleValueClass(savedItem.getDoubleValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapFloatClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setFloatValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByFloatValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(floats = {-1, 0, 1, -0.001f, 0.001f, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(float floatValue) {
                var item = new WrapperTypesModel();
                item.setFloatValueClass(new WrapFloatClass(floatValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByFloatValueClass(savedItem.getFloatValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapIntegerClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setIntegerValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByIntegerValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(ints = {-1, 0, 1, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(int intValue) {
                var item = new WrapperTypesModel();
                item.setIntegerValueClass(new WrapIntegerClass(intValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByIntegerValueClass(savedItem.getIntegerValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapLongClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setLongValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByLongValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(longs = {-1, 0, 1, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(long longValue) {
                var item = new WrapperTypesModel();
                item.setLongValueClass(new WrapLongClass(longValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByLongValueClass(savedItem.getLongValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapScaledBigDecimalClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setScaledBigDecimalValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByScaledBigDecimalValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(doubles = {-1, 0, 1, -0.001d, 0.001d, -100_000_000, 100_000_000})
            void givenValues_inAndOut_shouldSucceed(double doubleValue) {
                var item = new WrapperTypesModel();
                item.setScaledBigDecimalValueClass(new WrapScaledBigDecimalClass(new ScaledBigDecimal(doubleValue)));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByScaledBigDecimalValueClass(savedItem.getScaledBigDecimalValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapShortClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setShortValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByShortValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(shorts = {-1, 0, 1, -10_000, 10_000})
            void givenValues_inAndOut_shouldSucceed(short shortValue) {
                var item = new WrapperTypesModel();
                item.setShortValueClass(new WrapShortClass(shortValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByShortValueClass(savedItem.getShortValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }

        @Nested
        class WrapStringClassType {
            @Test
            void givenNull_inAndOut_shouldSucceed() {
                var item = new WrapperTypesModel();
                item.setStringValueClass(null);

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByStringValueClass(null);

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }

            @ParameterizedTest
            @ValueSource(strings = {"", " ", "test", "some longer test", "\r", "\n"})
            void givenValues_inAndOut_shouldSucceed(String stringValue) {
                var item = new WrapperTypesModel();
                item.setStringValueClass(new WrapStringClass(stringValue));

                var savedItem = repository.save(item);

                var retrievedItem = repository.findByStringValueClass(savedItem.getStringValueClass());

                assertThat(retrievedItem).isPresent()
                        .get()
                        .usingRecursiveComparison()
                        .isEqualTo(savedItem);
            }
        }
    }
}
