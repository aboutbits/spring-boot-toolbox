package it.aboutbits.springboot.toolbox.validation.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class IbanValidatorTest {
    @Nested
    class IsValid {
        @ParameterizedTest
        @ValueSource(strings = {"NL08INGB3330533676", "HR3425000097537651489", "IT37G0300203280237914848919", "AT705400073311799347", "DE60500105175378199617"})
        void validValues_shouldSucceed(String value) {
            assertThat(
                    IbanValidator.isValid(value)
            ).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"IT60X0542811101000000123450", "some-wrong-stuff", "", " ", "         ", "\t", "\r", "\n"})
        void invalidValues_shouldFail(String value) {
            assertThat(
                    IbanValidator.isValid(value)
            ).isFalse();
        }

        @Test
        void null_shouldFail() {
            assertThat(
                    IbanValidator.isValid(null)
            ).isFalse();
        }
    }

    @Nested
    class IsNotValid {
        @ParameterizedTest
        @ValueSource(strings = {"NL08INGB3330533676", "HR3425000097537651489", "IT37G0300203280237914848919", "AT705400073311799347", "DE60500105175378199617"})
        void validValues_shouldSucceed(String value) {
            assertThat(
                    IbanValidator.isNotValid(value)
            ).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"IT60X0542811101000000123450", "some-wrong-stuff", "", " ", "         ", "\t", "\r", "\n"})
        void invalidValues_shouldFail(String value) {
            assertThat(
                    IbanValidator.isNotValid(value)
            ).isTrue();
        }

        @Test
        void null_shouldFail() {
            assertThat(
                    IbanValidator.isNotValid(null)
            ).isTrue();
        }
    }
}
