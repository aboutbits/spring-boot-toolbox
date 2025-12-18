package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.ArrayList;
import java.util.Map;

@NullMarked
public class NullableCustomizer implements OpenApiCustomizer {
    public static final String NULLABLE_MARKER = "NULLABLE";

    @Override
    @SuppressWarnings("unchecked")
    public void customise(OpenAPI openApi) {
        if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
            return;
        }
        openApi.getComponents().getSchemas().values()
                .forEach(schema -> {
                    var requiredProperties = new ArrayList<String>();
                    if (((Schema<?>) schema).getProperties() != null) {
                        var properties = ((Schema<?>) schema).getProperties();
                        processProperties(properties, requiredProperties);
                    }
                    if (schema.getAllOf() != null) {
                        schema.getAllOf().forEach(allOfSchema -> {
                            var allOfSchemaTyped = (Schema<?>) allOfSchema;
                            if (allOfSchemaTyped.getProperties() != null) {
                                var properties = allOfSchemaTyped.getProperties();
                                processProperties(properties, requiredProperties);
                            }
                        });
                    }
                    schema.setRequired(requiredProperties);
                });
    }

    private static void processProperties(Map<String, Schema> properties, ArrayList<String> requiredProperties) {
        properties.forEach((propertyName, property) -> {
            var isNullable = isNullable(property);

            if (!isNullable) {
                requiredProperties.add(propertyName);
            } else {
                requiredProperties.remove(propertyName);
            }
            if (property.getTitle() != null && property.getTitle().equals(NULLABLE_MARKER)) {
                property.setTitle(null);
            }
            if (property.get$ref() != null) {
                property.set$ref(property.get$ref().replace(NULLABLE_MARKER, ""));
            }
            if (property.getItems() != null && property.getItems().get$ref() != null) {
                property.getItems().set$ref(property.getItems().get$ref().replace(NULLABLE_MARKER, ""));
            }
        });
    }

    private static boolean isNullable(Schema<?> property) {
        if (property.getTitle() != null && property.getTitle().equals(NULLABLE_MARKER)) {
            return true;
        }

        if (property.get$ref() != null && property.get$ref().endsWith(NULLABLE_MARKER)) {
            return true;
        }

        if (property.getItems() != null && property.getItems().get$ref() != null && property.getItems()
                .get$ref()
                .endsWith("?nullable=true")) {
            return true;
        }

        return false;
    }
}
