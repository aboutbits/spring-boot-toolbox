package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.archunit.toolbox.support.ArchIgnoreGroupName;
import it.aboutbits.springboot.toolbox.type.EmailAddress;
import it.aboutbits.springboot.toolbox.type.Iban;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import lombok.Data;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class TupleTransformerTest {
    @Nested
    @ArchIgnoreGroupName
    class SimpleSimpleValue {
        @Test
        void actualValue_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(String.class);

            // when
            var result = tupleTransformer.transformTuple(new Object[]{"Sepp"}, new String[]{});

            // then
            assertThat(result).isEqualTo("Sepp");
        }

        @Test
        void null_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(String.class);

            // when
            var result = tupleTransformer.transformTuple(new @Nullable Object[]{null}, new String[]{});

            // then
            assertThat(result).isNull();
        }
    }

    @SuppressWarnings("NullAway")
    @Nested
    @ArchIgnoreGroupName
    class WithPrimitives {
        @Test
        void createObjectWithPrimitiveFields_givenPrimitives_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataClassWithPrimitives.class);

            // when
            var result = tupleTransformer.transformTuple(new Object[]{7L, 12, true}, new String[]{});

            // then
            assertThat(result).isNotNull();
            assertThat(result.longField).isEqualTo(7L);
            assertThat(result.intField).isEqualTo(12);
            assertThat(result.booleanField).isTrue();
        }

        @Test
        void createObjectWithPrimitiveFields_givenBoxedObjects_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataClassWithPrimitives.class);
            var l = Long.valueOf(7);
            var i = Integer.valueOf(12);
            var b = Boolean.valueOf(true);
            var objectsUnderTest = new Object[]{l, i, b};

            // when
            var result = tupleTransformer.transformTuple(objectsUnderTest, new String[]{});

            // then
            assertThat(result).isNotNull();
            assertThat(result.longField).isEqualTo(7L);
            assertThat(result.intField).isEqualTo(12);
            assertThat(result.booleanField).isTrue();
        }
    }

    @SuppressWarnings("NullAway")
    @Nested
    @ArchIgnoreGroupName
    class WithCustomTypes {
        @Test
        void createObjectWithCustomTypedFields_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataRecordWithCustomType.class);

            // when
            var result = tupleTransformer.transformTuple(
                    new Object[]{
                            3.14d,
                            "IT27S0300203280975461985512",
                            "info@aboutbits.it"
                    },
                    new String[]{}
            );

            // then
            assertThat(result).isNotNull();
            assertThat(result.scaledBigDecimal).isEqualByComparingTo(ScaledBigDecimal.valueOf(3.14));
            assertThat(result.emailAddress).isEqualTo(new EmailAddress("info@aboutbits.it"));
            assertThat(result.iban).isEqualTo(new Iban("IT27S0300203280975461985512"));
        }
    }

    @SuppressWarnings("NullAway")
    @Nested
    @ArchIgnoreGroupName
    class Records {
        @Test
        void createRecord_givenMixedObjects_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataRecord.class);
            var l = Long.valueOf(7);
            var b = Boolean.valueOf(true);
            var objectsUnderTest = new Object[]{
                    l, b, "String123", false, SomeEnum.ENUM_1, UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88")
            };

            // when
            var result = tupleTransformer.transformTuple(objectsUnderTest, new String[]{});

            // then
            assertThat(result).isNotNull();
            assertThat(result.longField).isEqualTo(7L);
            assertThat(result.booleanBoxed).isTrue();
            assertThat(result.string).isEqualTo("String123");
            assertThat(result.booleanPrimitive).isFalse();
            assertThat(result.anEnum).isEqualTo(SomeEnum.ENUM_1);
            assertThat(result.uuid).isEqualTo(UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88"));
        }

        @Test
        void createRecord_givenMixedObjects_enumValueAsString_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataRecord.class);
            var l = Long.valueOf(7);
            var b = Boolean.valueOf(true);
            var objectsUnderTest = new Object[]{
                    l, b, "String123", false, "ENUM_1", UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88")
            };

            // when
            var result = tupleTransformer.transformTuple(objectsUnderTest, new String[]{});

            // then
            assertThat(result).isNotNull();
            assertThat(result.longField).isEqualTo(7L);
            assertThat(result.booleanBoxed).isTrue();
            assertThat(result.string).isEqualTo("String123");
            assertThat(result.booleanPrimitive).isFalse();
            assertThat(result.anEnum).isEqualTo(SomeEnum.ENUM_1);
            assertThat(result.uuid).isEqualTo(UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88"));
        }

        @Test
        void createRecordInsideAClass_givenMixedObjects_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataRecordParent.class);
            var rec = new DataRecord(
                    7L,
                    true,
                    "String123",
                    false,
                    SomeEnum.ENUM_1,
                    UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88")
            );
            var objectsUnderTest = new Object[]{rec, 33};

            // when
            var result = tupleTransformer.transformTuple(objectsUnderTest, new String[]{});

            // then
            assertThat(result).isNotNull();
            assertThat(result.dataRecord.longField).isEqualTo(7L);
            assertThat(result.dataRecord.booleanBoxed).isTrue();
            assertThat(result.dataRecord.string).isEqualTo("String123");
            assertThat(result.dataRecord.booleanPrimitive).isFalse();
            assertThat(result.dataRecord.anEnum).isEqualTo(SomeEnum.ENUM_1);
            assertThat(result.dataRecord.uuid).isEqualTo(UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88"));
            assertThat(result.someOtherField).isEqualTo(33);
        }

        @Test
        void createRecord_givenMixedObjects_someNullValues_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataRecord.class);
            var objectsUnderTest = new @Nullable Object[]{
                    null, null, "String123", false, null, UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88")
            };

            // when
            var result = tupleTransformer.transformTuple(objectsUnderTest, new String[]{});

            // then
            assertThat(result).isNotNull();
            assertThat(result.longField).isNull();
            assertThat(result.booleanBoxed).isNull();
            assertThat(result.string).isEqualTo("String123");
            assertThat(result.booleanPrimitive).isFalse();
            assertThat(result.anEnum).isNull();
            assertThat(result.uuid).isEqualTo(UUID.fromString("f255a993-0086-4b4d-a62f-3aa174501f88"));
        }

        @Test
        void createRecordInsideAClass_givenMixedObjectsAsList_shouldPass() {
            // given
            var tupleTransformer = new TupleTransformer<>(DataRecordParentWithList.class);
            var rec1 = new DataRecord(
                    7L,
                    true,
                    "String111",
                    false,
                    SomeEnum.ENUM_1,
                    UUID.fromString("c3221989-b494-4227-b7e1-2f84fdc048f7")
            );
            var rec2 = new DataRecord(
                    0L,
                    null,
                    "String222",
                    false,
                    SomeEnum.ENUM_2,
                    UUID.fromString("25f6e0c2-4628-43a1-a703-e80926496fd1")
            );
            var list = new ArrayList<>();
            list.add(rec1);
            list.add(rec2);
            var objectsUnderTest = new Object[]{list, 33};

            // when
            var result = tupleTransformer.transformTuple(objectsUnderTest, new String[]{});

            // then
            assertThat(result).isNotNull();

            var firstRecord = result.dataRecords.getFirst();
            assertThat(firstRecord.longField).isEqualTo(7L);
            assertThat(firstRecord.booleanBoxed).isTrue();
            assertThat(firstRecord.string).isEqualTo("String111");
            assertThat(firstRecord.booleanPrimitive).isFalse();
            assertThat(firstRecord.anEnum).isEqualTo(SomeEnum.ENUM_1);
            assertThat(firstRecord.uuid).isEqualTo(UUID.fromString("c3221989-b494-4227-b7e1-2f84fdc048f7"));

            var secondRecord = result.dataRecords.get(1);
            assertThat(secondRecord.longField).isZero();
            assertThat(secondRecord.booleanBoxed).isNull();
            assertThat(secondRecord.string).isEqualTo("String222");
            assertThat(secondRecord.booleanPrimitive).isFalse();
            assertThat(secondRecord.anEnum).isEqualTo(SomeEnum.ENUM_2);
            assertThat(secondRecord.uuid).isEqualTo(UUID.fromString("25f6e0c2-4628-43a1-a703-e80926496fd1"));

            assertThat(result.someOtherField).isEqualTo(33);
        }
    }

    @Data
    private static final class DataClassWithPrimitives {
        private final long longField;
        private final int intField;
        private final boolean booleanField;
    }

    @NullUnmarked
    protected record DataRecord(
            Long longField,
            Boolean booleanBoxed,
            String string,
            boolean booleanPrimitive,
            SomeEnum anEnum,
            UUID uuid
    ) {
    }

    protected record DataRecordParent(
            DataRecord dataRecord,
            long someOtherField
    ) {
    }

    protected record DataRecordWithCustomType(
            ScaledBigDecimal scaledBigDecimal,
            Iban iban,
            EmailAddress emailAddress
    ) {
    }

    protected record DataRecordParentWithList(
            List<DataRecord> dataRecords,
            long someOtherField
    ) {
    }

    protected enum SomeEnum {
        ENUM_1,
        ENUM_2
    }
}
