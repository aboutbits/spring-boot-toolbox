package it.aboutbits.springboot.toolbox.persistence;

import it.aboutbits.springboot.toolbox.parameter.SortParameter;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

/// A mapping of sort parameter definitions to their corresponding database columns.
///
/// The values in this map are of type [Object] and are expected to be either:
/// - [String] - for column names when using Hibernate/JPA
/// - `org.jooq.Field<?>` - for jOOQ field objects when using jOOQ
///
/// @param <T> the enum type that implements [SortParameter.Definition]
@NullMarked
public class SortMappings<T extends Enum<?> & SortParameter.Definition> extends HashMap<T, Object> {
    SortMappings() {
        super();
    }

    public static <T extends Enum<?> & SortParameter.Definition> Mapping<T> map(
            T property,
            String column
    ) {
        return new Mapping<>(property, column);
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

    public record Mapping<T extends Enum<?> & SortParameter.Definition>(T property, Object column) {
    }
}
