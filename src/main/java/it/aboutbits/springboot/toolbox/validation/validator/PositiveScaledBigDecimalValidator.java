package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Positive;

public class PositiveScaledBigDecimalValidator implements ConstraintValidator<Positive, ScaledBigDecimal> {
    @Override
    public boolean isValid(ScaledBigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.compareTo(ScaledBigDecimal.ZERO) > 0;
    }
}
