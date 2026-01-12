package it.aboutbits.springboot.toolbox.jackson;

import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import org.jspecify.annotations.NullMarked;
import tools.jackson.core.JsonParser;
import tools.jackson.core.exc.InputCoercionException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

@NullMarked
public class EntityIdDeserializer extends ValueDeserializer<EntityId<?>> {
    /*
    This is needed because EntityId is just the interface. Jackson does not know what implementation to take.
    So we use this generic implementation.
     */
    @Override
    public EntityId<?> deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext
    ) {
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
        } catch (InputCoercionException _) {
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
