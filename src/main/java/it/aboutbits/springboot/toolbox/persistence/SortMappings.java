package it.aboutbits.springboot.toolbox.persistence;

import it.aboutbits.springboot.toolbox.parameter.SortParameter;
import lombok.NonNull;

import java.util.HashMap;

public final class SortMappings<T extends Enum<?> & SortParameter.Definition> extends HashMap<T, String> {
    private SortMappings() {
        super();
    }

    public static <T extends Enum<?> & SortParameter.Definition> Mapping<T> map(
            @NonNull T property,
            @NonNull String column
    ) {
        return new Mapping<>(property, column);
    }

    @SafeVarargs
    public static <T extends Enum<?> & SortParameter.Definition> SortMappings<T> of(
            @NonNull Mapping<T>... mappings
    ) {
        var sortMappings = new SortMappings<T>();

        for (var mapping : mappings) {
            sortMappings.put(mapping.property(), mapping.column());
        }

        return sortMappings;
    }

    public record Mapping<T extends Enum<?> & SortParameter.Definition>(@NonNull T property, @NonNull String column) {
    }
}
