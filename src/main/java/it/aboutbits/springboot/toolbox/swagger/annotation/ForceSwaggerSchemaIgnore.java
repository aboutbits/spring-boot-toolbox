package it.aboutbits.springboot.toolbox.swagger.annotation;

import org.jspecify.annotations.NullMarked;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes that should be ignored when forcing Swagger schemas.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@NullMarked
public @interface ForceSwaggerSchemaIgnore {
}
