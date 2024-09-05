package it.aboutbits.springboot.toolbox.type.mvc;

import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.function.Function;

public final class CustomTypePropertyEditor<T extends CustomType<?>> extends PropertyEditorSupport {
    private final Constructor<T> constructor;
    private final Function<String, Object> typeConverter;

    @SneakyThrows
    public CustomTypePropertyEditor(@NonNull Class<T> customType) {
        this.constructor = RecordReflectionUtil.getCanonicalConstructor(customType);
        this.typeConverter = getTextToTypeConverter(
                constructor.getParameters()[0].getType()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public String getAsText() {
        var value = (T) getValue();

        return value == null ? null : String.valueOf(value.value());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        var value = typeConverter.apply(text);

        try {
            setValue(
                    constructor.newInstance(value)
            );
        } catch (
                IllegalAccessException
                | InvocationTargetException
                | InstantiationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Function<String, Object> getTextToTypeConverter(Class<?> wrappedType) {
        if (String.class.isAssignableFrom(wrappedType)) {
            return text -> text;
        } else if (Short.class.isAssignableFrom(wrappedType)) {
            return Short::parseShort;
        } else if (Integer.class.isAssignableFrom(wrappedType)) {
            return Integer::parseInt;
        } else if (Long.class.isAssignableFrom(wrappedType)) {
            return Long::parseLong;
        } else if (Float.class.isAssignableFrom(wrappedType)) {
            return Float::parseFloat;
        } else if (Double.class.isAssignableFrom(wrappedType)) {
            return Double::parseDouble;
        } else if (BigDecimal.class.isAssignableFrom(wrappedType)) {
            return BigDecimal::new;
        } else if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
            return ScaledBigDecimal::new;
        } else {
            throw new IllegalArgumentException("Unable to convert text to type: " + wrappedType.getName());
        }
    }
}
