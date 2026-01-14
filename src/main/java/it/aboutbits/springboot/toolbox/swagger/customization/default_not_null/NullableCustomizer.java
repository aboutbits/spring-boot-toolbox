package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Map;

@NullMarked
public class NullableCustomizer implements OpenApiCustomizer {
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
                        processProperties(schema.getName(), properties, requiredProperties);
                    }
                    if (schema.getAllOf() != null) {
                        schema.getAllOf().forEach(allOfSchema -> {
                            var allOfSchemaTyped = (Schema<?>) allOfSchema;
                            if (allOfSchemaTyped.getProperties() != null) {
                                var properties = allOfSchemaTyped.getProperties();
                                processProperties(schema.getName(), properties, requiredProperties);
                            }
                        });
                    }
                    schema.setRequired(requiredProperties);
                });
    }

    @SuppressWarnings("rawtypes")
    private static void processProperties(
            String modelFqn,
            Map<String, Schema> properties,
            ArrayList<String> requiredProperties
    ) {
        var cls = loadClass(modelFqn);
        if (cls == null) {
            return;
        }

        properties.forEach((propertyName, property) -> {
            var isNullable = isNullable(cls, propertyName);

            if (!isNullable) {
                requiredProperties.add(propertyName);
            } else {
                requiredProperties.remove(propertyName);
            }
        });
    }

    @org.jspecify.annotations.Nullable
    private static Class<?> loadClass(String fqn) {
        try {
            return Class.forName(fqn);
        } catch (ClassNotFoundException _) {
            // if this does not work, we probably have a parameterized type where the fqn is concatenated
        }

        var lastDotIndex = -1;
        for (var i = 0; i <= fqn.length(); i++) {
            if (i == fqn.length() || fqn.charAt(i) == '.') {
                var fullPart = fqn.substring(lastDotIndex + 1, i);
                if (!fullPart.isEmpty() && Character.isUpperCase(fullPart.charAt(0))) {
                    // Try the full part first
                    var baseFqn = fqn.substring(0, i);
                    try {
                        return Class.forName(baseFqn);
                    } catch (ClassNotFoundException _) {
                    }

                    // Try stripping capitalized segments from the end of the part
                    // e.g., LabelAndDescriptionChoiceCom -> try LabelAndDescriptionChoice, then LabelAndDescription, etc.
                    for (var j = fullPart.length() - 1; j > 0; j--) {
                        if (Character.isUpperCase(fullPart.charAt(j))) {
                            var strippedPart = fullPart.substring(0, j);
                            var candidateFqn = fqn.substring(0, lastDotIndex + 1) + strippedPart;
                            try {
                                return Class.forName(candidateFqn);
                            } catch (ClassNotFoundException _) {
                            }
                        }
                    }
                }
                lastDotIndex = i;
            }
        }
        return null;
    }

    private static boolean isNullable(Class<?> cls, String propertyName) {
        var currentClass = cls;
        while (currentClass != null) {
            try {
                var field = currentClass.getDeclaredField(propertyName);
                if (isNullable(field.getAnnotatedType(), field.getAnnotations())) {
                    return true;
                }
            } catch (NoSuchFieldException _) {
            }

            for (var method : currentClass.getDeclaredMethods()) {
                if (method.getName().equals(propertyName)
                        || method.getName().equals("get" + capitalize(propertyName))
                        || method.getName().equals("is" + capitalize(propertyName))) {
                    if (isNullable(method.getAnnotatedReturnType(), method.getAnnotations())) {
                        return true;
                    }
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return false;
    }

    private static boolean isNullable(
            AnnotatedType annotatedType,
            Annotation[] annotations
    ) {
        if (annotatedType.isAnnotationPresent(org.jspecify.annotations.Nullable.class)) {
            return true;
        }
        for (var annotation : annotations) {
            var name = annotation.annotationType().getName();
            if (name.equals("org.springframework.lang.Nullable") || name.equals("jakarta.annotation.Nullable")) {
                return true;
            }
        }
        return false;
    }

    private static String capitalize(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
