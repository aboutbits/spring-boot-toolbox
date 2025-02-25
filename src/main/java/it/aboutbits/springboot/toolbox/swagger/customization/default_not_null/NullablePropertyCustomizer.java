package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class NullablePropertyCustomizer implements PropertyCustomizer {
    static {
        /*
            We need this because the ModelResolver will only process these whitelisted annotations.
            We will then be able to manipulate each property based on the set annotations.
         */

        var list = new ArrayList<>(ModelResolver.NOT_NULL_ANNOTATIONS);
        list.add("Nullable");

        ModelResolver.NOT_NULL_ANNOTATIONS = list;
    }

    @Override
    public Schema<?> customize(Schema property, AnnotatedType annotatedType) {
        /*
            Mark the nullable ones as nullable.
         */

        if (annotatedType.getCtxAnnotations() != null && Arrays.stream(annotatedType.getCtxAnnotations())
                .anyMatch(a -> "Nullable".equals(a.annotationType().getSimpleName()))) {
            property.setTitle("NULLABLE");
        }

        return property;
    }
}
