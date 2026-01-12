package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import com.fasterxml.jackson.databind.type.SimpleType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.customizers.PropertyCustomizer;

@NullMarked
public class CustomTypePropertyCustomizerForCodeGenerator implements PropertyCustomizer {
    @Override
    public Schema<?> customize(Schema property, AnnotatedType annotatedType) {
        var type = annotatedType.getType();

        if (type instanceof SimpleType simpleType) {
            var rawClass = simpleType.getRawClass();

            if (rawClass.getName().contains(".") && !rawClass.getName().startsWith("java.")) {
                property.setDescription(SwaggerMetaUtil.setOriginalTypeFqn(
                        property.getDescription(),
                        rawClass.getName()
                ));
            }
        }

        return property;
    }
}
