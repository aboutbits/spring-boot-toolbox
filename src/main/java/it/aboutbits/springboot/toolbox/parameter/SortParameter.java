package it.aboutbits.springboot.toolbox.parameter;

import lombok.NonNull;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record SortParameter<T extends Enum<?> & SortParameter.Definition>(List<SortField> sortFields) {
    private static final String DEFAULT_SORT_PROPERTY = "id";
    private static final Sort DEFAULT_SORT = Sort.by(
            Sort.Direction.ASC,
            DEFAULT_SORT_PROPERTY
    );

    public static <T extends Enum<?> & Definition> SortParameter<T> unsorted() {
        return new SortParameter<>(Collections.emptyList());
    }

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

    public SortParameter<T> or(@NonNull SortParameter<T> fallback) {
        return sortFields == null || sortFields.isEmpty() ? fallback : this;
    }

    public Sort buildSortWithoutDefault(@NonNull Map<T, String> mapping) {
        var stringMapping = mapping.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));

        return buildSort(stringMapping, false);
    }

    public Sort buildSort(@NonNull Map<T, String> mapping) {
        var stringMapping = mapping.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue
                ));

        return buildSort(stringMapping, true);
    }

    // SonarLint: Replace this usage of 'Stream.collect(Collectors. toList())' with 'Stream.toList()' and ensure that the list is unmodified.
    @SuppressWarnings("java:S6204")
    private Sort buildSort(@NonNull Map<String, String> mapping, boolean withDefault) {
        if (sortFields == null || sortFields.isEmpty()) {
            return withDefault ? DEFAULT_SORT : Sort.unsorted();
        }

        var additionalSort = Sort.by(
                sortFields.stream()
                        .filter(sortField -> mapping.containsKey(sortField.property()))
                        .map(sortField -> new Sort.Order(
                                sortField.direction(),
                                mapping.get(sortField.property()),
                                sortField.nullHandling()
                        ))
                        // We do not use .toList() here as we potentially want to modify the sort list later in the StoreImpl
                        .collect(Collectors.toList())
        );

        if (withDefault) {
            var includesDefault = additionalSort.getOrderFor(DEFAULT_SORT_PROPERTY) != null;
            if (!includesDefault) {
                return additionalSort.and(DEFAULT_SORT);
            }
        }
        return additionalSort;
    }

    public record SortField(
            @NonNull String property,
            @NonNull Sort.Direction direction,
            @NonNull Sort.NullHandling nullHandling
    ) {
    }

    public interface Definition {
    }
}
