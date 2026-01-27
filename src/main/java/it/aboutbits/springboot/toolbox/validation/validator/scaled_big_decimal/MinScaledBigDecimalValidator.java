package it.aboutbits.springboot.toolbox.validation.validator.scaled_big_decimal;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Min;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class MinScaledBigDecimalValidator implements ConstraintValidator<Min, ScaledBigDecimal> {

    private @Nullable Long lowerBound;

    @Override
    public void initialize(Min constraintAnnotation) {
        this.lowerBound = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(@Nullable ScaledBigDecimal scaledBigDecimal, ConstraintValidatorContext context) {
        if (lowerBound == null) {
            throw new IllegalStateException("lowerBound must be initialized");
        }

        if (scaledBigDecimal == null) {
            return true;
        }

        return scaledBigDecimal.compareTo(new ScaledBigDecimal(lowerBound)) >= 0;
    }
}
