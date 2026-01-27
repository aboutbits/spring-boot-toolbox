package it.aboutbits.springboot.toolbox.validation.annotation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class ValidPasswordTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private final Validator validator = VALIDATOR_FACTORY.getValidator();

    @Test
    void testIsValidWithValidPassword() {
        var testObject = new TestObject("validPassword123");

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithNullPassword() {
        var testObject = new TestObject(null);

        var result = validator.validate(testObject);

        // Password null is considered valid by the validator
        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithBlankPassword() {
        var testObject = new TestObject("");

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getMessageTemplate()).isEqualTo("{shared.error.validation.invalidPassword.length}");
        });

        testObject = new TestObject("   ");
        result = validator.validate(testObject);
        assertThat(result).isNotEmpty();
        // For blank but within length (e.g. 8 spaces), it should trigger the general message
    }

    @Test
    void testIsValidWithTooShortPassword() {
        var testObject = new TestObject("short");

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getMessageTemplate()).isEqualTo("{shared.error.validation.invalidPassword.length}");
        });
    }

    @Test
    void testIsValidWithTooLongPassword() {
        var testObject = new TestObject("thisPasswordIsWayTooLongAndShouldFailValidationBecauseItExceedsFiftyCharacters");

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getMessageTemplate()).isEqualTo("{shared.error.validation.invalidPassword.length}");
        });
    }

    @Test
    void testIsValidWithCustomLengths() {
        var testObject = new CustomLengthObject("pass"); // 4 chars, min is 5

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getMessageTemplate()).isEqualTo("too short");
        });

        testObject = new CustomLengthObject("password123"); // 11 chars, max is 10
        result = validator.validate(testObject);
        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getMessageTemplate()).isEqualTo("too short"); // The message template in CustomLengthObject is "too short" for lengthMessage
        });
    }

    @Test
    void testIsValidWithSpacesOnly() {
        // "        " is 8 spaces, so lengthValid is true, but StringUtils.isNotBlank is false
        var testObject = new TestObject("        ");

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getMessageTemplate()).isEqualTo("{shared.error.validation.invalidPassword}");
        });
    }

    record TestObject(
            @ValidPassword @Nullable String password
    ) {
    }

    record CustomLengthObject(
            @ValidPassword(
                    minLength = 5,
                    maxLength = 10,
                    lengthMessage = "too short",
                    message = "invalid"
            ) @Nullable String password
    ) {
    }
}
