package it.aboutbits.springboot.toolbox.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.aboutbits.springboot.toolbox.type.CustomType;

import java.io.IOException;

public class CustomTypeSerializer extends JsonSerializer<CustomType<?>> {
    @SuppressWarnings("unchecked")
    @Override
    public Class<CustomType<?>> handledType() {
        return (Class<CustomType<?>>) (Class<?>) CustomType.class;
    }

    @Override
    public void serialize(
            CustomType<?> customType,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider
    ) throws IOException {
        var value = customType.value();

        if (value instanceof String stringValue) {
            jsonGenerator.writeString(
                    stringValue
            );
            return;
        }

        jsonGenerator.writeRawValue(
                String.valueOf(
                        value
                )
        );
    }
}
