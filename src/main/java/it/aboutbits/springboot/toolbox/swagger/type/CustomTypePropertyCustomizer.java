package it.aboutbits.springboot.toolbox.swagger.type;

import com.fasterxml.jackson.databind.type.SimpleType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.reflection.util.CustomTypeReflectionUtil;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.PropertyCustomizer;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
public class CustomTypePropertyCustomizer implements PropertyCustomizer {
    @Override
    @SneakyThrows(NoSuchMethodException.class)
    public Schema<?> customize(Schema property, AnnotatedType annotatedType) {
        var type = annotatedType.getType();

        if (type instanceof SimpleType simpleType) {
            var rawClass = simpleType.getRawClass();

            if (rawClass.getName().contains(".") && !rawClass.getName().startsWith("java.")) {
                property.setDescription(SwaggerMetaUtil.setOriginalTypeFqn(
                        property.getDescription(),
                        rawClass.getName()
                ));
            }

            if (CustomType.class.isAssignableFrom(rawClass)) {
                @SuppressWarnings("unchecked")
                var wrappedType = CustomTypeReflectionUtil.getWrappedType(
                        (Class<? extends CustomType<?>>) rawClass
                );

                var isIdentity = EntityId.class.isAssignableFrom(rawClass);

                var description = SwaggerMetaUtil.setIsIdentity(property.getDescription(), isIdentity);

                if (Boolean.class.isAssignableFrom(wrappedType)) {
                    property.type("boolean");
                    property.format(null);
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (Byte.class.isAssignableFrom(wrappedType)) {
                    property.type("integer");
                    property.format("");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (Short.class.isAssignableFrom(wrappedType)) {
                    property.type("integer");
                    property.format("");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (Integer.class.isAssignableFrom(wrappedType)) {
                    property.type("integer");
                    property.format("int32");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (Long.class.isAssignableFrom(wrappedType)) {
                    property.type("integer");
                    property.format("int64");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (BigInteger.class.isAssignableFrom(wrappedType)) {
                    property.type("integer");
                    property.format("int64");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (Float.class.isAssignableFrom(wrappedType)) {
                    property.type("number");
                    property.format("float");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (Double.class.isAssignableFrom(wrappedType)) {
                    property.type("number");
                    property.format("double");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (BigDecimal.class.isAssignableFrom(wrappedType)) {
                    property.type("number");
                    property.format("");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
                    property.type("number");
                    property.format("");
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (String.class.isAssignableFrom(wrappedType)) {
                    property.type("string");
                    property.format(null);
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }
                if (Character.class.isAssignableFrom(wrappedType)) {
                    property.type("string");
                    property.format(null);
                    property.minLength(1);
                    property.maxLength(1);
                    property.setDescription(description);
                    property.setProperties(null);
                    property.set$ref(null);
                    return property;
                }

                log.warn("Property {} of type WrappedValue: Can not resolve parameter type!", property.getName());
            }
        }

        return property;
    }
}
