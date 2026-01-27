package it.aboutbits.springboot.toolbox.validation.annotation;

import it.aboutbits.springboot.toolbox.validation.validator.ValidDateRangeValidator;
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
 * Validator bean to check if the {@code to} date field is after the {@code from} date field.<br>
 * If one of the two fields is {@code null} this validation bean will still return true, as in a form, for example,
 * the {@code from} field could be specified.
 * <p>
 * Supports {@link java.time.LocalDateTime}, {@link java.time.LocalDate}, {@link java.time.OffsetDateTime},
 * {@link java.time.ZonedDateTime}, and {@link java.time.Instant}.
 */
@Documented
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Repeatable(ValidDateRange.List.class)
public @interface ValidDateRange {
    String message() default "{shared.error.validation.invalidDateRange}";

    boolean allowEmptyRange() default false;

    String fromDateField();

    String toDateField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidDateRange[] value();
    }
}
