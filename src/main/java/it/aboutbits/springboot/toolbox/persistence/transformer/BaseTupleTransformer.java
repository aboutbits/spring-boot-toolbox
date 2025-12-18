package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

@NullMarked
abstract class BaseTupleTransformer<T> implements org.hibernate.query.TupleTransformer<T> {
    protected final Class<T> outputClass;

    protected BaseTupleTransformer(Class<T> outputClass) {
        this.outputClass = outputClass;
    }

    @Override
    public abstract @Nullable T transformTuple(@Nullable Object[] objects, String[] strings);

    protected static <T> boolean isSimpleType(Class<T> outputClass) {
        return String.class.isAssignableFrom(outputClass)
                || Float.class.isAssignableFrom(outputClass)
                || Double.class.isAssignableFrom(outputClass)
                || Short.class.isAssignableFrom(outputClass)
                || Integer.class.isAssignableFrom(outputClass)
                || Long.class.isAssignableFrom(outputClass)
                || Character.class.isAssignableFrom(outputClass)
                || Byte.class.isAssignableFrom(outputClass)
                || Boolean.class.isAssignableFrom(outputClass);
    }

    protected static <X extends CustomType<?>> X toCustomType(
            Object actualValue,
            Class<X> targetType
    ) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = RecordReflectionUtil.getConstructorForType(targetType, actualValue.getClass());

        return constructor.newInstance(actualValue);
    }
}
