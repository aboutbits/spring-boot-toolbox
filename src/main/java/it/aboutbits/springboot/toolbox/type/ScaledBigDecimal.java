package it.aboutbits.springboot.toolbox.type;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * A record that represents a scaled BigDecimal with a fixed MathContext.
 * This class provides various constructors for creating instances with different numerical types.
 * It also provides a set of arithmetic operations that return new instances with the appropriate scale and rounding.
 */
public record ScaledBigDecimal(
        @NonNull BigDecimal value
) implements Comparable<ScaledBigDecimal> {
    private static final MathContext MATH_CONTEXT = new MathContext(15, RoundingMode.HALF_UP);

    public static final ScaledBigDecimal ZERO = new ScaledBigDecimal(0);
    public static final ScaledBigDecimal ONE = new ScaledBigDecimal(1);
    public static final ScaledBigDecimal TWO = new ScaledBigDecimal(2);
    public static final ScaledBigDecimal TEN = new ScaledBigDecimal(10);

    public ScaledBigDecimal(@NonNull BigDecimal value) {
        this.value = value.setScale(MATH_CONTEXT.getPrecision(), MATH_CONTEXT.getRoundingMode());
    }

    public ScaledBigDecimal(int value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(@NonNull Integer value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(@NonNull BigInteger value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(long value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(@NonNull Long value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(float value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(@NonNull Float value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(double value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(@NonNull Double value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(@NonNull String value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public static ScaledBigDecimal valueOf(int value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(@NonNull Integer value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(@NonNull BigInteger value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(long value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(@NonNull Long value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(float value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(@NonNull Float value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(double value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(@NonNull Double value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(@NonNull String value) {
        return new ScaledBigDecimal(value);
    }

    @NotNull
    public ScaledBigDecimal add(@NonNull ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().add(other.value()));
    }

    @NotNull
    public ScaledBigDecimal subtract(@NonNull ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().subtract(other.value()));
    }

    @NotNull
    public ScaledBigDecimal multiply(@NonNull ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().multiply(other.value()));
    }

    @NotNull
    public ScaledBigDecimal divide(@NonNull ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().divide(other.value(), MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal add(@NonNull BigDecimal other) {
        return new ScaledBigDecimal(this.value().add(other));
    }

    @NotNull
    public ScaledBigDecimal subtract(@NonNull BigDecimal other) {
        return new ScaledBigDecimal(this.value().subtract(other));
    }

    @NotNull
    public ScaledBigDecimal multiply(@NonNull BigDecimal other) {
        return new ScaledBigDecimal(this.value().multiply(other));
    }

    @NotNull
    public ScaledBigDecimal divide(@NonNull BigDecimal other) {
        return new ScaledBigDecimal(this.value().divide(other, MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal remainder(@NonNull ScaledBigDecimal divisor) {
        return new ScaledBigDecimal(this.value().remainder(divisor.value(), MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal remainder(@NonNull BigDecimal divisor) {
        return new ScaledBigDecimal(this.value().remainder(divisor, MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal sqrt() {
        return new ScaledBigDecimal(this.value().sqrt(MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal pow(int n) {
        return new ScaledBigDecimal(this.value().pow(n, MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal abs() {
        return new ScaledBigDecimal(this.value().abs(MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal negate() {
        return new ScaledBigDecimal(this.value().negate(MATH_CONTEXT));
    }

    @NotNull
    public ScaledBigDecimal min(@NonNull ScaledBigDecimal val) {
        return new ScaledBigDecimal(this.value().min(val.value()));
    }

    @NotNull
    public ScaledBigDecimal min(@NonNull BigDecimal val) {
        return new ScaledBigDecimal(this.value().min(val));
    }

    @NotNull
    public ScaledBigDecimal max(@NonNull ScaledBigDecimal val) {
        return new ScaledBigDecimal(this.value().max(val.value()));
    }

    @NotNull
    public ScaledBigDecimal max(@NonNull BigDecimal val) {
        return new ScaledBigDecimal(this.value().max(val));
    }

    @NotNull
    public BigDecimal toBigDecimal(int scale) {
        return this.value().setScale(scale, RoundingMode.HALF_UP);
    }

    @NotNull
    public ScaledBigDecimal roundToScale(int scale) {
        return new ScaledBigDecimal(this.value().setScale(scale, RoundingMode.HALF_UP));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof ScaledBigDecimal other) {
            return this.value().compareTo(other.value()) == 0;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(MATH_CONTEXT, value());
    }

    @Override
    public int compareTo(@NotNull ScaledBigDecimal o) {
        return this.value().compareTo(o.value());
    }
}
