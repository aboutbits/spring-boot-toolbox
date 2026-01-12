package it.aboutbits.springboot.toolbox.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@NullMarked
public final class ConstraintViolationExceptionBuilder {
    private final Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();

    private ConstraintViolationExceptionBuilder() {
    }

    @SuppressWarnings("unused")
    public static ConstraintViolationExceptionBuilder constraintViolation() {
        return new ConstraintViolationExceptionBuilder();
    }

    @SuppressWarnings("unused")
    public ConstraintViolationExceptionBuilder causedBy(
            String propertyPath,
            ExceptionMessageDefinition message
    ) {
        return causedBy(propertyPath, message.code());
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public ConstraintViolationExceptionBuilder causedBy(String propertyPath, String message) {
        var constraintViolation = new CustomConstraintViolation(message, PathImpl.createPathFromString(propertyPath));
        constraintViolations.add(constraintViolation);
        return this;
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public ConstraintViolationExceptionBuilder causedBy(ConstraintViolationException e) {
        constraintViolations.addAll(e.getConstraintViolations());
        return this;
    }

    @SuppressWarnings("unused")
    public ConstraintViolationException create() {
        return new ConstraintViolationException(constraintViolations);
    }

    @SuppressWarnings("unused")
    public boolean hasCauses() {
        return !constraintViolations.isEmpty();
    }

    @NullMarked
    public record CustomConstraintViolation(String message, Path propertyPath) implements ConstraintViolation<Object> {
        @Override
        public String getMessage() {
            return message;
        }

        @Nullable
        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Nullable
        @Override
        public Object getRootBean() {
            return null;
        }

        @Nullable
        @Override
        public Class<Object> getRootBeanClass() {
            return null;
        }

        @Nullable
        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Nullable
        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return propertyPath;
        }

        @Nullable
        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Nullable
        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Nullable
        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }
}
