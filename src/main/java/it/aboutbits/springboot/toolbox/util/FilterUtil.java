package it.aboutbits.springboot.toolbox.util;

import lombok.NonNull;
import org.springframework.data.util.Streamable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a collection of tools to simply filter items to a new collection or stream.
 * <p>
 * The returned collections are unmodifiable.
 */
public final class FilterUtil {
    private FilterUtil() {
    }

    public static <T> Set<T> filterToSet(@NonNull Collection<T> items, @NonNull Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T> Set<T> filterToSet(@NonNull Streamable<T> items, @NonNull Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T> Set<T> filterToSet(@NonNull Stream<T> items, @NonNull Predicate<T> filter) {
        return items
                .filter(filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T> List<T> filterToList(@NonNull Collection<T> items, @NonNull Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .toList();
    }

    public static <T> List<T> filterToList(@NonNull Streamable<T> items, @NonNull Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .toList();
    }

    public static <T> List<T> filterToList(@NonNull Stream<T> items, @NonNull Predicate<T> filter) {
        return items
                .filter(filter)
                .toList();
    }

    public static <T> Stream<T> filterToStream(@NonNull Collection<T> items, @NonNull Predicate<T> filter) {
        return items.stream()
                .filter(filter);
    }

    public static <T> Stream<T> filterToStream(@NonNull Streamable<T> items, @NonNull Predicate<T> filter) {
        return items.stream()
                .filter(filter);
    }

    public static <T> Stream<T> filterToStream(@NonNull Stream<T> items, @NonNull Predicate<T> filter) {
        return items
                .filter(filter);
    }
}
