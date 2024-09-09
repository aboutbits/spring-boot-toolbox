package it.aboutbits.springboot.toolbox.boot.persistence.impl.jpa;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.identity.EntityId;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;

public class ReferencedTestModel {
    // we just use this to have and ID we can actually reference

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
