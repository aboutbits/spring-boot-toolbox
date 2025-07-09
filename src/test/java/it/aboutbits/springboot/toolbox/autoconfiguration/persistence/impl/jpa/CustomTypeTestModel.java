package it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.converter.UUIDConverter;
import it.aboutbits.springboot.toolbox.persistence.javatype.EmailAddressJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.IbanJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.ScaledBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
import it.aboutbits.springboot.toolbox.type.EmailAddress;
import it.aboutbits.springboot.toolbox.type.Iban;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.type.identity.Identified;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JavaType;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "custom_type_test_model")
public class CustomTypeTestModel implements Identified<CustomTypeTestModel.ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JavaType(CustomTypeTestModel.ID.JavaType.class)
    private ID id;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(EmailAddressJavaType.class)
    private EmailAddress email;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(IbanJavaType.class)
    private Iban iban;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(ScaledBigDecimalJavaType.class)
    private ScaledBigDecimal accountBalance;

    private UUID uuid;

    @Convert(converter = UUIDConverter.class)
    private UUID uuidAsString;

    @JavaType(ReferencedTestModel.ID.JavaType.class)
    private ReferencedTestModel.ID referencedId;

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
