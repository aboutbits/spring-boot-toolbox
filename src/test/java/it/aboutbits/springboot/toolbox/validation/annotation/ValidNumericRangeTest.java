package it.aboutbits.springboot.toolbox.validation.annotation;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class ValidNumericRangeTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private final Validator validator = VALIDATOR_FACTORY.getValidator();

    @Test
    void testIsValidWithScaledBigDecimalValidRange() {
        var testObject = new TestObject(
                ScaledBigDecimal.valueOf(10.5),
                ScaledBigDecimal.valueOf(20.5)
        );

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithScaledBigDecimalEqualValues() {
        var testObject = new TestObject(
                ScaledBigDecimal.valueOf(15.0),
                ScaledBigDecimal.valueOf(15.0)
        );

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithScaledBigDecimalEqualValuesWhenNotAllowed() {
        var testObject = new TestObjectNoEqual(
                ScaledBigDecimal.valueOf(15.0),
                ScaledBigDecimal.valueOf(15.0)
        );

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
    }

    @Test
    void testIsValidWithScaledBigDecimalInvalidRange() {
        var testObject = new TestObject(
                ScaledBigDecimal.valueOf(30.5),
                ScaledBigDecimal.valueOf(20.5)
        );

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getPropertyPath().toString()).isEqualTo("upperBound");
        });
    }

    @Test
    void testIsValidWithNullValues() {
        var testObject = new TestObject(null, null);

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithNullLowerBound() {
        var testObject = new TestObject(null, ScaledBigDecimal.valueOf(20.5));

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithNullUpperBound() {
        var testObject = new TestObject(ScaledBigDecimal.valueOf(10.5), null);

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @ValidNumericRange(lowerBoundField = "lowerBound", upperBoundField = "upperBound")
    record TestObject(
            @Nullable Object lowerBound,
            @Nullable Object upperBound
    ) {
    }

    @ValidNumericRange(lowerBoundField = "lowerBound", upperBoundField = "upperBound", allowEqualValues = false)
    record TestObjectNoEqual(
            @Nullable Object lowerBound,
            @Nullable Object upperBound
    ) {
    }
}
