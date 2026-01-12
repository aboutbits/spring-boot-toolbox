package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;

import static it.aboutbits.springboot.toolbox.swagger.customization.custom_type.SchemaUtil.getClassFromSchemaReference;

@Slf4j
@NullMarked
public class CustomTypeParameterCustomizerForCodeGenerator implements ParameterCustomizer {
    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    @Nullable
    public Parameter customize(@Nullable Parameter parameter, MethodParameter methodParameter) {
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
}
