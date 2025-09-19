package it.aboutbits.springboot.toolbox.autoconfiguration.web;

import it.aboutbits.springboot.toolbox.jackson.CustomTypeDeserializer;
import it.aboutbits.springboot.toolbox.jackson.CustomTypeSerializer;
import it.aboutbits.springboot.toolbox.jackson.EntityIdDeserializer;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.web.CustomTypePropertyEditor;
import it.aboutbits.springboot.toolbox.web.EntityIdPropertyEditor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Set;

@Configuration
public class CustomTypeConfiguration {
    @ControllerAdvice
    public static class CustomTypePropertyBinder {
        @SuppressWarnings("rawtypes")
        private final Set<Class<? extends CustomType>> types;

        public CustomTypePropertyBinder(CustomTypeScanner configuration) {
            this.types = configuration.getRelevantTypes();
        }

        @InitBinder
        public void initBinder(WebDataBinder binder) {
            for (var clazz : types) {
                binder.registerCustomEditor(clazz, new CustomTypePropertyEditor<>(clazz));
            }
            binder.registerCustomEditor(EntityId.class, new EntityIdPropertyEditor());
        }
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(CustomTypeScanner configuration) {
        var types = configuration.getRelevantTypes();

        var deserializers = types.stream()
                .map(CustomTypeDeserializer::new)
                .toList()
                .toArray(new CustomTypeDeserializer[types.size()]);


        return builder -> builder
                .serializers(new CustomTypeSerializer())
                .deserializers(deserializers)
                .deserializerByType(EntityId.class, new EntityIdDeserializer());
    }

}
