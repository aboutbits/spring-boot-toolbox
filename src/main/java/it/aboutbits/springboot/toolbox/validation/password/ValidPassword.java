package it.aboutbits.springboot.toolbox.validation.password;

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

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
