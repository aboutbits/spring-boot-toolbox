package it.aboutbits.springboot.toolbox.boot.type;

import it.aboutbits.springboot.toolbox.boot.type.swagger.GenericSingleValueWrapperModelConverter;
import it.aboutbits.springboot.toolbox.boot.type.swagger.GenericSingleValueWrapperPropertyCustomizer;
import it.aboutbits.springboot.toolbox.boot.type.swagger.IdentityModelConverter;
import it.aboutbits.springboot.toolbox.boot.type.swagger.IdentityPropertyCustomizer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        GenericSingleValueWrapperModelConverter.class,
        GenericSingleValueWrapperPropertyCustomizer.class,
        IdentityModelConverter.class,
        IdentityPropertyCustomizer.class,
        CustomTypeConfigurationRegistrar.class
})
public @interface RegisterCustomTypes {
    String[] additionalTypePackages() default "";
}
