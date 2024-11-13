package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.ArrayList;

public class NullableCustomizer implements OpenApiCustomizer {
    @Override
    @SuppressWarnings("unchecked")
    public void customise(OpenAPI openApi) {
        if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
            return;
        }
        openApi.getComponents().getSchemas().values()
                .forEach(schema -> {
                    if (((Schema<?>) schema).getProperties() != null) {
                        var requiredProperties = new ArrayList<String>();
                        ((Schema<?>) schema).getProperties().forEach((propertyName, property) -> {
                            if (property.getNullable() == null || !property.getNullable()) {
                                requiredProperties.add(propertyName);
                            }
                        });
                        schema.setRequired(requiredProperties);
                    }
                });
    }
}
