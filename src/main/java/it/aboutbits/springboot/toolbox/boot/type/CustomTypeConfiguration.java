package it.aboutbits.springboot.toolbox.boot.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.jackson.CustomTypeDeserializer;
import it.aboutbits.springboot.toolbox.type.jackson.CustomTypeSerializer;
import it.aboutbits.springboot.toolbox.type.mvc.CustomTypePropertyEditor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Set;

@Slf4j
@Configuration
public class CustomTypeConfiguration {
    @ControllerAdvice
    public static class CustomTypePropertyBinder {
        @SuppressWarnings("rawtypes")
        private final Set<Class<? extends CustomType>> types;

        public CustomTypePropertyBinder(CustomTypeScanner configuration) {
            this.types = configuration.findAllCustomTypeRecords();
        }

        @InitBinder
        public void initBinder(WebDataBinder binder) {
            for (var clazz : types) {
                binder.registerCustomEditor(clazz, new CustomTypePropertyEditor<>(clazz));
            }
        }
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(CustomTypeScanner configuration) {
        var types = configuration.findAllCustomTypeRecords();

        var deserializers = types.stream()
                .map(CustomTypeDeserializer::new)
                .toList()
                .toArray(new CustomTypeDeserializer[types.size()]);

        return builder -> builder
                .serializers(new CustomTypeSerializer())
                .deserializers(deserializers);
    }
}
