package it.aboutbits.springboot.toolbox.reflection.util;

import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.UUID;

@NullMarked
public final class CustomTypeReflectionUtil {
    private CustomTypeReflectionUtil() {
    }

    public static <T extends CustomType<?>> Constructor<T> getCustomTypeConstructor(Class<T> customType) throws NoSuchMethodException {
        try {
            return customType.getConstructor(
                    getWrappedType(customType)
            );
        } catch (NoSuchMethodException | SecurityException _) {
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

    public static boolean isSupportedWrappedType(Class<?> wrappedType) {
        return Boolean.class.isAssignableFrom(wrappedType)
                || String.class.isAssignableFrom(wrappedType)
                || Character.class.isAssignableFrom(wrappedType)
                || Byte.class.isAssignableFrom(wrappedType)
                || Short.class.isAssignableFrom(wrappedType)
                || Integer.class.isAssignableFrom(wrappedType)
                || Long.class.isAssignableFrom(wrappedType)
                || Float.class.isAssignableFrom(wrappedType)
                || Double.class.isAssignableFrom(wrappedType)
                || BigInteger.class.isAssignableFrom(wrappedType)
                || BigDecimal.class.isAssignableFrom(wrappedType)
                || ScaledBigDecimal.class.isAssignableFrom(wrappedType)
                || UUID.class.isAssignableFrom(wrappedType)
                || Enum.class.isAssignableFrom(wrappedType)
                || wrappedType.isEnum();
    }
}
