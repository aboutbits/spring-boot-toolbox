package it.aboutbits.springboot.toolbox.jackson;

import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@NullMarked
public class CustomTypeSerializer extends ValueSerializer<CustomType<?>> {
    @SuppressWarnings("unchecked")
    @Override
    public Class<CustomType<?>> handledType() {
        return (Class<CustomType<?>>) (Class<?>) CustomType.class;
    }

    @Override
    public void serialize(
            CustomType<?> customType,
            JsonGenerator jsonGenerator,
            SerializationContext ctx
    ) {
        var value = customType.value();

        switch (value) {
            case String stringValue -> jsonGenerator.writeString(stringValue);
            case UUID uuidValue -> jsonGenerator.writeString(uuidValue.toString());
            case Enum<?> enumValue -> jsonGenerator.writeString(enumValue.name());
            case Byte numericValue -> jsonGenerator.writeNumber(numericValue);
            case Short numericValue -> jsonGenerator.writeNumber(numericValue);
            case Integer numericValue -> jsonGenerator.writeNumber(numericValue);
            case Long numericValue -> jsonGenerator.writeNumber(numericValue);
            case Float numericValue -> jsonGenerator.writeNumber(numericValue);
            case Double numericValue -> jsonGenerator.writeNumber(numericValue);
            case BigInteger numericValue -> jsonGenerator.writeNumber(numericValue);
            case BigDecimal numericValue -> jsonGenerator.writeNumber(numericValue);
            case ScaledBigDecimal numericValue -> jsonGenerator.writeNumber(numericValue.doubleValue());
            default -> jsonGenerator.writeRawValue(String.valueOf(value));
        }
    }
}
