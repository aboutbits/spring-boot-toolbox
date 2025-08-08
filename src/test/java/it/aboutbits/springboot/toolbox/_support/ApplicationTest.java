package it.aboutbits.springboot.toolbox._support;

import it.aboutbits.springboot.toolbox._support.persistence.WithPostgres;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@WithPostgres
public @interface ApplicationTest {

}
