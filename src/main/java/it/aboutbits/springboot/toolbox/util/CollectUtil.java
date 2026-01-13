package it.aboutbits.springboot.toolbox.util;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.util.Streamable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
@NullMarked
public final class CollectUtil {
    private CollectUtil() {
    }

    public static <T, R> Set<R> collectToSet(Collection<T> items, NullableFunction<? super T, R> mapper) {
        return items.stream()
                .map(mapper::apply)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T, R> Set<R> collectToSet(Streamable<T> items, NullableFunction<? super T, R> mapper) {
        return items.stream()
                .map(mapper::apply)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T, R> Set<R> collectToSet(Stream<T> items, NullableFunction<? super T, R> mapper) {
        return items
                .map(mapper::apply)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    @SuppressWarnings("java:S6204")
    public static <T, R> List<R> collectToList(Collection<T> items, NullableFunction<? super T, R> mapper) {
        //noinspection SimplifyStreamApiCallChains
        return items.stream()
                .map(mapper::apply)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @SuppressWarnings("java:S6204")
    public static <T, R> List<R> collectToList(Streamable<T> items, NullableFunction<? super T, R> mapper) {
        //noinspection SimplifyStreamApiCallChains
        return items.stream()
                .map(mapper::apply)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    @SuppressWarnings("java:S6204")
    public static <T, R> List<R> collectToList(Stream<T> items, NullableFunction<? super T, R> mapper) {
        //noinspection SimplifyStreamApiCallChains
        return items
                .map(mapper::apply)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    public static <T, R> Stream<R> collectToStream(Collection<T> items, NullableFunction<? super T, R> mapper) {
        //noinspection NullableProblems
        return items.stream()
                .map(mapper::apply)
                .filter(Objects::nonNull);
    }

    public static <T, R> Stream<R> collectToStream(Streamable<T> items, NullableFunction<? super T, R> mapper) {
        //noinspection NullableProblems
        return items.stream()
                .map(mapper::apply)
                .filter(Objects::nonNull);
    }

    public static <T, R> Stream<R> collectToStream(Stream<T> items, NullableFunction<? super T, R> mapper) {
        //noinspection NullableProblems
        return items
                .map(mapper::apply)
                .filter(Objects::nonNull);
    }

    public static <T, K, R> Map<K, R> collectToMap(
            Collection<T> items,
            Function<T, K> keyMapper,
            Function<T, R> valueMapper
    ) {
        return items.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T, K, R> Map<K, R> collectToMap(
            Streamable<T> items,
            Function<T, K> keyMapper,
            Function<T, R> valueMapper
    ) {
        return items.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T, K, R> Map<K, R> collectToMap(
            Stream<T> items,
            Function<T, K> keyMapper,
            Function<T, R> valueMapper
    ) {
        return items
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
