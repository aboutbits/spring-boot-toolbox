package it.aboutbits.springboot.toolbox.swagger.customization.force_schema;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.swagger.annotation.ForceSwaggerSchema;
import it.aboutbits.springboot.toolbox.swagger.annotation.ForceSwaggerSchemaIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
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

        var classesToProcess = new HashSet<Class<?>>();
        for (var clazz : annotatedClasses) {
            if (clazz.isAnnotationPresent(ForceSwaggerSchemaIgnore.class)) {
                continue;
            }
            classesToProcess.add(clazz);
            var annotation = clazz.getAnnotation(ForceSwaggerSchema.class);
            if (annotation != null && annotation.includeSubTypes()) {
                var subTypes = classScanner.getSubTypesOf(clazz);
                for (var subType : subTypes) {
                    if (!subType.isAnnotationPresent(ForceSwaggerSchemaIgnore.class)) {
                        classesToProcess.add(subType);
                    }
                }

                collectPublicNestedTypes(clazz, classesToProcess);
                for (var subType : subTypes) {
                    if (classesToProcess.contains(subType)) {
                        collectPublicNestedTypes(subType, classesToProcess);
                    }
                }
            }
        }

        for (var clazz : classesToProcess) {
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

    private void collectPublicNestedTypes(Class<?> clazz, Set<Class<?>> collected) {
        for (var nested : clazz.getDeclaredClasses()) {
            if (Modifier.isPublic(nested.getModifiers()) && !nested.isAnnotationPresent(ForceSwaggerSchemaIgnore.class)) {
                if (collected.add(nested)) {
                    collectPublicNestedTypes(nested, collected);
                }
            }
        }
    }
}

