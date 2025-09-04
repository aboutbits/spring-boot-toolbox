package it.aboutbits.springboot.toolbox.util;

import lombok.NonNull;
import org.springframework.data.util.Streamable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is a collection of tools to simply collect a property to a new collection or stream.
 * The common usage would, for example, be to collect the ids of all items in a list to a set.
 * <p>
 * The returned collections are unmodifiable.
 */
public final class CollectUtil {
    private CollectUtil() {
    }

    public static <T, R> Set<R> collectToSet(@NonNull Collection<T> items, @NonNull Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T, R> Set<R> collectToSet(@NonNull Streamable<T> items, @NonNull Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T, R> Set<R> collectToSet(@NonNull Stream<T> items, @NonNull Function<T, R> mapper) {
        return items
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T, R> List<R> collectToList(@NonNull Collection<T> items, @NonNull Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .toList();
    }

    public static <T, R> List<R> collectToList(@NonNull Streamable<T> items, @NonNull Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .toList();
    }

    public static <T, R> List<R> collectToList(@NonNull Stream<T> items, @NonNull Function<T, R> mapper) {
        return items
                .map(mapper)
                .filter(Objects::nonNull)
                .toList();
    }

    public static <T, R> Stream<R> collectToStream(@NonNull Collection<T> items, @NonNull Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .filter(Objects::nonNull);
    }

    public static <T, R> Stream<R> collectToStream(@NonNull Streamable<T> items, @NonNull Function<T, R> mapper) {
        return items.stream()
                .map(mapper)
                .filter(Objects::nonNull);
    }

    public static <T, R> Stream<R> collectToStream(@NonNull Stream<T> items, @NonNull Function<T, R> mapper) {
        return items
                .map(mapper)
                .filter(Objects::nonNull);
    }
}
