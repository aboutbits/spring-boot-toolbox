package it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.ScaledBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.type.identity.Identified;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JavaType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@Entity
@Getter
@Setter
@Builder
@Table(name = "query_transformer_test_model")
@NoArgsConstructor
@AllArgsConstructor
@NullUnmarked
public class QueryTransformerTestModel implements Identified<QueryTransformerTestModel.@NonNull ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JavaType(ID.JavaType.class)
    private ID id;

    private String name;

    private String email;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JavaType(ScaledBigDecimalJavaType.class)
    private ScaledBigDecimal scaledBigDecimalValue;

    @NullMarked
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
