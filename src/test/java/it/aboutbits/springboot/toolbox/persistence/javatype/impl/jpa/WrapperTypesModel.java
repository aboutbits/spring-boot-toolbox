package it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBigDecimalClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBigDecimalRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBigIntegerClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBigIntegerRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBooleanClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBooleanRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapByteClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapByteRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapCharacterClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapCharacterRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapDoubleClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapDoubleRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapFloatClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapFloatRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapIntegerClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapIntegerRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapLongClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapLongRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapScaledBigDecimalClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapScaledBigDecimalRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapShortClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapShortRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapStringClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapStringRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapUUIDClassJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapUUIDRecordJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBooleanClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBooleanRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapByteClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapByteRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapCharacterClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapCharacterRecord;
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
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapUUIDClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapUUIDRecord;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.type.identity.Identified;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

@Entity
@Getter
@Setter
@Table(name = "wrapper_type_test_model")
public class WrapperTypesModel implements Identified<WrapperTypesModel.ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JavaType(ID.JavaType.class)
    private ID id;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapBigDecimalRecordJavaType.class)
    private WrapBigDecimalRecord bigDecimalValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapBigIntegerRecordJavaType.class)
    private WrapBigIntegerRecord bigIntegerValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapDoubleRecordJavaType.class)
    private WrapDoubleRecord doubleValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapFloatRecordJavaType.class)
    private WrapFloatRecord floatValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapIntegerRecordJavaType.class)
    private WrapIntegerRecord integerValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapLongRecordJavaType.class)
    private WrapLongRecord longValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapScaledBigDecimalRecordJavaType.class)
    private WrapScaledBigDecimalRecord scaledBigDecimalValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapShortRecordJavaType.class)
    private WrapShortRecord shortValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapByteRecordJavaType.class)
    private WrapByteRecord byteValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapStringRecordJavaType.class)
    private WrapStringRecord stringValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapCharacterRecordJavaType.class)
    private WrapCharacterRecord charValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapBooleanRecordJavaType.class)
    private WrapBooleanRecord boolValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapUUIDRecordJavaType.class)
    @JdbcType(UUIDJdbcType.class)
    private WrapUUIDRecord uuidValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapUUIDRecordJavaType.class)
    @JdbcType(CharJdbcType.class)
    private WrapUUIDRecord uuidValueAsString;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapBigDecimalClassJavaType.class)
    private WrapBigDecimalClass bigDecimalValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapBigIntegerClassJavaType.class)
    private WrapBigIntegerClass bigIntegerValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapDoubleClassJavaType.class)
    private WrapDoubleClass doubleValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapFloatClassJavaType.class)
    private WrapFloatClass floatValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapIntegerClassJavaType.class)
    private WrapIntegerClass integerValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapLongClassJavaType.class)
    private WrapLongClass longValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapScaledBigDecimalClassJavaType.class)
    private WrapScaledBigDecimalClass scaledBigDecimalValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapShortClassJavaType.class)
    private WrapShortClass shortValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapByteClassJavaType.class)
    private WrapByteClass byteValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapStringClassJavaType.class)
    private WrapStringClass stringValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapCharacterClassJavaType.class)
    private WrapCharacterClass charValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapBooleanClassJavaType.class)
    private WrapBooleanClass boolValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapUUIDClassJavaType.class)
    @JdbcType(UUIDJdbcType.class)
    private WrapUUIDClass uuidValueClass;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapUUIDClassJavaType.class)
    @JdbcType(CharJdbcType.class)
    private WrapUUIDClass uuidValueClassAsString;

    public record ID(
            Long value
    ) implements EntityId<Long> {

        @Override
        public String toString() {
            return String.valueOf(value());
        }

        public static class JavaType extends WrappedLongJavaType<ID> implements AutoRegisteredJavaType<ID> {
            public JavaType() {
                super(ID.class);
            }
        }
    }
}
