package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
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

            // Check for nullable type parameters in collections/arrays/maps
            var annotatedType = getAnnotatedType(cls, propertyName);
            if (annotatedType != null) {
                addNullableDescriptionToContainedTypes(annotatedType, property);
            }
        });
    }

    @org.jspecify.annotations.Nullable
    private static AnnotatedType getAnnotatedType(Class<?> cls, String propertyName) {
        var currentClass = cls;
        while (currentClass != null) {
            try {
                var field = currentClass.getDeclaredField(propertyName);
                return field.getAnnotatedType();
            } catch (NoSuchFieldException _) {
            }

            for (var method : currentClass.getDeclaredMethods()) {
                if (method.getName().equals(propertyName)
                        || method.getName().equals("get" + capitalize(propertyName))
                        || method.getName().equals("is" + capitalize(propertyName))) {
                    return method.getAnnotatedReturnType();
                }
            }

            currentClass = currentClass.getSuperclass();
        }
        return null;
    }

    // Walks the type and its schema together: the element of a collection or array is described by the
    // schema's items, the value of a map by its additionalProperties
    private static void addNullableDescriptionToContainedTypes(AnnotatedType annotatedType, Schema<?> schema) {
        if (annotatedType instanceof AnnotatedArrayType arrayType) {
            addNullableDescriptionToContainedType(arrayType.getAnnotatedGenericComponentType(), schema.getItems());
        } else if (annotatedType instanceof AnnotatedParameterizedType parameterizedType
                && parameterizedType.getType() instanceof ParameterizedType type
                && type.getRawType() instanceof Class<?> rawClass) {
            var typeArguments = parameterizedType.getAnnotatedActualTypeArguments();
            // The element of Collection<E> and the value of Map<K, V> are both the last type argument
            var containedType = typeArguments[typeArguments.length - 1];
            if (Collection.class.isAssignableFrom(rawClass)) {
                addNullableDescriptionToContainedType(containedType, schema.getItems());
            } else if (Map.class.isAssignableFrom(rawClass)
                    && schema.getAdditionalProperties() instanceof Schema<?> valueSchema) {
                addNullableDescriptionToContainedType(containedType, valueSchema);
            }
        }
    }

    private static void addNullableDescriptionToContainedType(
            AnnotatedType containedType,
            @org.jspecify.annotations.Nullable Schema<?> containedSchema
    ) {
        if (containedSchema == null) {
            return; // Schema structure doesn't match the type
        }
        if (hasNullableAnnotation(containedType)) {
            containedSchema.setDescription(SwaggerMetaUtil.setIsNullable(
                    containedSchema.getDescription(),
                    true
            ));
        }
        addNullableDescriptionToContainedTypes(containedType, containedSchema);
    }

    private static boolean hasNullableAnnotation(AnnotatedType annotatedType) {
        for (var annotation : annotatedType.getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("Nullable")) {
                return true;
            }
        }
        return false;
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
        for (var annotation : annotatedType.getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("Nullable")) {
                return true;
            }
        }
        for (var annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Nullable")) {
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
