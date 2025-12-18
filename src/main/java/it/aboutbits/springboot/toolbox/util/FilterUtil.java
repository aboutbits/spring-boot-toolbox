package it.aboutbits.springboot.toolbox.util;

import org.jspecify.annotations.NullMarked;
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
@NullMarked
public final class FilterUtil {
    private FilterUtil() {
    }

    public static <T> Set<T> filterToSet(Collection<T> items, Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T> Set<T> filterToSet(Streamable<T> items, Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T> Set<T> filterToSet(Stream<T> items, Predicate<T> filter) {
        return items
                .filter(filter)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static <T> List<T> filterToList(Collection<T> items, Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .toList();
    }

    public static <T> List<T> filterToList(Streamable<T> items, Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .toList();
    }

    public static <T> List<T> filterToList(Stream<T> items, Predicate<T> filter) {
        return items
                .filter(filter)
                .toList();
    }

    public static <T> Stream<T> filterToStream(Collection<T> items, Predicate<T> filter) {
        return items.stream()
                .filter(filter);
    }

    public static <T> Stream<T> filterToStream(Streamable<T> items, Predicate<T> filter) {
        return items.stream()
                .filter(filter);
    }

    public static <T> Stream<T> filterToStream(Stream<T> items, Predicate<T> filter) {
        return items
                .filter(filter);
    }
}
