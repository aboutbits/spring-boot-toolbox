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

public abstract class WrappedCharacterJavaType<T extends CustomType<Character>> extends AbstractClassJavaType<T> {
    private final transient Constructor<T> constructor;

    protected WrappedCharacterJavaType(Class<T> type) {
        super(type);

        try {
            this.constructor = type.getConstructor(Character.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No constructor found for " + type.getName(), e);
        }
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration()
                .getJdbcTypeRegistry()
                .getDescriptor(Types.CHAR);
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
        if (Character.class.isAssignableFrom(aClass)) {
            return (X) id.value();
        }
        if (String.class.isAssignableFrom(aClass)) {
            return (X) String.valueOf(id.value());
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
        if (value instanceof Character charValue) {
            return constructor.newInstance(charValue);
        }
        if (value instanceof String stringValue && stringValue.length() == 1) {
            return constructor.newInstance(stringValue.charAt(0));
        }

        throw unknownWrap(value.getClass());
    }
}
