package it.aboutbits.springboot.toolbox.type;

import it.aboutbits.springboot.toolbox._support.ArchIgnoreGroupName;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@NullMarked
class EmailAddressTest {
    @Nested
    @ArchIgnoreGroupName
    class Constructor {
        @ParameterizedTest
        @ValueSource(strings = {"egon@aboutbits.it", "hans.mueller@aboutbits.it", "peterP_pansky@aboutbits.it"})
        void validValues_shouldSucceed(String value) {
            assertThatCode(
                    () -> new EmailAddress(value)
            ).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                        "",
                        " ",
                        "         ",
                        "\t",
                        "\r",
                        "\n",
                        "sepp",
                        "x@ y",
                        "@aboutbits.it",
                        "hans mueller@aboutbits.it",
                        "peter@pansky@aboutbits.it"
                }
        )
        void invalidValues_shouldFail(String value) {
            assertThatIllegalArgumentException().isThrownBy(
                    () -> new EmailAddress(value)
            );
        }

        @Test
        void null_shouldFail() {
            assertThatIllegalArgumentException().isThrownBy(
                    () -> new EmailAddress(null)
            );
        }
    }

    @Nested
    @ArchIgnoreGroupName
    class ToStringAndValue {
        @Test
        void shouldAlwaysReturnTheSameValue() {
            var emailAddress = new EmailAddress("someValue@aboutbits.it");

            assertThat(emailAddress.value()).isEqualTo(emailAddress.toString());
        }

        @Test
        void shouldReturnLowercaseValue() {
            var emailAddress = new EmailAddress("someValue@aboutbits.it");

            assertThat(emailAddress.value()).isEqualTo("somevalue@aboutbits.it");
        }
    }

    @Nested
    class CompareTo {
        @Test
        void shouldCompareToReturnNegativeWhenInputValueIsGreater() {
            var emailAddress1 = new EmailAddress("abc@aboutbits.it");
            var emailAddress2 = new EmailAddress("xyz@aboutbits.it");

            assertThat(emailAddress1.compareTo(emailAddress2)).isNegative();
        }

        @Test
        void shouldCompareToReturnPositiveWhenInputValueIsLesser() {
            var emailAddress1 = new EmailAddress("xyz@aboutbits.it");
            var emailAddress2 = new EmailAddress("abc@aboutbits.it");

            assertThat(emailAddress1.compareTo(emailAddress2)).isPositive();
        }

        @Test
        void shouldCompareToReturnZeroWhenInputValuesAreEqual() {
            var emailAddress1 = new EmailAddress("abc@aboutbits.it");
            var emailAddress2 = new EmailAddress("abc@aboutbits.it");

            assertThat(emailAddress1).isEqualByComparingTo(emailAddress2);
        }
    }
}
