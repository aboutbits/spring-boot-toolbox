package it.aboutbits.springboot.toolbox.autoconfiguration.swagger;

import it.aboutbits.springboot.toolbox.swagger.CustomTypeModelConverter;
import it.aboutbits.springboot.toolbox.swagger.CustomTypePropertyCustomizer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        CustomTypeModelConverter.class,
        CustomTypePropertyCustomizer.class
})
public @interface RegisterCustomTypesWithSwagger {
}
