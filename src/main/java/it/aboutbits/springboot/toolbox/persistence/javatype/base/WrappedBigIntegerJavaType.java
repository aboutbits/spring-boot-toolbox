package it.aboutbits.springboot.toolbox.persistence.javatype.base;

import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.SneakyThrows;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.Types;

public abstract class WrappedBigIntegerJavaType<T extends CustomType<BigInteger>> extends AbstractClassJavaType<T> {
    private final transient Constructor<T> canonicalConstructor;

    protected WrappedBigIntegerJavaType(Class<T> type) {
        super(type);

        this.canonicalConstructor = RecordReflectionUtil.getCanonicalConstructor(type);
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration()
                .getJdbcTypeRegistry()
                .getDescriptor(Types.BIGINT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrap(T id, Class<X> aClass, WrapperOptions wrapperOptions) {
        var javaTypeClass = getJavaTypeClass();

        if (id == null) {
            return null;
        }
        if (javaTypeClass.isAssignableFrom(aClass)) {
            return (X) id;
        }
        if (Long.class.isAssignableFrom(aClass)) {
            return (X) Long.valueOf(id.value().longValue());
        }

        throw unknownUnwrap(aClass);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows({InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class})
    @Override
    public <X> T wrap(X value, WrapperOptions wrapperOptions) {
        var clazz = getJavaTypeClass();

        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        if (value instanceof Long longValue) {
            return canonicalConstructor.newInstance(BigInteger.valueOf(longValue));
        }

        throw unknownWrap(value.getClass());
    }
}
