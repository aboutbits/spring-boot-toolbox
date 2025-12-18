package it.aboutbits.springboot.toolbox.reflection.util;

import it.aboutbits.springboot.toolbox.autoconfiguration.web.CustomTypeScanner;
import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@NullMarked
class CustomTypeReflectionUtilTest {
    @Test
    void testGetCustomTypeConstructorWithValidCustomType() throws Exception {
        // given
        var customTypeClass = ValidCustomType.class;

        // when
        var constructor = CustomTypeReflectionUtil.getCustomTypeConstructor(customTypeClass);

        // then
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterCount()).isEqualTo(1);
        assertThat(constructor.getParameterTypes()[0]).isEqualTo(String.class);
    }

    @Test
    void testGetCustomTypeConstructorWithMissingConstructor() {
        // given
        var customTypeClass = InvalidCustomType.class;

        // then
        assertThatExceptionOfType(NoSuchMethodException.class).isThrownBy(
                () -> CustomTypeReflectionUtil.getCustomTypeConstructor(customTypeClass) // when
        );
    }

    @Test
    void testGetCustomTypeConstructorWithMismatchingConstructor() {
        // given
        var customTypeClass = OtherInvalidCustomType.class;

        // then
        assertThatExceptionOfType(NoSuchMethodException.class).isThrownBy(
                () -> CustomTypeReflectionUtil.getCustomTypeConstructor(customTypeClass) // when
        );
    }

    @Test
    void testGetCustomTypeConstructorWithInheritedCustomType() throws Exception {
        // given
        var customTypeClass = ChildClass.class;

        // when
        var constructor = CustomTypeReflectionUtil.getCustomTypeConstructor(customTypeClass);

        // then
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterCount()).isEqualTo(1);
        assertThat(constructor.getParameterTypes()[0]).isEqualTo(Integer.class);
    }

    @SuppressWarnings("checkstyle:RedundantModifier")
    @CustomTypeScanner.DisableCustomTypeConfiguration
    public static class ValidCustomType implements CustomType<String> {
        private final String value;

        public ValidCustomType(Long otherValue) {
            this.value = "";
        }

        public ValidCustomType(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }

    @CustomTypeScanner.DisableCustomTypeConfiguration
    public static class InvalidCustomType implements CustomType<String> {
        @SuppressWarnings("NullAway")
        @Override
        public @Nullable String value() {
            return null;
        }

        // Missing the required constructor
    }

    @SuppressWarnings("checkstyle:RedundantModifier")
    @CustomTypeScanner.DisableCustomTypeConfiguration
    public static class OtherInvalidCustomType implements CustomType<String> {
        private final String value;

        // wrong type constructor
        public OtherInvalidCustomType(Long value) {
            this.value = "";
        }

        @Override
        public String value() {
            return value;
        }
    }

    @SuppressWarnings("checkstyle:RedundantModifier")
    @CustomTypeScanner.DisableCustomTypeConfiguration
    public static class ParentClass implements CustomType<Integer> {
        private final Integer value;

        public ParentClass(Integer value) {
            this.value = value;
        }

        @Override
        public Integer value() {
            return value;
        }
    }

    @SuppressWarnings("checkstyle:RedundantModifier")
    @CustomTypeScanner.DisableCustomTypeConfiguration
    public static class ChildClass extends ParentClass {
        public ChildClass(Integer value) {
            super(value);
        }
    }
}
