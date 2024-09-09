package it.aboutbits.springboot.toolbox.boot.type;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({CustomTypeConfigurationRegistrar.class, CustomTypeConfiguration.class})
public @interface RegisterCustomTypes {
    String[] additionalTypePackages() default "";
}