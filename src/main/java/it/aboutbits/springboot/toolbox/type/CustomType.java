package it.aboutbits.springboot.toolbox.type;

import org.jspecify.annotations.NullMarked;

/**
 * Implementations should override `toString()` by forwarding
 * the `toString()` method of the wrapped value.
 * Or return `Objects.toString(value())`
 */
@NullMarked
public interface CustomType<T> {
    T value();
}
