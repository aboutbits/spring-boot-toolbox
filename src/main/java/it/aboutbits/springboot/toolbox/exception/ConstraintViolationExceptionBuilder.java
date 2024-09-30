package it.aboutbits.springboot.toolbox.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import lombok.NonNull;
import org.hibernate.validator.internal.engine.path.PathImpl;

import java.util.HashSet;
import java.util.Set;

public final class ConstraintViolationExceptionBuilder {
    private final Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();

    private ConstraintViolationExceptionBuilder() {

    }

    public static ConstraintViolationExceptionBuilder constraintViolation() {
        return new ConstraintViolationExceptionBuilder();
    }

    public ConstraintViolationExceptionBuilder causedBy(@NonNull String propertyPath, String message) {
        var constraintViolation = new CustomConstraintViolation(message, PathImpl.createPathFromString(propertyPath));
        constraintViolations.add(constraintViolation);
        return this;
    }

    public ConstraintViolationExceptionBuilder causedBy(@NonNull ConstraintViolationException e) {
        constraintViolations.addAll(e.getConstraintViolations());
        return this;
    }

    public ConstraintViolationException create() {
        return new ConstraintViolationException(constraintViolations);
    }

    public boolean hasCauses() {
        return !constraintViolations.isEmpty();
    }

    private record CustomConstraintViolation(String message, Path propertyPath) implements ConstraintViolation<Object> {
        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public Object getRootBean() {
            return null;
        }

        @Override
        public Class<Object> getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return propertyPath;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }
}
