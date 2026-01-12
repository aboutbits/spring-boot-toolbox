package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

/**
 * Transformer for records with a single wrapped value (CustomType, e.g., Iban, EmailAddress).
 */
@NullMarked
class WrappedTupleTransformer<T> extends BaseTupleTransformer<T> {

    WrappedTupleTransformer(Class<T> outputClass) {
        super(outputClass);
    }

    @Override
    public @Nullable T transformTuple(@Nullable Object[] objects, String[] aliases) {
        try {
            if (objects.length != 1) {
                throw new TransformerRuntimeException("WRAPPED mode does not support multiple values!");
            }
            if (objects[0] == null) {
                return null;
            }
            // noinspection unchecked
            return (T) toCustomType(objects[0], (Class<CustomType<?>>) outputClass);
        } catch (
                InvocationTargetException
                | InstantiationException
                | IllegalAccessException exception
        ) {
            throw new TransformerRuntimeException(
                    String.format(
                            "Query transformation: Given database record cannot be converted into target class %s",
                            outputClass.getName()
                    ),
                    exception
            );
        }
    }
}
