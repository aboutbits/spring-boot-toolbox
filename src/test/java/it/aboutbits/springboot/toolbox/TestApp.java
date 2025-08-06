package it.aboutbits.springboot.toolbox;

import it.aboutbits.springboot.toolbox.autoconfiguration.web.RegisterCustomTypesWithJacksonAndMvc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
@RegisterCustomTypesWithJacksonAndMvc
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

}
