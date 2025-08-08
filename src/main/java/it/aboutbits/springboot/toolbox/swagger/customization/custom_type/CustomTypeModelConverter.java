package it.aboutbits.springboot.toolbox.swagger.customization.custom_type;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import it.aboutbits.springboot.toolbox.reflection.util.CustomTypeReflectionUtil;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.UUID;

public class CustomTypeModelConverter implements ModelConverter {

    @Override
    @SneakyThrows(NoSuchMethodException.class)
    public Schema<?> resolve(
            AnnotatedType annotatedType,
            ModelConverterContext context,
            Iterator<ModelConverter> chain
    ) {

        var type = annotatedType.getType();

        if (type instanceof Class<?> clazz) {
            Schema<?> result = null;
            if (CustomType.class.isAssignableFrom(clazz)) {
                @SuppressWarnings("unchecked")
                var wrappedType = CustomTypeReflectionUtil.getWrappedType(
                        (Class<? extends CustomType<?>>) clazz
                );

                if (Boolean.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Boolean.TYPE));
                }
                if (Byte.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Byte.TYPE));
                }
                if (Short.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Short.TYPE));
                }
                if (Integer.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Integer.TYPE));
                }
                if (Long.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Long.TYPE));
                }
                if (BigInteger.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Long.TYPE));
                }
                if (Float.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Float.TYPE));
                }
                if (Double.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Double.TYPE));
                }
                if (BigDecimal.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Double.TYPE));
                }
                if (ScaledBigDecimal.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Double.TYPE));
                }
                if (String.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(String.class));
                }
                if (Character.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(Character.TYPE));
                }
                if (UUID.class.isAssignableFrom(wrappedType)) {
                    result = context.resolve(new AnnotatedType(UUID.class));
                }

                if (result != null) {
                    var isIdentity = EntityId.class.isAssignableFrom(clazz);

                    var description = SwaggerMetaUtil.setOriginalTypeFqn(
                            result.getDescription(),
                            ((Class<?>) type).getName()
                    );
                    description = SwaggerMetaUtil.setIsIdentity(description, isIdentity);
                    result.setDescription(description);

                    return result;
                }
            }
        }

        return chain.hasNext() ? chain.next().resolve(annotatedType, context, chain) : null;
    }
}
