package it.aboutbits.springboot.toolbox.web;

import it.aboutbits.springboot.toolbox.jackson.CustomTypeDeserializer;
import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

public final class CustomTypePropertyEditor<T extends CustomType<?>> extends PropertyEditorSupport {
    private final Constructor<T> constructor;
    private final Function<String, Object> typeConverter;

    public CustomTypePropertyEditor(@NonNull Class<T> customType) {
        Constructor<T> c;
        if (customType.isRecord()) {
            c = RecordReflectionUtil.getCanonicalConstructor(customType);
        } else {
            try {
                var customTypeInterface = Arrays.stream(customType.getGenericInterfaces())
                        .filter(i ->
                                        i instanceof ParameterizedType
                                                && CustomType.class.isAssignableFrom((Class<?>) ((ParameterizedType) i).getRawType())
                        ).findFirst()
                        .map(i -> (ParameterizedType) i)
                        .orElseThrow();

                var parameterType = (Class<?>) customTypeInterface.getActualTypeArguments()[0];

                c = customType.getConstructor(parameterType);
            } catch (Exception e) {
                throw new CustomTypeDeserializer.CustomTypeDeserializerException(
                        "Unable to find constructor for type: " + customType.getName(),
                        e
                );
            }
        }
        this.constructor = c;
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
        }
        if (Short.class.isAssignableFrom(wrappedType)) {
            return Short::parseShort;
        }
        if (Integer.class.isAssignableFrom(wrappedType)) {
            return Integer::parseInt;
        }
        if (Long.class.isAssignableFrom(wrappedType)) {
            return Long::parseLong;
        }
        if (BigInteger.class.isAssignableFrom(wrappedType)) {
            return BigInteger::new;
        }
        if (Float.class.isAssignableFrom(wrappedType)) {
            return Float::parseFloat;
        }
        if (Double.class.isAssignableFrom(wrappedType)) {
            return Double::parseDouble;
        }
        if (BigDecimal.class.isAssignableFrom(wrappedType)) {
            return BigDecimal::new;
        }
        if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
            return ScaledBigDecimal::new;
        }
        throw new IllegalArgumentException("Unable to convert text to type: " + wrappedType.getName());
    }
}
