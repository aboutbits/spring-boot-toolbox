package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;

/**
 * Transformer for complex tuples with more than one value (records/classes with multiple fields).
 */
@NullMarked
class TupleTupleTransformer<T> extends BaseTupleTransformer<T> {
    private final Constructor<T> outputClassConstructor;
    private final Class<?>[] outputClassFieldClasses;

    TupleTupleTransformer(Class<T> outputClass) {
        super(outputClass);

        // Find all fields and their types inside the result class
        // Ignore constants, because they will not be used as constructor parameters
        outputClassFieldClasses = Arrays
                .stream(outputClass.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(Field::getType)
                .toArray(Class[]::new);

        // Find the all-args-constructor inside the result class
        try {
            outputClassConstructor = outputClass.getDeclaredConstructor(outputClassFieldClasses);
            outputClassConstructor.setAccessible(true);
        } catch (NoSuchMethodException exception) {
            throw new TransformerRuntimeException(
                    String.format(
                            "Query transformation: Could not find a valid constructor in target class %s",
                            outputClass.getName()
                    ),
                    exception
            );
        }
    }

    @Override
    public @Nullable T transformTuple(@Nullable Object[] objects, String[] aliases) {
        try {
            // If we have a single entry in the result, and that entry matches the desired result class
            // we can just give it back, no casting, nor type-checking needed. We can just unbox it and
            // give it back as-is!
            // Example: "SELECT p FROM Person p"
            if (objects.length == 1) {
                if (objects[0] == null) {
                    return null;
                }
                if (outputClass == objects[0].getClass()) {
                    // noinspection unchecked
                    return (T) objects[0];
                }
            }

            if (objects.length != outputClassFieldClasses.length) {
                throw new TransformerRuntimeException(
                        String.format(
                                "Invalid query transforming: object count does not match target class field count for %s",
                                outputClass.getName()
                        )
                );
            }

            // Unboxing not possible, we have a complex combined result, check single record entries for type-safety!
            for (var i = 0; i < objects.length; i++) {
                transformObject(objects, i);
            }

            return outputClassConstructor.newInstance(objects);
        } catch (
                InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException
                | UnsupportedOperationException exception
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

    /**
     * objects are manipulated in-place
     */
    private void transformObject(
            @Nullable Object[] objects,
            int i
    ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        var obj = objects[i];

        if (obj == null) {
            objects[i] = null;
            return;
        }

        // Everything ok, null matches every object and equal classes do not need casting!
        // Unboxing of primitives is automatic when we call the constructor of the target result class.
        if (outputClassFieldClasses[i].isPrimitive() || obj.getClass() == outputClassFieldClasses[i]) {
            return;
        }

        // Check if the two classes are either the same, or if it is a superclass or superinterface of it...
        // For example, casting an ArrayList to List can be done directly
        if (outputClassFieldClasses[i].isAssignableFrom(obj.getClass())) {
            objects[i] = outputClassFieldClasses[i].cast(obj);
            return;
        }

        // Converter: STRING to ENUM
        // A string from the DB, that does not match a corresponding field inside the result class
        // should probably be an enum value, which implements the "valueOf" interface.
        if (obj instanceof String && outputClassFieldClasses[i].isEnum()) {
            objects[i] = outputClassFieldClasses[i].getMethod("valueOf", String.class).invoke(
                    null,
                    obj.toString()
            );
            return;
        }

        // Converter: Instant to OffsetDateTime
        if (obj instanceof Instant instant && outputClassFieldClasses[i].isAssignableFrom(OffsetDateTime.class)) {
            objects[i] = OffsetDateTime.ofInstant(
                    instant,
                    ZoneId.systemDefault()
            );
            return;
        }

        // Converter: to Records that wrap exactly one value (CustomType)
        if (CustomType.class.isAssignableFrom(outputClassFieldClasses[i])) {
            // noinspection unchecked
            objects[i] = toCustomType(obj, (Class<? extends CustomType<?>>) outputClassFieldClasses[i]);
            return;
        }

        // Non-matching classes in fields. No converter found...
        throw new UnsupportedOperationException(
                String.format(
                        "Query transformation: Type mismatch without converter. Cannot cast from %s to %s.",
                        obj.getClass().getName(),
                        outputClassFieldClasses[i].getName()
                )
        );
    }
}
