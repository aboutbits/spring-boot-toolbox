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
class IbanTest {
    @Nested
    @ArchIgnoreGroupName
    class Constructor {
        @ParameterizedTest
        @ValueSource(
                strings = {
                        "NL08INGB3330533676",
                        "HR3425000097537651489",
                        "IT37G0300203280237914848919",
                        "AT705400073311799347",
                        "DE60500105175378199617"
                }
        )
        void validValues_shouldSucceed(String value) {
            assertThatCode(
                    () -> new Iban(value)
            ).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(
                strings = {
                        "IT60X0542811101000000123450",
                        "some-wrong-stuff",
                        "",
                        " ",
                        "         ",
                        "\t",
                        "\r",
                        "\n"
                }
        )
        void invalidValues_shouldFail(String value) {
            assertThatIllegalArgumentException().isThrownBy(
                    () -> new Iban(value)
            );
        }

        @Test
        void null_shouldFail() {
            assertThatIllegalArgumentException().isThrownBy(
                    () -> new Iban(null)
            );
        }
    }

    @Nested
    @ArchIgnoreGroupName
    class ToStringAndValue {
        @Test
        void shouldAlwaysReturnTheSameValue() {
            var emailAddress = new Iban("HR3425000097537651489");

            assertThat(emailAddress.value()).isEqualTo(emailAddress.toString());
        }

        @Test
        void shouldReturnUppercaseValue() {
            var emailAddress = new Iban("hr3425000097537651489");

            assertThat(emailAddress.value()).isEqualTo("HR3425000097537651489");
        }
    }

    @Nested
    class CompareTo {
        @Test
        void shouldCompareToReturnNegativeWhenInputValueIsGreater() {
            var iban1 = new Iban("HR3425000097537651489");
            var iban2 = new Iban("NL08INGB3330533676");

            assertThat(iban1.compareTo(iban2)).isNegative();
        }

        @Test
        void shouldCompareToReturnPositiveWhenInputValueIsLesser() {
            var iban1 = new Iban("NL08INGB3330533676");
            var iban2 = new Iban("HR3425000097537651489");

            assertThat(iban1.compareTo(iban2)).isPositive();
        }

        @Test
        void shouldCompareToReturnZeroWhenInputValuesAreEqual() {
            var iban1 = new Iban("HR3425000097537651489");
            var iban2 = new Iban("HR3425000097537651489");

            assertThat(iban1).isEqualByComparingTo(iban2);
        }
    }
}
