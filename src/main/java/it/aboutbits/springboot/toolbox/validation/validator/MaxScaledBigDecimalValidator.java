package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Max;

public class MaxScaledBigDecimalValidator implements ConstraintValidator<Max, ScaledBigDecimal> {

    private long upperBound;

    @Override
    public void initialize(Max constraintAnnotation) {
        this.upperBound = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(ScaledBigDecimal scaledBigDecimal, ConstraintValidatorContext context) {
        if (scaledBigDecimal == null) {
            return true;
        }

        return scaledBigDecimal.compareTo(new ScaledBigDecimal(upperBound)) <= 0;
    }
}
