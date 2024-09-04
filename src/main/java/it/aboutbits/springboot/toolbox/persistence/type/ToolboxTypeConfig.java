package it.aboutbits.springboot.toolbox.persistence.type;

import org.hibernate.annotations.JavaTypeRegistrations;

@JavaTypeRegistrations({
        /*@JavaTypeRegistration(
                javaType = ID.class,
                descriptorClass = IdJavaType.class
        )*/
})
public abstract class ToolboxTypeConfig {
}
