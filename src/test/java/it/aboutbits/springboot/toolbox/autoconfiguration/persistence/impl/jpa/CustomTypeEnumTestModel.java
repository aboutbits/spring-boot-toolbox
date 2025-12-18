package it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedEnumJavaType;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.type.identity.Identified;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JavaType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@Entity
@Getter
@Setter
@Table(name = "custom_type_enum_test_model")
@NullUnmarked
public class CustomTypeEnumTestModel implements Identified<CustomTypeEnumTestModel.ID> {
    @Id
    @JavaType(CustomTypeEnumTestModel.ID.JavaType.class)
    private ID id;

    public enum CustomTypeEnum {
        ENUM_FIRST, ENUM_OTHER, ENUM_LAST
    }

    @NullMarked
    public record ID(
            CustomTypeEnum value
    ) implements EntityId<CustomTypeEnum> {

        @Override
        public String toString() {
            return value().name();
        }

        public static class JavaType extends WrappedEnumJavaType<ID> implements AutoRegisteredJavaType<ID> {
            public JavaType() {
                super(ID.class);
            }
        }
    }
}
