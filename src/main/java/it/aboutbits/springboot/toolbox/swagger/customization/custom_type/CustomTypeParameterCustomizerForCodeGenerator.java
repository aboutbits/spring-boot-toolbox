package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;

import java.util.Optional;

@Slf4j
public class CustomTypeParameterCustomizerForCodeGenerator implements ParameterCustomizer {
    @SuppressWarnings("checkstyle:MethodLength")
    @Override
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
            return parameter;
        }


        var rawClass = optionalClass.get();

        if (rawClass.getName().contains(".") && !rawClass.getName().startsWith("java.")) {
            property.setDescription(SwaggerMetaUtil.setOriginalTypeFqn(
                    property.getDescription(),
                    rawClass.getName()
            ));
        }

        return parameter;
    }

    public Optional<Class<?>> getClassFromSchemaReference(String schemaRef) {
        try {
            var className = schemaRef.replace("#/components/schemas/", "");
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
