package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ScaledBigDecimalValidatorTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Nested
    class PositiveConstraint {
        record Sut(@Positive ScaledBigDecimal value) {
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0000001, 1, 100_000_000.9999})
        void valid_shouldSucceed(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, -0.0000001, 0})
        void inValid_shouldFail(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isNotEmpty();
        }
    }

    @Nested
    class PositiveOrZeroConstraint {
        record Sut(@PositiveOrZero ScaledBigDecimal value) {
        }

        @ParameterizedTest
        @ValueSource(doubles = {0, 0.0000001, 1, 100_000_000.9999})
        void valid_shouldSucceed(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, -0.0000001})
        void inValid_shouldFail(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isNotEmpty();
        }
    }

    @Nested
    class NegativeConstraint {
        record Sut(@Negative ScaledBigDecimal value) {
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, -0.0000001})
        void valid_shouldSucceed(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(doubles = {0, 0.0000001, 1, 100_000_000.9999})
        void inValid_shouldFail(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isNotEmpty();
        }
    }

    @Nested
    class NegativeOrZeroConstraint {
        record Sut(@NegativeOrZero ScaledBigDecimal value) {
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, -0.0000001, 0})
        void valid_shouldSucceed(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0000001, 1, 100_000_000.9999})
        void inValid_shouldFail(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isNotEmpty();
        }
    }

    @Nested
    class MinConstraint {
        record Sut(@Min(50) ScaledBigDecimal value) {
        }

        @ParameterizedTest
        @ValueSource(doubles = {50, 50.0000001, 100_000_000.9999})
        void valid_shouldSucceed(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(doubles = {-1, -0.0000001, 0, 49, 49.999999})
        void inValid_shouldFail(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isNotEmpty();
        }
    }

    @Nested
    class MaxConstraint {
        record Sut(@Max(-50) ScaledBigDecimal value) {
        }

        @ParameterizedTest
        @ValueSource(doubles = {-100_000_000.99999, -50.000001, -50})
        void valid_shouldSucceed(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(doubles = {-49.999999, -49, 0, 100_000_000})
        void inValid_shouldFail(double doubleValue) {
            var validator = VALIDATOR_FACTORY.getValidator();

            var instance = new Sut(ScaledBigDecimal.valueOf(doubleValue));

            var violations = validator.validate(instance);

            assertThat(violations).isNotEmpty();
        }
    }
}
