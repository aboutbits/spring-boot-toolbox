package it.aboutbits.springboot.toolbox.persistence.javatype.base;

import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.SneakyThrows;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

@NullMarked
public abstract class WrappedEnumJavaType<T extends CustomType<? extends Enum<?>>> extends AbstractClassJavaType<T> {
    private final transient Constructor<T> constructor;
    private final Class<? extends Enum<?>> enumClass;

    @SuppressWarnings("unchecked")
    protected WrappedEnumJavaType(Class<T> type) {
        super(type);

        try {
            this.enumClass = determineEnumClass(type);
            this.constructor = type.getConstructor(enumClass);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No constructor found for " + type.getName() + " with enum parameter", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Enum<?>> determineEnumClass(Class<T> type) {
        // Try to extract from the generic superclass (if the CustomType exposes the enum via inheritance)
        var genericSuperclass = type.getGenericSuperclass();
        if (genericSuperclass instanceof java.lang.reflect.ParameterizedType parameterizedType) {
            var actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class<?> enumType) {
                if (enumType.isEnum()) {
                    return (Class<? extends Enum<?>>) enumType;
                }
            }
        }
        // Fallback: inspect constructors and use the single-arg enum constructor (works for records and classes)
        for (var ctor : type.getDeclaredConstructors()) {
            var params = ctor.getParameterTypes();
            if (params.length == 1 && params[0].isEnum()) {
                return (Class<? extends Enum<?>>) params[0];
            }
        }
        throw new IllegalStateException("Could not determine enum type for " + type.getName());
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration()
                .getJdbcTypeRegistry()
                .getDescriptor(Types.VARCHAR);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <X> X unwrap(@Nullable T id, Class<X> aClass, WrapperOptions wrapperOptions) {
        var javaTypeClass = getJavaTypeClass();

        if (id == null) {
            return null;
        }
        if (javaTypeClass.isAssignableFrom(aClass)) {
            return (X) id;
        }
        if (String.class.isAssignableFrom(aClass)) {
            return (X) id.value().name();
        }
        if (enumClass.isAssignableFrom(aClass)) {
            return (X) id.value();
        }

        throw unknownUnwrap(aClass);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows({InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class})
    @Override
    @Nullable
    public <X> T wrap(@Nullable X value, WrapperOptions wrapperOptions) {
        var clazz = getJavaTypeClass();

        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        if (value instanceof String stringValue) {
            var enumValue = Enum.valueOf((Class<? extends Enum>) enumClass, stringValue);
            return constructor.newInstance(enumValue);
        }
        if (enumClass.isInstance(value)) {
            return constructor.newInstance(value);
        }

        throw unknownWrap(value.getClass());
    }
}
