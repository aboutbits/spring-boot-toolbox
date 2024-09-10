package it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.type.identity.Identified;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapBigIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapDoubleJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapFloatJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapLongJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapScaledBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapShortJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype.WrapStringJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimal;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigInteger;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDouble;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloat;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapInteger;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLong;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimal;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShort;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapString;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JavaType;

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
    @JavaType(WrapBigDecimalJavaType.class)
    private WrapBigDecimal bigDecimalValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapBigIntegerJavaType.class)
    private WrapBigInteger bigIntegerValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapDoubleJavaType.class)
    private WrapDouble doubleValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapFloatJavaType.class)
    private WrapFloat floatValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapIntegerJavaType.class)
    private WrapInteger integerValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapLongJavaType.class)
    private WrapLong longValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapScaledBigDecimalJavaType.class)
    private WrapScaledBigDecimal scaledBigDecimalValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapShortJavaType.class)
    private WrapShort shortValue;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(WrapStringJavaType.class)
    private WrapString stringValue;

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
