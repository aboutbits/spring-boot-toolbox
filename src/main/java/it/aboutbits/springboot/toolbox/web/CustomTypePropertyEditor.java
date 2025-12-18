package it.aboutbits.springboot.toolbox.web;

import it.aboutbits.springboot.toolbox.jackson.CustomTypeDeserializer;
import it.aboutbits.springboot.toolbox.reflection.util.CustomTypeReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import java.util.function.Function;

@NullMarked
public final class CustomTypePropertyEditor<T extends CustomType<?>> extends PropertyEditorSupport {
    private final Constructor<T> constructor;
    private final Function<@Nullable String, @Nullable Object> typeConverter;

    public CustomTypePropertyEditor(Class<T> customType) {
        try {
            this.constructor = CustomTypeReflectionUtil.getCustomTypeConstructor(customType);
        } catch (NoSuchMethodException e) {
            throw new CustomTypeDeserializer.CustomTypeDeserializerException(
                    "Unable to find constructor for type: " + customType.getName(),
                    e
            );
        }

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

    private static Function<@Nullable String, @Nullable Object> getTextToTypeConverter(Class<?> wrappedType) {
        if (Boolean.class.isAssignableFrom(wrappedType)) {
            return Boolean::parseBoolean;
        }
        if (String.class.isAssignableFrom(wrappedType)) {
            return text -> text;
        }
        if (Character.class.isAssignableFrom(wrappedType)) {
            return text -> {
                if (text == null || text.length() != 1) {
                    throw new IllegalArgumentException("Unable to convert text to type: " + wrappedType.getName());
                }
                return text.charAt(0);
            };
        }
        if (Byte.class.isAssignableFrom(wrappedType)) {
            return s -> s != null ? Byte.parseByte(s) : null;
        }
        if (Short.class.isAssignableFrom(wrappedType)) {
            return s -> s != null ? Short.parseShort(s) : null;
        }
        if (Integer.class.isAssignableFrom(wrappedType)) {
            return s -> s != null ? Integer.parseInt(s) : null;
        }
        if (Long.class.isAssignableFrom(wrappedType)) {
            return s -> s != null ? Long.parseLong(s) : null;
        }
        if (BigInteger.class.isAssignableFrom(wrappedType)) {
            return val -> val != null ? new BigInteger(val) : null;
        }
        if (Float.class.isAssignableFrom(wrappedType)) {
            return s -> s != null ? Float.parseFloat(s) : null;
        }
        if (Double.class.isAssignableFrom(wrappedType)) {
            return s -> s != null ? Double.parseDouble(s) : null;
        }
        if (BigDecimal.class.isAssignableFrom(wrappedType)) {
            return val -> val != null ? new BigDecimal(val) : null;
        }
        if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
            return value -> value != null ? new ScaledBigDecimal(value) : null;
        }
        if (UUID.class.isAssignableFrom(wrappedType)) {
            return name -> name != null ? UUID.fromString(name) : null;
        }
        if (Enum.class.isAssignableFrom(wrappedType)) {
            return toEnumConverter(wrappedType);
        }
        throw new IllegalArgumentException("Unable to convert text to type: " + wrappedType.getName());
    }

    @SuppressWarnings("unchecked")
    private static Function<@Nullable String, @Nullable Object> toEnumConverter(Class<?> wrappedType) {
        var enumClass = (Class<? extends Enum<?>>) wrappedType.asSubclass(Enum.class);

        return text -> {
            if (text == null) {
                return null;
            }
            try {
                return Enum.valueOf((Class<? extends Enum>) enumClass, text);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unable to convert text to enum: " + enumClass.getName(), e);
            }
        };
    }
}
