package it.aboutbits.springboot.toolbox.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Converter
@NullMarked
public class UUIDConverter implements AttributeConverter<UUID, String> {
    @Override
    @Nullable
    public String convertToDatabaseColumn(@Nullable UUID attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.toString();
    }

    @Override
    @Nullable
    public UUID convertToEntityAttribute(@Nullable String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        return UUID.fromString(dbData);
    }
}
