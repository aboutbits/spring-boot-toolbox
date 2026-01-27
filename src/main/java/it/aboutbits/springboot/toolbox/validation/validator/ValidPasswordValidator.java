package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private @Nullable String message;
    private @Nullable String lengthMessage;
    private int minLength = 8;
    private int maxLength = 50;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.lengthMessage = constraintAnnotation.lengthMessage();
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(@Nullable String password, ConstraintValidatorContext context) {
        if (message == null || lengthMessage == null) {
            throw new IllegalStateException("Both message and lengthMessage must be set.");
        }

        if (minLength > maxLength) {
            throw new IllegalStateException("minLength must be less than maxLength.");
        }

        if (password == null) {
            return true; // Consider null as valid; use @NotNull for null checks if needed
        }

        var length = password.length();
        var lengthValid = length >= minLength && length <= maxLength;

        if (StringUtils.isNotBlank(password) && lengthValid) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        if (!lengthValid) {
            context.buildConstraintViolationWithTemplate(lengthMessage)
                    .addConstraintViolation();
        } else {
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return false;
    }
}
