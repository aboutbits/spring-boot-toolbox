package it.aboutbits.springboot.toolbox.autoconfiguration.web;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({CustomTypeScannerRegistrar.class, CustomTypeConfiguration.class})
public @interface RegisterCustomTypesWithJacksonAndMvc {
    String[] additionalTypePackages() default "";
}
