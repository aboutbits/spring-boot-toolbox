package it.aboutbits.springboot.toolbox.validation.annotation;

import it.aboutbits.springboot.toolbox.validation.validator.ValidNumericRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator bean to check if the lower bound numeric field is not higher than the upper bound numeric field.<br>
 * If one of the two fields is {@code null} this validation bean will still return true, as in a form, for example,
 * only one field could be specified.
 * <p>
 * Supports numeric types like {@link Integer}, {@link Long}, {@link Float}, {@link Double}, etc.
 */
@Documented
@Constraint(validatedBy = ValidNumericRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Repeatable(ValidNumericRange.List.class)
public @interface ValidNumericRange {
    String message() default "{shared.error.validation.invalidNumericRange}";

    boolean allowEqualValues() default true;

    String lowerBoundField();

    String upperBoundField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidNumericRange[] value();
    }
}
