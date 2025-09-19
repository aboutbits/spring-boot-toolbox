package it.aboutbits.springboot.toolbox.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;

import java.io.IOException;

public class EntityIdDeserializer extends JsonDeserializer<EntityId<?>> {
    /*
    This is needed because EntityId is just the interface. Jackson does not know what implementation to take.
    So we use this generic implementation.
     */
    @Override
    public EntityId<?> deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext
    ) throws IOException {
        // We need to do this because due to type erasure we don't actually know the wrapped type. So we try a number first and fallback to a string.
        try {
            var theValue = jsonParser.getLongValue();
            return new EntityId<Long>() {
                @Override
                public Long value() {
                    return theValue;
                }

                @Override
                public String toString() {
                    return String.valueOf(value());
                }
            };
        } catch (JsonParseException e) {
            var theValue = jsonParser.getValueAsString();
            return new EntityId<String>() {
                @Override
                public String value() {
                    return theValue;
                }

                @Override
                public String toString() {
                    return theValue;
                }
            };
        }
    }
}
