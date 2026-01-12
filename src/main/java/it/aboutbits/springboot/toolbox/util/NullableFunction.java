package it.aboutbits.springboot.toolbox.util;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@FunctionalInterface
public interface NullableFunction<T, R> {
    @Nullable R apply(T t);
}
