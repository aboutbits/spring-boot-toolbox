package it.aboutbits.springboot.toolbox.swagger.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CustomModelResolver extends ModelResolver {
    public CustomModelResolver(ObjectMapper mapper) {
        super(mapper, new CustomTypeNameResolver());
    }

    public static class CustomTypeNameResolver extends TypeNameResolver {
        @Override
        protected String getNameOfClass(Class<?> cls) {
            return cls.getName();
        }
    }
}
