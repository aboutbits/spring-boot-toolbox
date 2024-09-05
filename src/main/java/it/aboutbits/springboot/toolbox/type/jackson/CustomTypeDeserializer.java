package it.aboutbits.springboot.toolbox.type.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.function.Function;

public class CustomTypeDeserializer<T extends CustomType<?>> extends JsonDeserializer<T> {
    private final Class<T> customType;
    private final Constructor<T> constructor;
    private final Function<JsonParser, Object> typeConverter;

    public CustomTypeDeserializer(Class<T> customType) {
        this.customType = customType;

        this.constructor = RecordReflectionUtil.getCanonicalConstructor(customType);

        this.typeConverter = getTypeConverter(
                constructor.getParameterTypes()[0]
        );
    }

    @Override
    public Class<T> handledType() {
        return customType;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var value = typeConverter.apply(jsonParser);

        try {
            return constructor.newInstance(value);
        } catch (
                IllegalAccessException
                | InvocationTargetException
                | InstantiationException e) {
            throw new IOException(e);
        }
    }

    private static Function<JsonParser, Object> getTypeConverter(Class<?> wrappedType) {
        if (String.class.isAssignableFrom(wrappedType)) {
            return getStringConverter();
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
        throw new CustomTypeDeserializerException("Value type not supported: " + wrappedType.getName());
    }

    private static Function<JsonParser, Object> getScaledBigDecimalConverter() {
        return jsonParser -> {
            try {
                return new ScaledBigDecimal(jsonParser.getDecimalValue());
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as ScaledBigDecimal.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getBigDecimalConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getDecimalValue();
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as BigDecimal.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getDoubleConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getDoubleValue();
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Double.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getFloatConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getFloatValue();
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Float.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getLongConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getLongValue();
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Long.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getIntegerConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getIntValue();
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Integer.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getShortConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getShortValue();
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as Short.", e);
            }
        };
    }

    private static Function<JsonParser, Object> getStringConverter() {
        return jsonParser -> {
            try {
                return jsonParser.getValueAsString();
            } catch (IOException e) {
                throw new CustomTypeDeserializerException("Failed to read value as String.", e);
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
