package it.aboutbits.springboot.toolbox._support;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ApplicationTest
@AutoConfigureMockMvc
public @interface HttpTest {

}
