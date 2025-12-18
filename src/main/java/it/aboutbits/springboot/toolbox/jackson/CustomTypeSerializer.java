package it.aboutbits.springboot.toolbox.jackson;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

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
            default -> jsonGenerator.writeRawValue(String.valueOf(value));
        }
    }
}
