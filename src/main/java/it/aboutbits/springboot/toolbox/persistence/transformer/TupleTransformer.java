package it.aboutbits.springboot.toolbox.persistence.transformer;

import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class TupleTransformer<T> {

    private final Class<T> outputClass;
    private final Constructor<T> outputClassConstructor;
    private final Class[] outputClassFieldClasses;

    public TupleTransformer(final Class<T> outputClass) {
        this.outputClass = outputClass;

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

    @SuppressWarnings("unchecked")
    public T transform(final Object[] objects) {
        try {

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

                // Converter: to WrappedValue
                if (CustomType.class.isAssignableFrom(outputClassFieldClasses[i])) {
                    objects[i] = toWrappedValue(objects[i], outputClassFieldClasses[i]);
                    continue;
                }

                // Converter: Instant to OffsetDateTime
                if (objects[i] instanceof Instant && outputClassFieldClasses[i].isAssignableFrom(OffsetDateTime.class)) {
                    objects[i] = OffsetDateTime.ofInstant(
                            (Instant) objects[i],
                            ZoneId.systemDefault()
                    );
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

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException
                 | UnsupportedOperationException exception) {
            throw new TransformerRuntimeException(
                    String.format(
                            "Query transformation: Given database record cannot be converted into target class %s",
                            outputClass.getName()
                    ),
                    exception
            );
        }
    }

    @SuppressWarnings("unchecked")
    private <X extends CustomType<?>> X toWrappedValue(
            Object actualValue,
            Class<X> targetType
    ) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // types with a preferred constructor
        if (targetType.isAssignableFrom(ScaledBigDecimal.class)) {
            var constructor = targetType.getDeclaredConstructor(Double.class);
            var value = (Double) actualValue;
            return constructor.newInstance(value);
        }

        // types using first suitable constructor
        Constructor<?>[] constructors = targetType.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 1) {
                if (Number.class.isAssignableFrom(parameterTypes[0])) {
                    if (Long.class.equals(parameterTypes[0])) {
                        var val = (Long) actualValue;
                        return (X) constructor.newInstance(val);
                    } else if (Integer.class.equals(parameterTypes[0])) {
                        var val = (Integer) actualValue;
                        return (X) constructor.newInstance(val);
                    } else if (Double.class.equals(parameterTypes[0])) {
                        var val = (Double) actualValue;
                        return (X) constructor.newInstance(val);
                    } else if (Float.class.equals(parameterTypes[0])) {
                        var val = (Float) actualValue;
                        return (X) constructor.newInstance(val);
                    } else if (BigInteger.class.equals(parameterTypes[0])) {
                        var val = BigInteger.valueOf((Long) actualValue);
                        return (X) constructor.newInstance(val);
                    } else if (BigDecimal.class.equals(parameterTypes[0])) {
                        var val = BigDecimal.valueOf((Double) actualValue);
                        return (X) constructor.newInstance(val);
                    } else {
                        throw new IllegalArgumentException("Unsupported number type");
                    }
                } else if (String.class.equals(parameterTypes[0])) {
                    var val = (String) actualValue;
                    return (X) constructor.newInstance(val);
                } else if (Boolean.class.equals(parameterTypes[0])) {
                    var val = (Boolean) actualValue;
                    return (X) constructor.newInstance(val);
                } // Add more types as needed
            }
        }
        throw new IllegalArgumentException(targetType.getSimpleName() + " does not have a suitable single-value constructor");
    }
}
