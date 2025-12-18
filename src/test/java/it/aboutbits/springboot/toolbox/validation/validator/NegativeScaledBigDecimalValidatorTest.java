package it.aboutbits.springboot.toolbox.validation.validator;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Negative;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class NegativeScaledBigDecimalValidatorTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

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
