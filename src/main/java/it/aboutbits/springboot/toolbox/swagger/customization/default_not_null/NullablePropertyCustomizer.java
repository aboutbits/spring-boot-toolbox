package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

import static it.aboutbits.springboot.toolbox.swagger.customization.default_not_null.NullableCustomizer.NULLABLE_MARKER;

/**
 * We treat all properties as not-nullable by default unless we specify @Nullable
 * This is the opposite of what swagger does by default.
 * <p>
 * This customizer adds a nullability maker. Later we process this to manipulate the required fields of a schema.
 */
@Component
@NullMarked
public class NullablePropertyCustomizer implements PropertyCustomizer {
    static {
        /*
            We need this because the ModelResolver will only retain annotations with a matching name
            that is contained in the list.
         */

        var list = new ArrayList<>(ModelResolver.NOT_NULL_ANNOTATIONS);
        list.add("Nullable");

        ModelResolver.NOT_NULL_ANNOTATIONS = list;
    }

    @Override
    public Schema<?> customize(Schema property, AnnotatedType annotatedType) {
        if (isAnnotatedAsNullable(annotatedType)) {
            property.setTitle(NULLABLE_MARKER);

            // refs do not retain other properties
            if (property.get$ref() != null) {
                property.set$ref(property.get$ref() + NULLABLE_MARKER);
            }
        }

        return property;
    }

    @SuppressWarnings("java:S1872") // Disabled: "Use an 'instanceof' comparison instead." We MUST match by name.
    private static boolean isAnnotatedAsNullable(AnnotatedType annotatedType) {
        if (annotatedType.getCtxAnnotations() == null) {
            return false;
        }

        return Arrays.stream(annotatedType.getCtxAnnotations())
                .anyMatch(
                        a -> "Nullable".equals(a.annotationType().getSimpleName())
                );
    }
}
