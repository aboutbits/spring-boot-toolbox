package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;

public class TupleTransformer<T> {
    private final Class<T> outputClass;
    private Constructor<T> outputClassConstructor = null;
    private Class<?>[] outputClassFieldClasses = null;

    private final Mode mode;

    private enum Mode {
        PRIMITIVE,  // Real Java primitives or their wrapped counterpart (ex., long and Long)
        WRAPPED,  // Record with a single wrapped value (CustomType as for example Iban)
        TUPLE  // Complex tuples with more than one value
    }

    public TupleTransformer(Class<T> outputClass) {
        this.outputClass = outputClass;

        // Find all fields and their types inside the result class
        // Ignore constants, because they will not be used as constructor parameters
        if (outputClass.isPrimitive() || isSimpleType(outputClass)) {
            mode = Mode.PRIMITIVE;
        } else if (CustomType.class.isAssignableFrom(outputClass)) {
            mode = Mode.WRAPPED;
        } else {
            mode = Mode.TUPLE;

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
    }

    @SuppressWarnings("unchecked")
    public T transform(Object[] objects) {
        try {
            if (Mode.PRIMITIVE.equals(mode)) {
                if (objects.length != 1) {
                    throw new TransformerRuntimeException("PRIMITIVE mode does not support multiple values!");
                }
                return (T) objects[0];
            }

            if (Mode.WRAPPED.equals(mode)) {
                if (objects.length != 1) {
                    throw new TransformerRuntimeException("WRAPPED mode does not support multiple values!");
                }
                return (T) toCustomType(objects[0], (Class<CustomType<?>>) outputClass);
            }

            // If we have a single entry in the result, and that entry matches the desired result class
            // we can just give it back, no casting, nor type-checking needed. We can just unbox it and
            // give it back as-is!
            // Example: "SELECT p FROM Person p"
            if (objects.length == 1 && outputClass == objects[0].getClass()) {
                return (T) objects[0];
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

                // Everything ok, null matches every object and equal classes do not need casting!
                // Unboxing of primitives is automatic when we call the constructor of the target result class.
                if (objects[i] == null || outputClassFieldClasses[i].isPrimitive() || objects[i].getClass() == outputClassFieldClasses[i]) {
                    continue;
                }

                // Check if the two classes are either the same, or if it is a superclass or superinterface of it...
                // For example, casting an ArrayList to List can be done directly
                if (outputClassFieldClasses[i].isAssignableFrom(objects[i].getClass())) {
                    objects[i] = outputClassFieldClasses[i].cast(objects[i]);
                    continue;
                }

                // Converter: STRING to ENUM
                // A string from the DB, that does not match a corresponding field inside the result class
                // should probably be an enum value, which implements the "valueOf" interface.
                if (objects[i] instanceof String && outputClassFieldClasses[i].isEnum()) {
                    objects[i] = outputClassFieldClasses[i].getMethod("valueOf", String.class).invoke(
                            null,
                            objects[i].toString()
                    );
                    continue;
                }

                // Converter: Instant to OffsetDateTime
                if (objects[i] instanceof Instant instant && outputClassFieldClasses[i].isAssignableFrom(OffsetDateTime.class)) {
                    objects[i] = OffsetDateTime.ofInstant(
                            instant,
                            ZoneId.systemDefault()
                    );
                    continue;
                }

                // Converter: to Records that wrap exactly one value (CustomType)
                if (CustomType.class.isAssignableFrom(outputClassFieldClasses[i])) {
                    objects[i] = toCustomType(objects[i], (Class<? extends CustomType<?>>) outputClassFieldClasses[i]);
                    continue;
                }

                // Non-matching classes in fields. No converter found...
                throw new UnsupportedOperationException(
                        String.format(
                                "Query transformation: Type mismatch without converter. Cannot cast from %s to %s.",
                                objects[i].getClass().getName(),
                                outputClassFieldClasses[i].getName()
                        )
                );
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

    private static <T> boolean isSimpleType(Class<T> outputClass) {
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

    private static <X extends CustomType<?>> X toCustomType(
            Object actualValue,
            Class<X> targetType
    ) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = RecordReflectionUtil.getConstructorForType(targetType, actualValue.getClass());

        return constructor.newInstance(actualValue);
    }
}
