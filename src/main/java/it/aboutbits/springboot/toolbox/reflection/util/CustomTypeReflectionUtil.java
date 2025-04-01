package it.aboutbits.springboot.toolbox.reflection.util;

import it.aboutbits.springboot.toolbox.type.CustomType;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public final class CustomTypeReflectionUtil {
    private CustomTypeReflectionUtil() {
    }

    public static <T extends CustomType<?>> Constructor<T> getCustomTypeConstructor(Class<T> customType) throws NoSuchMethodException {
        try {
            return customType.getConstructor(
                    getWrappedType(customType)
            );
        } catch (NoSuchMethodException | SecurityException e) {
            throw new NoSuchMethodException();
        }
    }

    public static Class<?> getWrappedType(Class<? extends CustomType<?>> customType) throws NoSuchMethodException {
        Class<?> currentClass = customType;
        while (currentClass != null) {
            // Check interfaces of the current class
            var customTypeInterface = Arrays.stream(currentClass.getGenericInterfaces())
                    .filter(i ->
                                    i instanceof ParameterizedType
                                            && CustomType.class.isAssignableFrom((Class<?>) ((ParameterizedType) i).getRawType())
                    ).findFirst()
                    .map(i -> (ParameterizedType) i);

            if (customTypeInterface.isPresent()) {
                return (Class<?>) customTypeInterface.get().getActualTypeArguments()[0];
            }

            // Move to the parent class
            currentClass = currentClass.getSuperclass();
        }

        throw new NoSuchMethodException();
    }
}
