package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
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
