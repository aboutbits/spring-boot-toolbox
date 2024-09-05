package it.aboutbits.springboot.toolbox.boot.type.swagger;

import com.fasterxml.jackson.databind.type.SimpleType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.persistence.identity.EntityId;
import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class IdentityPropertyCustomizer implements PropertyCustomizer {
    @Override
    public Schema<?> customize(Schema property, AnnotatedType annotatedType) {
        var type = annotatedType.getType();

        if (type instanceof SimpleType simpleType && EntityId.class.isAssignableFrom(simpleType.getRawClass())) {
            String displayName = null;

            var bindings = simpleType.getBindings();
            var boundType = bindings.getBoundType(0);

            if (boundType == null) {
                var rawClass = simpleType.getRawClass();
                displayName = resolveDisplayName(rawClass);
            }

            var constructor = RecordReflectionUtil.getCanonicalConstructor(simpleType.getRawClass());

            var wrappedType = constructor.getParameters()[0].getType();


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
            if (String.class.isAssignableFrom(wrappedType)) {
                property.type("string");
                property.format(null);
                property.setDescription(displayName);
                property.setProperties(null);
                property.set$ref(null);
                return property;
            }
            log.warn("Property {} of type EntityId: Can not resolve parameter type!", property.getName());
        }

        return property;
    }

    @NonNull
    private static String resolveDisplayName(Class<?> rawClass) {
        var parent = rawClass.getEnclosingClass();
        if (parent != null) {
            return parent.getSimpleName() + "." + rawClass.getSimpleName();
        }
        return rawClass.getSimpleName();
    }
}
