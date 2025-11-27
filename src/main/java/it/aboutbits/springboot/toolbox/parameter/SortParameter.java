package it.aboutbits.springboot.toolbox.parameter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents sorting parameters for data retrieval and manipulation.
 * This class provides methods for creating, customizing, and applying sorting criteria
 * based on enum constants and associated sort properties. Sorting criteria can be defined
 * with various configurations, including direction and null-handling behavior.
 */
@EqualsAndHashCode
public final class SortParameter<T extends Enum<?> & SortParameter.Definition> {
    private static final String DEFAULT_SORT_PROPERTY = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRETION = Sort.Direction.ASC;

    @Accessors(fluent = true)
    @Getter
    private final List<SortField> sortFields = new ArrayList<>();

    public SortParameter(List<SortField> sortFields) {
        if (sortFields == null) {
            return;
        }
        this.sortFields.addAll(sortFields);
    }

    private SortParameter() {
    }

    /**
     * Creates a {@link SortParameter} that represents an unsorted state.
     *
     * @param <T> a type that extends both {@link Enum} and {@link Definition}.
     * @return an instance of {@link SortParameter} configured with no sorting fields.
     */
    public static <T extends Enum<?> & Definition> SortParameter<T> unsorted() {
        return new SortParameter<>();
    }

    /**
     * Creates a {@link SortParameter} initialized with the provided sort definitions.
     * Each provided enum constant is converted into a {@link SortField} with ascending
     * order direction and default null-handling behavior. This method allows the
     * specification of multiple sorting criteria.
     *
     * @param <T>             a type parameter representing an enum that implements the {@link Definition} interface.
     * @param sortDefinitions an array of enum constants defining the sort properties. Must not be null.
     * @return a {@link SortParameter} instance configured with the given sort definitions.
     */
    @SafeVarargs
    public static <T extends Enum<?> & Definition> SortParameter<T> by(
            @NonNull T... sortDefinitions
    ) {
        return new SortParameter<>(
                Stream.of(sortDefinitions)
                        .map(sortDefinition -> new SortField(
                                     sortDefinition.name(),
                                     Sort.Direction.ASC,
                                     Sort.NullHandling.NATIVE
                             )
                        )
                        .toList()
        );
    }

    /**
     * Creates a {@link SortParameter} initialized with a single sort definition.
     * This method allows specifying the property to sort by, the direction of sorting,
     * and uses the default null-handling behavior ({@link Sort.NullHandling#NATIVE}).
     *
     * @param <T>            a type that extends both {@link Enum} and {@link Definition}.
     * @param sortDefinition an enum constant defining the property to sort by. Must not be null.
     * @param direction      the direction of sorting, either {@link Sort.Direction#ASC} or {@link Sort.Direction#DESC}.
     *                       Must not be null.
     * @return an instance of {@link SortParameter} configured with the given sort definition and direction.
     */
    public static <T extends Enum<?> & Definition> SortParameter<T> by(
            @NonNull T sortDefinition,
            @NonNull Sort.Direction direction
    ) {
        return new SortParameter<>(
                List.of(new SortField(
                                sortDefinition.name(),
                                direction,
                                Sort.NullHandling.NATIVE
                        )
                )
        );
    }

    /**
     * Creates a {@link SortParameter} instance configured with a single sort definition.
     * This method allows specifying the property to sort by, the direction of sorting,
     * and null-handling behavior.
     *
     * @param <T>            the type parameter extending both {@link Enum} and {@link Definition}.
     * @param sortDefinition an enum constant defining the property to sort by. Must not be null.
     * @param direction      the direction of sorting, either {@link Sort.Direction#ASC} or {@link Sort.Direction#DESC}. Must not be null.
     * @param nullHandling   the strategy for handling null values during sorting, specified by {@link Sort.NullHandling}. Must not be null.
     * @return an instance of {@link SortParameter} configured with the given sort definition, direction, and null-handling behavior.
     */
    public static <T extends Enum<?> & Definition> SortParameter<T> by(
            @NonNull T sortDefinition,
            @NonNull Sort.Direction direction,
            @NonNull Sort.NullHandling nullHandling
    ) {
        return new SortParameter<>(
                List.of(new SortField(
                                sortDefinition.name(),
                                direction,
                                nullHandling
                        )
                )
        );
    }

    /**
     * Adds additional sorting criteria to the existing {@link SortParameter}.
     * Each provided enum constant is converted into a {@link SortField} with ascending
     * order direction and default null-handling behavior.
     *
     * @param sortDefinitions an array of enum constants defining the additional sort properties. Must not be null.
     * @return the updated {@link SortParameter} instance containing the new sort definitions.
     */
    @SafeVarargs
    public final SortParameter<T> and(
            @NonNull T... sortDefinitions
    ) {
        sortFields.addAll(
                Stream.of(sortDefinitions)
                        .map(sortDefinition -> new SortField(
                                     sortDefinition.name(),
                                     Sort.Direction.ASC,
                                     Sort.NullHandling.NATIVE
                             )
                        )
                        .toList()
        );

        return this;
    }

    /**
     * Adds a sorting criterion to the current {@link SortParameter} instance.
     * The provided sort definition and direction are converted into a {@link SortField}
     * with default null-handling behavior and appended to the existing sort fields.
     *
     * @param sortDefinition the enum constant defining the property to sort by. Must not be null.
     * @param direction      the direction of sorting, either {@link Sort.Direction#ASC} or {@link Sort.Direction#DESC}. Must not be null.
     * @return the updated {@link SortParameter} instance containing the new sorting criterion.
     */
    public SortParameter<T> and(
            @NonNull T sortDefinition,
            @NonNull Sort.Direction direction
    ) {
        sortFields.add(
                new SortField(
                        sortDefinition.name(),
                        direction,
                        Sort.NullHandling.NATIVE
                )
        );

        return this;
    }

    /**
     * Adds a sorting criterion to the current {@link SortParameter} instance. The provided sort definition,
     * direction, and null-handling behavior are converted into a {@link SortField} and appended to the
     * existing sort fields.
     *
     * @param sortDefinition the enum constant defining the property to sort by. Must not be null.
     * @param direction      the direction of sorting, either {@link Sort.Direction#ASC} or {@link Sort.Direction#DESC}. Must not be null.
     * @param nullHandling   the strategy for handling null values during sorting, specified by {@link Sort.NullHandling}. Must not be null.
     * @return the updated {@link SortParameter} instance containing the new sorting criterion.
     */
    public SortParameter<T> and(
            @NonNull T sortDefinition,
            @NonNull Sort.Direction direction,
            @NonNull Sort.NullHandling nullHandling
    ) {
        sortFields.add(
                new SortField(
                        sortDefinition.name(),
                        direction,
                        nullHandling
                )
        );

        return this;
    }

    /**
     * Returns the current {@link SortParameter} instance if it has defined sorting fields.
     * Otherwise, returns the provided fallback {@link SortParameter}.
     *
     * @param fallback the {@link SortParameter} to use as a fallback in case the current instance
     *                 has no defined sorting fields. Must not be null.
     * @return the current {@link SortParameter} if it has defined sorting fields,
     * or the provided fallback if it does not.
     */
    public SortParameter<T> or(@NonNull SortParameter<T> fallback) {
        return sortFields.isEmpty() ? fallback : this;
    }

    /**
     * Builds a {@link Sort} object without applying any default sorting parameters. Converts the enum keys
     * of the provided map to their string names and generates the sort object.
     *
     * @param mapping a non-null map where the keys are enumeration values representing sort properties
     *                and the values are their associated sort directions. The enumeration keys must
     *                have a `name()` method for string conversion. Must not be null.
     * @return an instance of {@link Sort} created using the transformed key-value mapping,
     * excluding default sorting behavior.
     */
    public Sort buildSortWithoutDefault(@NonNull Map<T, String> mapping) {
        var stringMapping = mapping.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));

        return buildSort(stringMapping, false);
    }

    /**
     * Builds a {@link Sort} object based on the provided mapping of enumeration values to string properties.
     * Converts enum keys to their respective string names and generates the sort object.
     * <p>
     * If a sort mapping for "id" is provided, it will be used as the default sort property. Unless specified, this sort will be applied last.
     *
     * @param mapping a non-null map where the keys represent enumeration values and the values represent sort property names.
     *                The enumeration keys must implement the `name()` method to retrieve their string representation.
     * @return an instance of {@link Sort} created using the transformed key-value mapping with default sorting behavior.
     */
    public Sort buildSort(@NonNull Map<T, String> mapping) {
        var stringMapping = mapping.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));

        return buildSort(stringMapping, true);
    }

    private Sort buildSort(@NonNull Map<String, String> mapping, boolean withDefault) {
        if (sortFields.isEmpty()) {
            return withDefault ? getMappedDefaultSort(mapping) : Sort.unsorted();
        }

        var additionalSort = Sort.by(
                new ArrayList<>(
                        sortFields.stream()
                                .filter(sortField -> mapping.containsKey(sortField.property()))
                                .map(sortField -> new Sort.Order(
                                        sortField.direction(),
                                        mapping.get(sortField.property()),
                                        sortField.nullHandling()
                                ))
                                .toList()
                )
        );

        if (withDefault) {
            var includesDefault = additionalSort.getOrderFor(DEFAULT_SORT_PROPERTY) != null;
            if (!includesDefault) {
                return additionalSort.and(getMappedDefaultSort(mapping));
            }
        }
        return additionalSort;
    }

    private static Sort getMappedDefaultSort(Map<String, String> mapping) {
        return Sort.by(DEFAULT_SORT_DIRETION, mapping.getOrDefault(DEFAULT_SORT_PROPERTY, DEFAULT_SORT_PROPERTY));
    }

    public record SortField(
            @NonNull String property,
            @NonNull Sort.Direction direction,
            @NonNull Sort.NullHandling nullHandling
    ) {
    }

    /**
     * Interface to give enums the purpose of listing sortable keys.
     */
    public interface Definition {
    }
}
