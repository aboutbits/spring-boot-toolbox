package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import it.aboutbits.springboot.toolbox.validation.annotation.ValidNumericRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.PropertyAccessorFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

@RequiredArgsConstructor
@NullMarked
public class ValidNumericRangeValidator implements ConstraintValidator<ValidNumericRange, Object> {
    @Nullable
    private String lowerBoundField;
    @Nullable
    private String upperBoundField;
    @Nullable
    private String message;
    private boolean allowEqualValues = true;

    @Override
    public void initialize(ValidNumericRange constraintAnnotation) {
        this.lowerBoundField = constraintAnnotation.lowerBoundField();
        this.upperBoundField = constraintAnnotation.upperBoundField();
        this.message = constraintAnnotation.message();
        this.allowEqualValues = constraintAnnotation.allowEqualValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (lowerBoundField == null || upperBoundField == null || message == null) {
            throw new IllegalStateException("All fields must be set (lowerBoundField, upperBoundField, message).");
        }

        var propertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(value);
        var lowerBound = propertyAccessor.getPropertyValue(lowerBoundField);
        var upperBound = propertyAccessor.getPropertyValue(upperBoundField);

        if (lowerBound == null || upperBound == null) {
            return true; // Consider null values valid; use @NotNull for null checks if needed
        }

        var isValid = false;

        if (lowerBound instanceof ScaledBigDecimal lower && upperBound instanceof ScaledBigDecimal upper) {
            isValid = compareValues(lower.compareTo(upper));
        } else if (lowerBound instanceof ScaledBigDecimal || upperBound instanceof ScaledBigDecimal) {
            throw new IllegalArgumentException("Both fields must be of the same type!");
        } else {
            if (!(lowerBound instanceof Number) || !(upperBound instanceof Number)) {
                throw new IllegalArgumentException("Both fields must be numeric types!");
            }

            isValid = compareNumbers((Number) lowerBound, (Number) upperBound);
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(upperBoundField)
                    .addConstraintViolation();
        }

        return isValid;
    }

    private boolean compareNumbers(Number lowerBound, Number upperBound) {
        // Convert both numbers to BigDecimal for accurate comparison
        var lower = convertToBigDecimal(lowerBound);
        var upper = convertToBigDecimal(upperBound);

        return compareValues(lower.compareTo(upper));
    }

    private boolean compareValues(int comparisonResult) {
        if (allowEqualValues) {
            return comparisonResult <= 0; // lower <= upper
        } else {
            return comparisonResult < 0;  // lower < upper
        }
    }

    private BigDecimal convertToBigDecimal(Number number) {
        if (number instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        } else if (number instanceof BigInteger bigInteger) {
            return new BigDecimal(bigInteger);
        } else if (number instanceof Double || number instanceof Float) {
            return BigDecimal.valueOf(number.doubleValue());
        } else {
            return BigDecimal.valueOf(number.longValue());
        }
    }
}
