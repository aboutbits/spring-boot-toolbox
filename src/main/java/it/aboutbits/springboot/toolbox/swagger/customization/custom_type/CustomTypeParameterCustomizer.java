package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import it.aboutbits.springboot.toolbox.reflection.util.CustomTypeReflectionUtil;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CustomTypeParameterCustomizer implements ParameterCustomizer {

    public Optional<Class<?>> getClassFromSchemaReference(String schemaRef) {
        try {
            var className = schemaRef.replace("#/components/schemas/", "");
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            // Log the error if needed
            return Optional.empty();
        }
    }


    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    @SneakyThrows(NoSuchMethodException.class)
    public Parameter customize(Parameter parameter, MethodParameter methodParameter) {
        if (parameter == null) {
            return null;
        }

        String schemaRef = null;
        var property = parameter.getSchema();

        if (parameter.getSchema() instanceof ArraySchema
                && parameter.getSchema().getItems() != null
                && parameter.getSchema().getItems().get$ref() != null
        ) {
            schemaRef = parameter.getSchema().getItems().get$ref();
            property = parameter.getSchema().getItems();
        }
        if (parameter.getSchema() instanceof ObjectSchema
                && parameter.getSchema().get$ref() != null
        ) {
            schemaRef = parameter.getSchema().getItems().get$ref();
        }

        if (schemaRef == null) {
            return parameter;
        }

        var optionalClass = getClassFromSchemaReference(schemaRef);

        if (optionalClass.isEmpty()) {
            log.error("Can not resolve type for schema reference: {}", schemaRef);
            return parameter; // throw new RuntimeException();
        }


        var rawClass = optionalClass.get();

        if (rawClass.getName().contains(".") && !rawClass.getName().startsWith("java.")) {
            property.setDescription(SwaggerMetaUtil.setOriginalTypeFqn(
                    property.getDescription(),
                    rawClass.getName()
            ));
        }

        if (CustomType.class.isAssignableFrom(rawClass)) {
            Class<?> wrappedType = null;
            if (!rawClass.equals(EntityId.class)) {
                wrappedType = CustomTypeReflectionUtil.getWrappedType(
                        (Class<? extends CustomType<?>>) rawClass
                );
            }

            var isIdentity = EntityId.class.isAssignableFrom(rawClass);

            var description = SwaggerMetaUtil.setIsIdentity(property.getDescription(), isIdentity);

            if (wrappedType == null) {
                return parameter;
            }

            if (Boolean.class.isAssignableFrom(wrappedType)) {
                property.type("boolean");
                property.format(null);
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (Byte.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (Short.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (Integer.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("int32");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (Long.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("int64");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (BigInteger.class.isAssignableFrom(wrappedType)) {
                property.type("integer");
                property.format("");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (Float.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("float");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (Double.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("double");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (BigDecimal.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
                property.type("number");
                property.format("");
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (String.class.isAssignableFrom(wrappedType)) {
                property.type("string");
                property.format(null);
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (Character.class.isAssignableFrom(wrappedType)) {
                property.type("string");
                property.format(null);
                property.minLength(1);
                property.maxLength(1);
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }
            if (UUID.class.isAssignableFrom(wrappedType)) {
                property.type("string");
                property.format("uuid");
                property.minLength(36);
                property.maxLength(36);
                property.setDescription(description);
                property.setProperties(null);
                property.set$ref(null);
                return parameter;
            }

            log.warn("Property {} of type WrappedValue: Can not resolve parameter type!", property.getName());
        }

        return parameter;
    }
}
