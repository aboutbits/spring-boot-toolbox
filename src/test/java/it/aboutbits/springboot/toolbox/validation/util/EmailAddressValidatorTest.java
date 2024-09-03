package it.aboutbits.springboot.toolbox.validation.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class EmailAddressValidatorTest {
    @Nested
    class IsValid {
        @ParameterizedTest
        @ValueSource(strings = {"egon@aboutbits.it", "hans.mueller@aboutbits.it", "peterP_pansky@aboutbits.it"})
        void validValues_shouldSucceed(String value) {
            assertThat(
                    EmailAddressValidator.isValid(value)
            ).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "         ", "\t", "\r", "\n", "sepp", "x@ y", "@aboutbits.it", "hans mueller@aboutbits.it", "peter@pansky@aboutbits.it"})
        void invalidValues_shouldFail(String value) {
            assertThat(
                    EmailAddressValidator.isValid(value)
            ).isFalse();
        }

        @Test
        void null_shouldFail() {
            assertThat(
                    EmailAddressValidator.isValid(null)
            ).isFalse();
        }
    }

    @Nested
    class IsNotValid {
        @ParameterizedTest
        @ValueSource(strings = {"egon@aboutbits.it", "hans.mueller@aboutbits.it", "peterP_pansky@aboutbits.it"})
        void validValues_shouldSucceed(String value) {
            assertThat(
                    EmailAddressValidator.isNotValid(value)
            ).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "         ", "\t", "\r", "\n", "sepp", "x@ y", "@aboutbits.it", "hans mueller@aboutbits.it", "peter@pansky@aboutbits.it"})
        void invalidValues_shouldFail(String value) {
            assertThat(
                    EmailAddressValidator.isNotValid(value)
            ).isTrue();
        }

        @Test
        void null_shouldFail() {
            assertThat(
                    EmailAddressValidator.isNotValid(null)
            ).isTrue();
        }
    }
}
