package it.aboutbits.springboot.toolbox.type;

import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A record that represents a scaled BigDecimal with a fixed MathContext.
 * This class provides various constructors for creating instances with different numerical types.
 * It also provides a set of arithmetic operations that return new instances with the appropriate scale and rounding.
 */
@NullMarked
public record ScaledBigDecimal(
        BigDecimal value
) implements CustomType<BigDecimal>, Comparable<ScaledBigDecimal> {
    private static final MathContext MATH_CONTEXT = new MathContext(15, RoundingMode.HALF_UP);

    public static final ScaledBigDecimal ZERO = new ScaledBigDecimal(0);
    public static final ScaledBigDecimal ONE = new ScaledBigDecimal(1);
    public static final ScaledBigDecimal TWO = new ScaledBigDecimal(2);
    public static final ScaledBigDecimal TEN = new ScaledBigDecimal(10);

    public ScaledBigDecimal(BigDecimal value) {
        this.value = value.setScale(MATH_CONTEXT.getPrecision(), MATH_CONTEXT.getRoundingMode());
    }

    public ScaledBigDecimal(int value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(Integer value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(BigInteger value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(long value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(Long value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public ScaledBigDecimal(float value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(Float value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(double value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(Double value) {
        this(new BigDecimal(String.valueOf(value), MATH_CONTEXT));
    }

    public ScaledBigDecimal(String value) {
        this(new BigDecimal(value, MATH_CONTEXT));
    }

    public static ScaledBigDecimal valueOf(int value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(Integer value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(BigInteger value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(long value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(Long value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(float value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(Float value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(double value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(Double value) {
        return new ScaledBigDecimal(value);
    }

    public static ScaledBigDecimal valueOf(String value) {
        return new ScaledBigDecimal(value);
    }

    public ScaledBigDecimal add(ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().add(other.value()));
    }

    public ScaledBigDecimal subtract(ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().subtract(other.value()));
    }

    public ScaledBigDecimal multiply(ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().multiply(other.value()));
    }

    public ScaledBigDecimal divide(ScaledBigDecimal other) {
        return new ScaledBigDecimal(this.value().divide(other.value(), MATH_CONTEXT));
    }

    public ScaledBigDecimal add(BigDecimal other) {
        return new ScaledBigDecimal(this.value().add(other));
    }

    public ScaledBigDecimal subtract(BigDecimal other) {
        return new ScaledBigDecimal(this.value().subtract(other));
    }

    public ScaledBigDecimal multiply(BigDecimal other) {
        return new ScaledBigDecimal(this.value().multiply(other));
    }

    public ScaledBigDecimal divide(BigDecimal other) {
        return new ScaledBigDecimal(this.value().divide(other, MATH_CONTEXT));
    }

    public ScaledBigDecimal remainder(ScaledBigDecimal divisor) {
        return new ScaledBigDecimal(this.value().remainder(divisor.value(), MATH_CONTEXT));
    }

    public ScaledBigDecimal remainder(BigDecimal divisor) {
        return new ScaledBigDecimal(this.value().remainder(divisor, MATH_CONTEXT));
    }

    public ScaledBigDecimal sqrt() {
        return new ScaledBigDecimal(this.value().sqrt(MATH_CONTEXT));
    }

    public ScaledBigDecimal pow(int n) {
        return new ScaledBigDecimal(this.value().pow(n, MATH_CONTEXT));
    }

    public ScaledBigDecimal abs() {
        return new ScaledBigDecimal(this.value().abs(MATH_CONTEXT));
    }

    public ScaledBigDecimal negate() {
        return new ScaledBigDecimal(this.value().negate(MATH_CONTEXT));
    }

    public ScaledBigDecimal min(ScaledBigDecimal val) {
        return new ScaledBigDecimal(this.value().min(val.value()));
    }

    public ScaledBigDecimal min(BigDecimal val) {
        return new ScaledBigDecimal(this.value().min(val));
    }

    public ScaledBigDecimal max(ScaledBigDecimal val) {
        return new ScaledBigDecimal(this.value().max(val.value()));
    }

    public ScaledBigDecimal max(BigDecimal val) {
        return new ScaledBigDecimal(this.value().max(val));
    }

    public BigDecimal toBigDecimal(int scale) {
        return this.value().setScale(scale, RoundingMode.HALF_UP);
    }

    public ScaledBigDecimal roundToScale(int scale) {
        return new ScaledBigDecimal(this.value().setScale(scale, RoundingMode.HALF_UP));
    }

    @Override

    public String toString() {
        return this.value().toString();
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
        return value().hashCode();
    }

    @Override
    public int compareTo(ScaledBigDecimal o) {
        return this.value().compareTo(o.value());
    }

    /**
     * @deprecated Allow drop-in-replacement of ScaledBigDecimal in the original project.
     * This is temporary. We need to avoid changing any rounding logic at the moment.
     */
    @Deprecated

    public BigDecimal toCurrency() {
        return this.value().setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @deprecated Allow drop-in-replacement of ScaledBigDecimal in the original project.
     * This is temporary. We need to avoid changing any rounding logic at the moment.
     */
    @Deprecated

    public ScaledBigDecimal roundToCurrency() {
        return new ScaledBigDecimal(this.value().setScale(2, RoundingMode.HALF_UP));
    }

    public int intValue() {
        return this.value().intValue();
    }

    public long longValue() {
        return this.value().longValue();
    }

    public float floatValue() {
        return this.value().floatValue();
    }

    public double doubleValue() {
        return this.value().doubleValue();
    }

    public byte byteValue() {
        return (byte) intValue();
    }

    public short shortValue() {
        return (short) intValue();
    }
}
