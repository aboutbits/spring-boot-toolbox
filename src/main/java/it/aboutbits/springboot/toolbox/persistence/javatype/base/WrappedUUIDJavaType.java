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
import java.nio.charset.StandardCharsets;
import java.sql.Types;
import java.util.UUID;

@NullMarked
public abstract class WrappedUUIDJavaType<T extends CustomType<UUID>> extends AbstractClassJavaType<T> {
    private final transient Constructor<T> constructor;

    protected WrappedUUIDJavaType(Class<T> type) {
        super(type);

        try {
            this.constructor = type.getConstructor(UUID.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No method found for " + type.getName(), e);
        }
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration()
                .getJdbcTypeRegistry()
                .getDescriptor(Types.OTHER);
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
        if (UUID.class.isAssignableFrom(aClass)) {
            return (X) id.value();
        }
        if (String.class.isAssignableFrom(aClass)) {
            return (X) id.value().toString();
        }
        if (byte[].class.isAssignableFrom(aClass)) {
            return (X) id.value().toString().getBytes(StandardCharsets.UTF_8);
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

        return switch (value) {
            case UUID uuidValue -> constructor.newInstance(uuidValue);
            case String uuidStringValue -> constructor.newInstance(UUID.fromString(uuidStringValue));
            case byte[] uuidBytesValue -> constructor.newInstance(UUID.fromString(new String(
                    uuidBytesValue,
                    StandardCharsets.UTF_8
            )));
            default -> throw unknownWrap(value.getClass());
        };
    }
}
