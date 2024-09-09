package it.aboutbits.springboot.toolbox;

import it.aboutbits.springboot.toolbox.boot.type.RegisterCustomTypes;
import it.aboutbits.springboot.toolbox.boot.type.swagger.RegisterCustomSwaggerTypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
@RegisterCustomTypes
@RegisterCustomSwaggerTypes
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

}
