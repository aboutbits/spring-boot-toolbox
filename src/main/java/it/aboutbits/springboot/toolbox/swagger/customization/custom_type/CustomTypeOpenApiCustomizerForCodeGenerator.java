package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.Optional;

@Slf4j
public class CustomTypeOpenApiCustomizerForCodeGenerator implements OpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        openApi.getComponents().getSchemas().forEach(this::updateSchema);
    }

    public Optional<Class<?>> getClassFromSchemaReference(String schemaRef) {
        try {
            var className = schemaRef.replace("#/components/schemas/", "");
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    private void updateSchema(String fqn, Schema<?> schema) {
        var type = getClassFromSchemaReference(fqn);

        if (type.isEmpty()) {
            log.debug("Can not resolve type for schema reference: {}", fqn);
            return;
        }

        var clazz = type.get();

        if (CustomType.class.isAssignableFrom(clazz)) {

            var isIdentity = EntityId.class.isAssignableFrom(clazz);

            var description = SwaggerMetaUtil.setOriginalTypeFqn(
                    schema.getDescription(),
                    fqn
            );
            description = SwaggerMetaUtil.setIsIdentity(description, isIdentity);
            description = SwaggerMetaUtil.setIsCustomType(description, true);
            schema.setDescription(description);
        }

    }
}
