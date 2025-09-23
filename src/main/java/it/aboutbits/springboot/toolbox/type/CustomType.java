package it.aboutbits.springboot.toolbox.type;

/**
 * Implementations should override `toString()` by forwarding
 * the `toString()` method of the wrapped value.
 * Or return `Objects.toString(value())`
 */
public interface CustomType<T> {
    T value();
}
