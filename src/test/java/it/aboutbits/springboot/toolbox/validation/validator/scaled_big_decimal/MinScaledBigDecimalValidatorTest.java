package it.aboutbits.springboot.toolbox.validation.validator.scaled_big_decimal;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class MinScaledBigDecimalValidatorTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

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
