package it.aboutbits.springboot.toolbox.reflection.util;

import java.lang.reflect.Constructor;

public final class RecordReflectionUtil {
    private RecordReflectionUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getCanonicalConstructor(Class<T> recordClass) {
        if (!recordClass.isRecord()) {
            throw new IllegalArgumentException("Class must be a record: " + recordClass.getName());
        }

        var constructors = recordClass.getDeclaredConstructors();

        for (var constructor : constructors) {
            if (parametersAreEqual(recordClass, constructor)) {
                return (Constructor<T>) constructor;
            }
        }

        throw new IllegalStateException("Canonical constructor not found for the record: " + recordClass.getName());
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructorForType(
            Class<T> recordClass,
            Class<?> type
    ) {
        if (!recordClass.isRecord()) {
            throw new IllegalArgumentException("Class must be a record: " + recordClass.getName());
        }

        var constructors = recordClass.getDeclaredConstructors();

        for (var constructor : constructors) {
            if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].equals(type)) {
                return (Constructor<T>) constructor;
            }
        }

        throw new IllegalStateException("No constructor for type (" + type.getName() + ") found for the record: " + recordClass.getName());
    }

    private static <T> boolean parametersAreEqual(Class<T> recordClass, Constructor<?> constructor) {
        var recordComponents = recordClass.getRecordComponents();
        var parameterTypes = constructor.getParameterTypes();

        if (recordComponents.length != parameterTypes.length) {
            return false;
        }

        for (int i = 0; i < recordComponents.length; i++) {
            if (!parameterTypes[i].equals(recordComponents[i].getType())) {
                return false;
            }
        }
        return true;
    }
}
