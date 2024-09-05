package it.aboutbits.springboot.toolbox.persistence.javatype.base;

import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.SneakyThrows;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Types;

public abstract class WrappedBigDecimalJavaType<T extends CustomType<BigDecimal>> extends AbstractClassJavaType<T> {
    protected WrappedBigDecimalJavaType(Class<T> type) {
        super(type);
    }

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return indicators.getTypeConfiguration()
                .getJdbcTypeRegistry()
                .getDescriptor(Types.DOUBLE);
    }

    @Override
    public <X> X unwrap(T id, Class<X> aClass, WrapperOptions wrapperOptions) {
        var javaTypeClass = getJavaTypeClass();

        if (id == null) {
            return null;
        }
        if (javaTypeClass.isAssignableFrom(aClass)) {
            return (X) id;
        }
        if (BigDecimal.class.isAssignableFrom(aClass)) {
            return (X) id.value();
        }
        throw unknownUnwrap(aClass);
    }

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
        if (value instanceof BigDecimal bigDecimal) {
            return (T) clazz.getConstructors()[0].newInstance(bigDecimal);
        }
        if (value instanceof String stringValue) {
            return (T) clazz.getConstructors()[0].newInstance(new BigDecimal(stringValue));
        }
        throw unknownWrap(value.getClass());
    }
}
