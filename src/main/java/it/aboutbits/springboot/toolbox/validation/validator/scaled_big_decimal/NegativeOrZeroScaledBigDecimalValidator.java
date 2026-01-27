package it.aboutbits.springboot.toolbox.validation.validator.scaled_big_decimal;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NegativeOrZero;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class NegativeOrZeroScaledBigDecimalValidator implements ConstraintValidator<NegativeOrZero, ScaledBigDecimal> {
    @Override
    public boolean isValid(@Nullable ScaledBigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.compareTo(ScaledBigDecimal.ZERO) < 1;
    }
}
