package it.aboutbits.springboot.toolbox.util;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class NullUtil {
    private NullUtil() {
    }

    public static <T> T nonNullOrFail(@Nullable T x) {
        if (x == null) {
            throw new IllegalStateException("Required non-null value, but got null.");
        }
        return x;
    }

    public static <T> T nonNullOrFail(@Nullable T x, String failureMessage) {
        if (x == null) {
            throw new IllegalStateException(failureMessage);
        }
        return x;
    }
}
