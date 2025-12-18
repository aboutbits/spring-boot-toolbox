package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Factory class that creates the appropriate tuple transformer based on the output class type.
 * <p>
 * This class maintains backward compatibility while delegating to specific transformer implementations:
 * <ul>
 *     <li>{@link PrimitiveTupleTransformer} - for Java primitives and their wrapped counterparts</li>
 *     <li>{@link WrappedTupleTransformer} - for records with a single wrapped value (CustomType)</li>
 *     <li>{@link TupleTupleTransformer} - for complex tuples with more than one value</li>
 * </ul>
 */
@NullMarked
class TupleTransformer<T> extends BaseTupleTransformer<T> {
    private final BaseTupleTransformer<T> delegate;

    TupleTransformer(Class<T> outputClass) {
        super(outputClass);

        // Find all fields and their types inside the result class
        // Ignore constants, because they will not be used as constructor parameters
        if (outputClass.isPrimitive() || isSimpleType(outputClass)) {
            delegate = new PrimitiveTupleTransformer<>(outputClass);
        } else if (CustomType.class.isAssignableFrom(outputClass)) {
            delegate = new WrappedTupleTransformer<>(outputClass);
        } else {
            delegate = new TupleTupleTransformer<>(outputClass);
        }
    }

    @Override
    public @Nullable T transformTuple(@Nullable Object[] objects, String[] aliases) {
        return delegate.transformTuple(objects, aliases);
    }
}
