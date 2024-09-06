package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Min;

public class MinScaledBigDecimalValidator implements ConstraintValidator<Min, ScaledBigDecimal> {

    private Long lowerBound;

    @Override
    public void initialize(Min constraintAnnotation) {
        this.lowerBound = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(ScaledBigDecimal scaledBigDecimal, ConstraintValidatorContext context) {
        if (scaledBigDecimal == null) {
            return true;
        }

        return scaledBigDecimal.compareTo(new ScaledBigDecimal(lowerBound)) >= 0;
    }
}
