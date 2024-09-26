package it.aboutbits.springboot.toolbox.persistence.transformer;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TupleTransformerTest {

    @Test
    void createObjectWithPrimitiveFields_givenPrimitives_shouldPass() {
        // given
        var tupleTransformer = new TupleTransformer<>(DataClassWithPrimitives.class);

        // when
        var result = tupleTransformer.transform(new Object[] {7L, 12, true});

        // then
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
        var objectsUnderTest = new Object[] {l, i, b};

        // when
        var result = tupleTransformer.transform(objectsUnderTest);

        // then
        assertThat(result.longField).isEqualTo(7L);
        assertThat(result.intField).isEqualTo(12);
        assertThat(result.booleanField).isTrue();
    }


    @Test
    void createRecord_givenMixedObjects_shouldPass() {
        // given
        var tupleTransformer = new TupleTransformer<>(DataRecord.class);
        var l = Long.valueOf(7);
        var b = Boolean.valueOf(true);
        var objectsUnderTest = new Object[] {l, b, "String123", false, SomeEnum.ENUM_1};

        // when
        var result = tupleTransformer.transform(objectsUnderTest);

        // then
        assertThat(result.longField).isEqualTo(7L);
        assertThat(result.booleanBoxed).isTrue();
        assertThat(result.string).isEqualTo("String123");
        assertThat(result.booleanPrimitive).isFalse();
        assertThat(result.anEnum).isEqualTo(SomeEnum.ENUM_1);
    }


    @Test
    void createRecord_givenMixedObjects_enumValueAsString_shouldPass() {
        // given
        var tupleTransformer = new TupleTransformer<>(DataRecord.class);
        var l = Long.valueOf(7);
        var b = Boolean.valueOf(true);
        var objectsUnderTest = new Object[] {l, b, "String123", false, "ENUM_1"};

        // when
        var result = tupleTransformer.transform(objectsUnderTest);

        // then
        assertThat(result.longField).isEqualTo(7L);
        assertThat(result.booleanBoxed).isTrue();
        assertThat(result.string).isEqualTo("String123");
        assertThat(result.booleanPrimitive).isFalse();
        assertThat(result.anEnum).isEqualTo(SomeEnum.ENUM_1);
    }


    @Test
    void createRecordInsideAClass_givenMixedObjects_shouldPass() {
        // given
        var tupleTransformer = new TupleTransformer<>(DataRecordParent.class);
        var rec = new DataRecord(7L, true, "String123", false, SomeEnum.ENUM_1);
        var objectsUnderTest = new Object[] {rec, 33};

        // when
        var result = tupleTransformer.transform(objectsUnderTest);

        // then
        assertThat(result.dataRecord.longField).isEqualTo(7L);
        assertThat(result.dataRecord.booleanBoxed).isTrue();
        assertThat(result.dataRecord.string).isEqualTo("String123");
        assertThat(result.dataRecord.booleanPrimitive).isFalse();
        assertThat(result.dataRecord.anEnum).isEqualTo(SomeEnum.ENUM_1);
        assertThat(result.someOtherField).isEqualTo(33);
    }


    @Test
    void createRecord_givenMixedObjects_someNullValues_shouldPass() {
        // given
        var tupleTransformer = new TupleTransformer<>(DataRecord.class);
        var objectsUnderTest = new Object[] {null, null, "String123", false, null};

        // when
        var result = tupleTransformer.transform(objectsUnderTest);

        // then
        assertThat(result.longField).isNull();
        assertThat(result.booleanBoxed).isNull();
        assertThat(result.string).isEqualTo("String123");
        assertThat(result.booleanPrimitive).isFalse();
        assertThat(result.anEnum).isNull();
    }


    @Test
    void createRecordInsideAClass_givenMixedObjectsAsList_shouldPass() {
        // given
        var tupleTransformer = new TupleTransformer<>(DataRecordParentWithList.class);
        var rec1 = new DataRecord(7L, true, "String111", false, SomeEnum.ENUM_1);
        var rec2 = new DataRecord(0L, null, "String222", false, SomeEnum.ENUM_2);
        var list = new ArrayList<>();
        list.add(rec1);
        list.add(rec2);
        var objectsUnderTest = new Object[] {list, 33};

        // when
        var result = tupleTransformer.transform(objectsUnderTest);

        // then
        var firstRecord = result.dataRecords.get(0);
        assertThat(firstRecord.longField).isEqualTo(7L);
        assertThat(firstRecord.booleanBoxed).isTrue();
        assertThat(firstRecord.string).isEqualTo("String111");
        assertThat(firstRecord.booleanPrimitive).isFalse();
        assertThat(firstRecord.anEnum).isEqualTo(SomeEnum.ENUM_1);

        var secondRecord = result.dataRecords.get(1);
        assertThat(secondRecord.longField).isEqualTo(0L);
        assertThat(secondRecord.booleanBoxed).isNull();
        assertThat(secondRecord.string).isEqualTo("String222");
        assertThat(secondRecord.booleanPrimitive).isFalse();
        assertThat(secondRecord.anEnum).isEqualTo(SomeEnum.ENUM_2);

        assertThat(result.someOtherField).isEqualTo(33);
    }


    @Data
    private static final class DataClassWithPrimitives {
        private final long longField;
        private final int intField;
        private final boolean booleanField;
    }

    protected record DataRecord(
            Long longField,
            Boolean booleanBoxed,
            String string,
            boolean booleanPrimitive,
            SomeEnum anEnum
    ) { }


    protected record DataRecordParent(
            DataRecord dataRecord,
            long someOtherField
    ) { }


    protected record DataRecordParentWithList(
            List<DataRecord> dataRecords,
            long someOtherField
    ) { }

    protected enum SomeEnum {
        ENUM_1,
        ENUM_2
    }
}
