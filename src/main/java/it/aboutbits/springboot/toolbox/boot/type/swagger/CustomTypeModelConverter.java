package it.aboutbits.springboot.toolbox.boot.type.swagger;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.persistence.identity.EntityId;
import it.aboutbits.springboot.toolbox.reflection.util.RecordReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Iterator;

@Component
public class CustomTypeModelConverter implements ModelConverter {

    @Override
    public Schema<?> resolve(
            AnnotatedType annotatedType,
            ModelConverterContext context,
            Iterator<ModelConverter> chain
    ) {

        var type = annotatedType.getType();

        if (type instanceof Class<?> clazz && CustomType.class.isAssignableFrom(clazz) && !EntityId.class.isAssignableFrom(
                clazz)) {
            var constructor = RecordReflectionUtil.getCanonicalConstructor(clazz);
            var wrappedType = constructor.getParameters()[0].getType();

            if (Short.class.isAssignableFrom(wrappedType)) {
                return context.resolve(new AnnotatedType(Short.TYPE));
            }
            if (Integer.class.isAssignableFrom(wrappedType)) {
                return context.resolve(new AnnotatedType(Integer.TYPE));
            }
            if (Long.class.isAssignableFrom(wrappedType)) {
                return context.resolve(new AnnotatedType(Long.TYPE));
            }
            if (Float.class.isAssignableFrom(wrappedType)) {
                return context.resolve(new AnnotatedType(Float.TYPE));
            }
            if (Double.class.isAssignableFrom(wrappedType)) {
                return context.resolve(new AnnotatedType(Double.TYPE));
            }
            if (BigDecimal.class.isAssignableFrom(wrappedType)) {
                return context.resolve(new AnnotatedType(Double.TYPE));
            }
            if (String.class.isAssignableFrom(wrappedType)) {
                return context.resolve(new AnnotatedType(String.class));
            }
        }

        return chain.hasNext() ? chain.next().resolve(annotatedType, context, chain) : null;
    }
}
