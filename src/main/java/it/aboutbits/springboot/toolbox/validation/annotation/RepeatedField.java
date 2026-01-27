package it.aboutbits.springboot.toolbox.validation.annotation;

import it.aboutbits.springboot.toolbox.validation.validator.RepeatedFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = RepeatedFieldValidator.class)
@Documented
@Repeatable(RepeatedField.List.class)
public @interface RepeatedField {

    String message() default "{shared.error.validation.repetitionMismatch}";

    String originalField();

    String repeatedField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        RepeatedField[] value();
    }
}
