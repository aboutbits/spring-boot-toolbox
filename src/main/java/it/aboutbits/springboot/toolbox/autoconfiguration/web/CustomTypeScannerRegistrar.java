package it.aboutbits.springboot.toolbox.autoconfiguration.web;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

public class CustomTypeScannerRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * We use this to register a new bean with actual configuration parameters coming from an annotation.
     * That way we can use @RegisterCustomTypesWithJacksonAndMvc and take the parameter "additionalTypePackages" to
     * scan packages.
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        var attributes = new AnnotationAttributes(
                Objects.requireNonNull(
                        metadata.getAnnotationAttributes(
                                RegisterCustomTypesWithJacksonAndMvc.class.getName()
                        )
                )
        );
        var value = attributes.getStringArray("additionalTypePackages");

        var builder = BeanDefinitionBuilder.genericBeanDefinition(CustomTypeScanner.class);
        builder.addPropertyValue("additionalTypePackages", value);
        registry.registerBeanDefinition("CustomTypeScanner", builder.getBeanDefinition());
    }
}
