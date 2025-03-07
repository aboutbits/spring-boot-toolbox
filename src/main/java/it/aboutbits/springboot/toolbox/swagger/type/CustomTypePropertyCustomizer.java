package it.aboutbits.springboot.toolbox.swagger.type;

import com.fasterxml.jackson.databind.type.SimpleType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.PropertyCustomizer;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
public class CustomTypePropertyCustomizer implements PropertyCustomizer {
    @Override
    public Schema<?> customize(Schema property, AnnotatedType annotatedType) {
        var type = annotatedType.getType();

        if (type instanceof SimpleType simpleType) {
            var rawClass = simpleType.getRawClass();

            // We set the enum name as the description because swagger treats each usage as a new enum.
            // This way we can preserve the information about the original enum type.
            if (rawClass.isEnum()) {
                var displayName = rawClass.getCanonicalName().replace(rawClass.getPackage().getName() + ".", "");
                property.setDescription(displayName);
            }
        }

        if (type instanceof SimpleType simpleType && CustomType.class.isAssignableFrom(simpleType.getRawClass())) {
            var rawClass = simpleType.getRawClass();

            var displayName = rawClass.getSimpleName();

            Class<?> wrappedType;
            if (EntityId.class.isAssignableFrom(rawClass)) {
                displayName = resolveEntityIdDisplayName(rawClass);
            }

            if (rawClass.equals(EntityId.class)) {
                wrappedType = simpleType.getBindings().getBoundType(0).getRawClass();
            } else {
                var constructor = RecordReflectionUtil.getCanonicalConstructor(rawClass);
                wrappedType = constructor.getParameters()[0].getType();
            }

            if (Short.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            if (Integer.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("int32");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            if (Long.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("int64");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            if (BigInteger.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("int64");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            if (Float.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("float");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            if (Double.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("double");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            if (BigDecimal.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }

            if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("");
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            if (String.class.isAssignableFrom(wrappedType)) {
                property.type("string");
                property.format(null);
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            log.warn("Property {} of type WrappedValue: Can not resolve parameter type!", property.getName());
        }

        return property;
    }

    @NonNull
    private static String resolveEntityIdDisplayName(Class<?> rawClass) {
        var parent = rawClass.getEnclosingClass();
        if (parent != null) {
            return parent.getSimpleName() + "." + rawClass.getSimpleName();
        }
        return rawClass.getSimpleName();
    }
}
