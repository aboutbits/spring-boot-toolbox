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
import java.sql.Types;

public abstract class WrappedDoubleJavaType<T extends CustomType<Double>> extends AbstractClassJavaType<T> {
    private final transient Constructor<T> canonicalConstructor;

    protected WrappedDoubleJavaType(Class<T> type) {
        super(type);

        this.canonicalConstructor = RecordReflectionUtil.getCanonicalConstructor(type);
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration()
                .getJdbcTypeRegistry()
                .getDescriptor(Types.DOUBLE);
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
        if (Double.class.isAssignableFrom(aClass)) {
            return (X) id.value();
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
        if (value instanceof Double doubleValue) {
            return canonicalConstructor.newInstance(doubleValue);
        }

        throw unknownWrap(value.getClass());
    }
}
