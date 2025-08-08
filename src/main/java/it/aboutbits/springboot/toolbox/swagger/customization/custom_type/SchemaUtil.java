package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import java.util.Optional;

final class SchemaUtil {
    private SchemaUtil() {
    }

    static Optional<Class<?>> getClassFromSchemaReference(String schemaRef) {
        try {
            var className = schemaRef.replace("#/components/schemas/", "");
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
