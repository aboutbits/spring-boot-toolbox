package it.aboutbits.springboot.toolbox.persistence;

import it.aboutbits.springboot.toolbox.parameter.SortParameter;
import org.jooq.Field;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public final class SortMappings<T extends Enum<?> & SortParameter.Definition> extends HashMap<T, String> {
    private SortMappings() {
        super();
    }

    public static <T extends Enum<?> & SortParameter.Definition> Mapping<T> map(
            T property,
            String column
    ) {
        return new Mapping<>(property, column);
    }

    public static <T extends Enum<?> & SortParameter.Definition> Mapping<T> map(
            T property,
            Field<?> column
    ) {
        return new Mapping<>(property, column.getName());
    }

    @SafeVarargs
    public static <T extends Enum<?> & SortParameter.Definition> SortMappings<T> of(
            Mapping<T>... mappings
    ) {
        var sortMappings = new SortMappings<T>();

        for (var mapping : mappings) {
            sortMappings.put(mapping.property(), mapping.column());
        }

        return sortMappings;
    }

    public record Mapping<T extends Enum<?> & SortParameter.Definition>(T property, String column) {
    }
}
