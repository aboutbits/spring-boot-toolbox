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

    public static Class<?> getWrappedType(Class<? extends CustomType<?>> customType) {
        var customTypeInterface = Arrays.stream(customType.getGenericInterfaces())
                .filter(i ->
                                i instanceof ParameterizedType
                                        && CustomType.class.isAssignableFrom((Class<?>) ((ParameterizedType) i).getRawType())
                ).findFirst()
                .map(i -> (ParameterizedType) i)
                .orElseThrow();

        return (Class<?>) customTypeInterface.getActualTypeArguments()[0];
    }
}
