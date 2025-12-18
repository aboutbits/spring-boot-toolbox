package it.aboutbits.springboot.toolbox.persistence.transformer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Transformer for real Java primitives or their wrapped counterparts (e.g., long and Long).
 */
@NullMarked
class PrimitiveTupleTransformer<T> extends BaseTupleTransformer<T> {

    PrimitiveTupleTransformer(Class<T> outputClass) {
        super(outputClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable T transformTuple(@Nullable Object[] objects, String[] aliases) {
        if (objects.length != 1) {
            throw new TransformerRuntimeException("PRIMITIVE mode does not support multiple values!");
        }

        if (objects[0] == null) {
            return null;
        }

        return (T) objects[0];
    }
}
