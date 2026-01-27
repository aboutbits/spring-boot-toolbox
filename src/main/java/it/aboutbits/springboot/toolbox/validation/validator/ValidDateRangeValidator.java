package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.validation.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.PropertyAccessorFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@NullMarked
public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    @Nullable
    private String fromDateField;
    @Nullable
    private String toDateField;
    @Nullable
    private String message;
    private boolean allowEmptyRange = false;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.fromDateField = constraintAnnotation.fromDateField();
        this.toDateField = constraintAnnotation.toDateField();
        this.message = constraintAnnotation.message();
        this.allowEmptyRange = constraintAnnotation.allowEmptyRange();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (fromDateField == null || toDateField == null || message == null) {
            throw new IllegalStateException("All fields must be set (fromDateField, toDateField, message).");
        }

        var propertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(value);
        var fromDate = propertyAccessor.getPropertyValue(fromDateField);
        var toDate = propertyAccessor.getPropertyValue(toDateField);

        if (fromDate == null || toDate == null) {
            return true; // Consider null dates valid; use @NotNull for null checks if needed
        }

        boolean isValid = switch (fromDate) {
            case LocalDate from when toDate instanceof LocalDate to ->
                    allowEmptyRange ? (to.isEqual(from) || to.isAfter(from)) : to.isAfter(from);
            case LocalDateTime from when toDate instanceof LocalDateTime to ->
                    allowEmptyRange ? (to.isEqual(from) || to.isAfter(from)) : to.isAfter(from);
            case OffsetDateTime from when toDate instanceof OffsetDateTime to ->
                    allowEmptyRange ? (to.isEqual(from) || to.isAfter(from)) : to.isAfter(from);
            case ZonedDateTime from when toDate instanceof ZonedDateTime to ->
                    allowEmptyRange ? (to.isEqual(from) || to.isAfter(from)) : to.isAfter(from);
            case Instant from when toDate instanceof Instant to ->
                    allowEmptyRange ? (to.equals(from) || to.isAfter(from)) : to.isAfter(from);
            default -> throw new IllegalArgumentException("Unsupported date data types!");
        };

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(toDateField)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
