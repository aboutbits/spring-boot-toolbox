package it.aboutbits.springboot.toolbox.jackson;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@NullMarked
class CustomTypeDeserializerTest {
    @Test
    void constructor_withValidCustomType_shouldNotThrow() {
        assertThatCode(() -> new CustomTypeDeserializer<>(EnumCustomType.class))
                .doesNotThrowAnyException();
    }

    @Test
    void constructor_withInvalidCustomType_shouldThrowException() {
        assertThatExceptionOfType(CustomTypeDeserializer.CustomTypeDeserializerException.class)
                .isThrownBy(() -> new CustomTypeDeserializer<>(InvalidCustomType.class))
                .withMessageContaining(
                        "Unable to find constructor for type: it.aboutbits.springboot.toolbox.jackson.CustomTypeDeserializerTest$InvalidCustomType"
                );
    }

    public enum EnumCustomType implements CustomType<EnumCustomType> {
        VALUE;

        @Override
        public EnumCustomType value() {
            return this;
        }
    }

    @SuppressWarnings("checkstyle:RedundantModifier")
    public static class InvalidCustomType implements CustomType<String> {
        @Override
        public String value() {
            return "test";
        }
        // Missing constructor(String)
    }
}
