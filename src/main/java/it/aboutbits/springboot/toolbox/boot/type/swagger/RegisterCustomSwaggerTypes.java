package it.aboutbits.springboot.toolbox.boot.type.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        CustomTypeModelConverter.class,
        CustomTypePropertyCustomizer.class,
        EntityIdModelConverter.class,
        EntityIdPropertyCustomizer.class
})
public @interface RegisterCustomSwaggerTypes {
}
