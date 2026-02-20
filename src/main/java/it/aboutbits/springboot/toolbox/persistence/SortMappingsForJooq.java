package it.aboutbits.springboot.toolbox.persistence;

import it.aboutbits.springboot.toolbox.parameter.SortParameter;
import org.jooq.Field;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SortMappingsForJooq<T extends Enum<?> & SortParameter.Definition> extends SortMappings<T> {
    private SortMappingsForJooq() {
        super();
    }

    public static <T extends Enum<?> & SortParameter.Definition> Mapping<T> map(
            T property,
            Field<?> column
    ) {
        return new Mapping<>(property, column);
    }

    @SafeVarargs
    public static <T extends Enum<?> & SortParameter.Definition> SortMappingsForJooq<T> of(
            Mapping<T>... mappings
    ) {
        var sortMappings = new SortMappingsForJooq<T>();

        for (var mapping : mappings) {
            sortMappings.put(mapping.property(), mapping.column());
        }

        return sortMappings;
    }
}
