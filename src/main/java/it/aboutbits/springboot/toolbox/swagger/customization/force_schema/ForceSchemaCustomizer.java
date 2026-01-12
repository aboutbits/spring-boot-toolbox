package it.aboutbits.springboot.toolbox.swagger.customization.force_schema;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.swagger.annotation.ForceSwaggerSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.Arrays;
import java.util.LinkedHashMap;

@RequiredArgsConstructor
@Slf4j
@NullMarked
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
            log.info("Forcing schema for class: {}", clazz.getName());

            if (clazz.isEnum()) {
                var fqn = clazz.getName();
                if (!openAPI.getComponents().getSchemas().containsKey(fqn)) {
                    var schema = new Schema<String>();
                    schema.name(fqn);
                    schema.setEnum(
                            Arrays.stream(clazz.getEnumConstants())
                                    .map(Object::toString)
                                    .toList()
                    );
                    schema.setType("string");
                    schema.addType("string");

                    openAPI.getComponents().getSchemas().put(fqn, schema);
                }
                continue;
            }

            var schemas = customModelConverters.read(clazz);
            // Only add if not already present (to avoid overriding naturally discovered schemas)
            schemas.forEach((key, schema) -> {
                if (!openAPI.getComponents().getSchemas().containsKey(key)) {
                    openAPI.getComponents().getSchemas().put(key, schema);
                }
            });
        }

        if (annotatedClasses.isEmpty()) {
            log.info("No classes with @ForceSwaggerSchema annotation found!");
            log.debug("Scanned packages: {}", String.join(", ", classScanner.getScannedPackages()));
        }
    }
}

