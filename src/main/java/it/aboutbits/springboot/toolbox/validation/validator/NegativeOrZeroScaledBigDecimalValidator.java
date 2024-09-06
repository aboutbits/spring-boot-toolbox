package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NegativeOrZero;

public class NegativeOrZeroScaledBigDecimalValidator implements ConstraintValidator<NegativeOrZero, ScaledBigDecimal> {
    @Override
    public boolean isValid(ScaledBigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.compareTo(ScaledBigDecimal.ZERO) < 1;
    }
}
