package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Max;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class MaxScaledBigDecimalValidator implements ConstraintValidator<Max, ScaledBigDecimal> {

    private long upperBound;

    @Override
    public void initialize(Max constraintAnnotation) {
        this.upperBound = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(@Nullable ScaledBigDecimal scaledBigDecimal, ConstraintValidatorContext context) {
        if (scaledBigDecimal == null) {
            return true;
        }

        return scaledBigDecimal.compareTo(new ScaledBigDecimal(upperBound)) <= 0;
    }
}
