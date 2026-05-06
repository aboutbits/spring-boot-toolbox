package it.aboutbits.springboot.toolbox.autoconfiguration.web;

import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@NullMarked
class CustomTypeScannerTest {
    @Test
    @SuppressWarnings("unchecked")
    void findAllCustomTypes_shouldFilterOutTypesMissingConstructor() {
        // given
        ClassScannerUtil.ClassScanner classScanner = mock(ClassScannerUtil.ClassScanner.class);
        when(classScanner.getSubTypesOf(CustomType.class)).thenReturn(Set.of(
                EnumCustomType.class,
                InvalidCustomType.class,
                UnsupportedWrappedTypeCustomType.class
        ));

        // when
        Set<Class<? extends CustomType>> result = CustomTypeScanner.findAllCustomTypes(classScanner);

        // then
        assertThat(result)
                .containsExactly(EnumCustomType.class)
                .doesNotContain(InvalidCustomType.class)
                .doesNotContain(UnsupportedWrappedTypeCustomType.class);
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
    }

    @SuppressWarnings("checkstyle:RedundantModifier")
    public static class UnsupportedWrappedTypeCustomType implements CustomType<Object> {
        private final Object value;

        public UnsupportedWrappedTypeCustomType(Object value) {
            this.value = value;
        }

        @Override
        public Object value() {
            return value;
        }
    }
}
