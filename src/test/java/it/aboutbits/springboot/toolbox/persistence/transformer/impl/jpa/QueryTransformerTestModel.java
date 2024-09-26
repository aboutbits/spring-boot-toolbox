package it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
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

@Entity
@Getter
@Setter
@Builder
@Table(name = "query_transformer_test_model")
@NoArgsConstructor
@AllArgsConstructor
public class QueryTransformerTestModel implements Identified<QueryTransformerTestModel.ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JavaType(ID.JavaType.class)
    private ID id;

    private String name;

    private String email;

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
