package it.aboutbits.springboot.toolbox.validation.annotation;

import it.aboutbits.springboot.toolbox.validation.validator.ValidPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidPasswordValidator.class)
@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "{shared.error.validation.invalidPassword}";

    String lengthMessage() default "{shared.error.validation.invalidPassword.length}";

    int minLength() default 8;

    int maxLength() default 50;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
