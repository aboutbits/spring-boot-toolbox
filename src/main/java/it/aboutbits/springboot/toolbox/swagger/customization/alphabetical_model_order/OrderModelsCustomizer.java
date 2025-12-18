package it.aboutbits.springboot.toolbox.swagger.customization.alphabetical_model_order;

import io.swagger.v3.oas.models.OpenAPI;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.TreeMap;

@NullMarked
public class OrderModelsCustomizer implements OpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        var components = openApi.getComponents();

        if (components != null && components.getSchemas() != null) {
            components.schemas(new TreeMap<>(components.getSchemas()));
        }
    }
}
