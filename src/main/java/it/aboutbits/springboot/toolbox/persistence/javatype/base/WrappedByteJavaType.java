package it.aboutbits.springboot.toolbox.persistence.javatype.base;

import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.SneakyThrows;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

public abstract class WrappedByteJavaType<T extends CustomType<Byte>> extends AbstractClassJavaType<T> {
    private final transient Constructor<T> constructor;

    protected WrappedByteJavaType(Class<T> type) {
        super(type);

        try {
            this.constructor = type.getConstructor(Byte.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No constructor found for " + type.getName(), e);
        }
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration()
                .getJdbcTypeRegistry()
                .getDescriptor(Types.TINYINT);
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
        if (Byte.class.isAssignableFrom(aClass)) {
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
        if (value instanceof Byte byteValue) {
            return constructor.newInstance(byteValue);
        }

        throw unknownWrap(value.getClass());
    }
}
