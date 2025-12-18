package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Max;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class MaxScaledBigDecimalValidatorTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

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
