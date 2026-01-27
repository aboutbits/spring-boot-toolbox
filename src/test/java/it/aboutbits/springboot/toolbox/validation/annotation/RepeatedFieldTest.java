package it.aboutbits.springboot.toolbox.validation.annotation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class RepeatedFieldTest {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private final Validator validator = VALIDATOR_FACTORY.getValidator();

    @Test
    void testIsValidWithMatchingFields() {
        var testObject = new TestObject("password123", "password123");

        var result = validator.validate(testObject);

        assertThat(result).isEmpty();
    }

    @Test
    void testIsValidWithNonMatchingFields() {
        var testObject = new TestObject("password123", "differentPassword");

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getPropertyPath().toString()).isEqualTo("repeatPassword");
            assertThat(violation.getMessageTemplate()).isEqualTo("{shared.error.validation.repetitionMismatch}");
        });
    }

    @Test
    void testIsValidWithNullValues() {
        var testObject = new TestObject(null, null);

        var result = validator.validate(testObject);

        // Based on implementation: fieldValue != null && fieldValue.equals(fieldMatchValue)
        // So null, null should be INVALID
        assertThat(result).isNotEmpty();
    }

    @Test
    void testIsValidWithOneNullValue() {
        var testObject = new TestObject("password123", null);

        var result = validator.validate(testObject);

        assertThat(result).isNotEmpty();
    }

    @Test
    void testIsValidWithMultipleAnnotations() {
        var testObject = new MultipleRepeatedFieldsObject(
                "email@example.com",
                "email@example.com",
                "password",
                "wrong-password"
        );

        var result = validator.validate(testObject);

        assertThat(result).hasSize(1);
        assertThat(result).anySatisfy(violation -> {
            assertThat(violation.getPropertyPath().toString()).isEqualTo("repeatPassword");
        });

        testObject = new MultipleRepeatedFieldsObject(
                "email@example.com",
                "wrong-email@example.com",
                "password",
                "wrong-password"
        );
        result = validator.validate(testObject);
        assertThat(result).hasSize(2);
    }

    @RepeatedField(originalField = "password", repeatedField = "repeatPassword")
    record TestObject(
            @Nullable String password,
            @Nullable String repeatPassword
    ) {
    }

    @RepeatedField(originalField = "email", repeatedField = "repeatEmail")
    @RepeatedField(originalField = "password", repeatedField = "repeatPassword")
    record MultipleRepeatedFieldsObject(
            @Nullable String email,
            @Nullable String repeatEmail,
            @Nullable String password,
            @Nullable String repeatPassword
    ) {
    }
}
