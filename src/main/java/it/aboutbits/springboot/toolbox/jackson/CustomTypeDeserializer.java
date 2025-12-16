package it.aboutbits.springboot.toolbox.jackson;

import it.aboutbits.springboot.toolbox.reflection.util.CustomTypeReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.TokenStreamLocation;
import tools.jackson.core.exc.InputCoercionException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.io.Closeable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import java.util.function.Function;

public class CustomTypeDeserializer<T extends CustomType<?>> extends ValueDeserializer<T> {
    private final Class<T> customType;
    private final Constructor<T> constructor;
    private final Function<JsonParser, Object> typeConverter;

    public CustomTypeDeserializer(Class<T> customType) {
        this.customType = customType;

        try {
            this.constructor = CustomTypeReflectionUtil.getCustomTypeConstructor(customType);
        } catch (NoSuchMethodException e) {
            throw new CustomTypeDeserializerException(
                    "Unable to find constructor for type: " + customType.getName(),
                    e
            );
        }

        this.typeConverter = getTypeConverter(
                constructor.getParameterTypes()[0]
        );
    }

    @Override
    public Class<T> handledType() {
        return customType;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        var value = typeConverter.apply(jsonParser);

        try {
            return constructor.newInstance(value);
        } catch (
                IllegalAccessException
                | InvocationTargetException
                | InstantiationException e) {
            throw new Ex(e);
        }
    }

    public static class Ex extends JacksonException {

        protected Ex(String msg) {
            super(msg);
        }

        protected Ex(Throwable rootCause) {
            super(rootCause);
        }

        protected Ex(String msg, Throwable rootCause) {
            super(msg, rootCause);
        }

        protected Ex(String msg, TokenStreamLocation loc, Throwable rootCause) {
            super(msg, loc, rootCause);
        }

        protected Ex(Closeable processor, Throwable rootCause) {
            super(processor, rootCause);
        }

        protected Ex(Closeable processor, String msg, TokenStreamLocation loc, Throwable rootCause) {
            super(processor, msg, loc, rootCause);
        }

        protected Ex(Closeable processor, String msg) {
            super(processor, msg);
        }

        protected Ex(Closeable processor, String msg, Throwable problem) {
            super(processor, msg, problem);
        }

        protected Ex(Closeable processor, String msg, TokenStreamLocation loc) {
            super(processor, msg, loc);
        }
    }

    private static Function<JsonParser, Object> getTypeConverter(Class<?> wrappedType) {
        if (Byte.class.isAssignableFrom(wrappedType)) {
            return getByteConverter();
        }
        if (Boolean.class.isAssignableFrom(wrappedType)) {
            return getBooleanConverter();
        }
        if (String.class.isAssignableFrom(wrappedType)) {
            return getStringConverter();
        }
        if (Character.class.isAssignableFrom(wrappedType)) {
            return getCharConverter();
        }
        if (Short.class.isAssignableFrom(wrappedType)) {
            return getShortConverter();
        }
        if (Integer.class.isAssignableFrom(wrappedType)) {
            return getIntegerConverter();
        }
        if (Long.class.isAssignableFrom(wrappedType)) {
            return getLongConverter();
        }
        if (BigInteger.class.isAssignableFrom(wrappedType)) {
            return getBigIntegerConverter();
        }
        if (Float.class.isAssignableFrom(wrappedType)) {
            return getFloatConverter();
        }
        if (Double.class.isAssignableFrom(wrappedType)) {
            return getDoubleConverter();
        }
        if (BigDecimal.class.isAssignableFrom(wrappedType)) {
            return getBigDecimalConverter();
        }
        if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
            return getScaledBigDecimalConverter();
        }
        if (UUID.class.isAssignableFrom(wrappedType)) {
            return getUUIDConverter();
        }
        if (Enum.class.isAssignableFrom(wrappedType) || wrappedType.isEnum()) {
            return getEnumConverter(wrappedType);
        }
        throw new CustomTypeDeserializerException("Value type not supported: " + wrappedType.getName());
    }

    private static Function<JsonParser, Object> getByteConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getByteValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Byte.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getBooleanConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getBooleanValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Boolean.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getScaledBigDecimalConverter() {
        return jsonParser -> {
            try {
                return new ScaledBigDecimal(jsonParser.getDecimalValue());
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as ScaledBigDecimal.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getBigDecimalConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getDecimalValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as BigDecimal.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getDoubleConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getDoubleValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Double.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getFloatConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getFloatValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Float.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getBigIntegerConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getBigIntegerValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as BigInteger.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getLongConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getLongValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Long.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getIntegerConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getIntValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Integer.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getShortConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getShortValue();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Short.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getStringConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getValueAsString();
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as String.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getCharConverter() {
        return jsonParser -> {
            try {
                var value = jsonParser.getValueAsString();
                if (value == null || value.length() != 1) {
                    throw new InputCoercionException(
                            jsonParser,
                            "Not a Char.",
                            jsonParser.currentToken(),
                            String.class
                    );
                }
                return value.charAt(0);
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Char.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getUUIDConverter() {
        return jsonParser -> {
            try {
                var value = jsonParser.getValueAsString();
                if (value == null || value.length() != 36) {
                    throw new InputCoercionException(
                            jsonParser,
                            "Not a UUID.",
                            jsonParser.currentToken(),
                            String.class
                    );
                }
                return UUID.fromString(value);
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as UUID.", e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static Function<JsonParser, Object> getEnumConverter(Class<?> wrappedType) {
        var enumClass = (Class<? extends Enum<?>>) wrappedType.asSubclass(Enum.class);
        return jsonParser -> {
            try {
                String value = jsonParser.getValueAsString();
                if (value == null) {
                    return null;
                }
                return Enum.valueOf((Class<? extends Enum>) enumClass, value);
            } catch (IllegalArgumentException e) {
                throw new CustomTypeDeserializerException(
                        "Failed to read value as Enum: " + enumClass.getName(),
                        e
                );
            } catch (InputCoercionException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Enum.", e);
            }
        };
    }

    public static final class CustomTypeDeserializerException extends RuntimeException {
        public CustomTypeDeserializerException(String message) {
            super(message);
        }

        public CustomTypeDeserializerException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
