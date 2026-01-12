package it.aboutbits.springboot.toolbox;

import it.aboutbits.springboot.toolbox.autoconfiguration.web.RegisterCustomTypesWithJacksonAndMvc;
import org.jspecify.annotations.NullMarked;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
@RegisterCustomTypesWithJacksonAndMvc
@NullMarked
public class TestApp {
    static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }
}
