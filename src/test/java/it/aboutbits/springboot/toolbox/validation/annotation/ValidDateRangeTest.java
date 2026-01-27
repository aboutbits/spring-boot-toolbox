package it.aboutbits.springboot.toolbox.validation.annotation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class ValidDateRangeTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private final Validator validator = VALIDATOR_FACTORY.getValidator();

    @Test
    void testIsValidWithValidRange() {
        var testObject = new TestObject(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2)
        );

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithEqualValues() {
        var testObject = new TestObject(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 1)
        );

        var result = validator.validate(testObject);

        // Default allowEmptyRange is false
        assertThat(result).isNotEmpty();
    }

    @Test
    void testIsValidWithEqualValuesWhenAllowed() {
        var testObject = new TestObjectEmptyAllowed(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 1)
        );

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithInvalidRange() {
        var testObject = new TestObject(
                LocalDate.of(2023, 1, 2),
                LocalDate.of(2023, 1, 1)
        );

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getPropertyPath().toString()).isEqualTo("toDate");
        });
    }

    @Test
    void testIsValidWithNullValues() {
        var testObject = new TestObject(null, null);

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @ValidDateRange(fromDateField = "fromDate", toDateField = "toDate")
    record TestObject(
            @Nullable LocalDate fromDate,
            @Nullable LocalDate toDate
    ) {
    }

    @ValidDateRange(fromDateField = "fromDate", toDateField = "toDate", allowEmptyRange = true)
    record TestObjectEmptyAllowed(
            @Nullable LocalDate fromDate,
            @Nullable LocalDate toDate
    ) {
    }
}
