package it.aboutbits.springboot.toolbox.swagger.customization.force_schema;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.swagger.annotation.ForceSwaggerSchema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.LinkedHashMap;

@RequiredArgsConstructor
public class ForceSchemaCustomizer implements OpenApiCustomizer {
    private final ModelResolver modelResolver;
    private final ClassScannerUtil.ClassScanner classScanner;

    @Override
    public void customise(OpenAPI openApi) {
        addAnnotatedSchemas(openApi);
    }

    private void addAnnotatedSchemas(OpenAPI openAPI) {
        if (openAPI.getComponents() == null) {
            openAPI.setComponents(new Components());
        }
        if (openAPI.getComponents().getSchemas() == null) {
            openAPI.getComponents().setSchemas(new LinkedHashMap<>());
        }

        // Create a custom ModelConverters instance with the same configuration
        var customModelConverters = new ModelConverters();
        customModelConverters.addConverter(modelResolver);

        // Scan for classes with @ForceSwaggerSchema annotation
        var annotatedClasses = classScanner.getClassesAnnotatedWith(ForceSwaggerSchema.class);

        for (var clazz : annotatedClasses) {
            var schemas = customModelConverters.read(clazz);
            // Only add if not already present (to avoid overriding naturally discovered schemas)
            schemas.forEach((key, schema) -> {
                if (!openAPI.getComponents().getSchemas().containsKey(key)) {
                    openAPI.getComponents().getSchemas().put(key, schema);
                }
            });
        }
    }
}

