package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.validation.annotation.RepeatedField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeanWrapperImpl;

@NullMarked
public class RepeatedFieldValidator implements ConstraintValidator<RepeatedField, Object> {
    private @Nullable String originalField;
    private @Nullable String repeatedField;
    private @Nullable String message;

    @Override
    public void initialize(RepeatedField constraintAnnotation) {
        this.originalField = constraintAnnotation.originalField();
        this.repeatedField = constraintAnnotation.repeatedField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (originalField == null || repeatedField == null) {
            throw new IllegalStateException("Both originalField and repeatedField must be set.");
        }

        var fieldValue = new BeanWrapperImpl(value).getPropertyValue(originalField);
        var fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(repeatedField);

        var isValid = fieldValue != null && fieldValue.equals(fieldMatchValue);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(repeatedField)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
