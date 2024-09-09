package it.aboutbits.springboot.toolbox.boot.type;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

public class CustomTypeConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        var attributes = new AnnotationAttributes(
                Objects.requireNonNull(
                        metadata.getAnnotationAttributes(
                                RegisterCustomTypes.class.getName()
                        )
                )
        );
        var value = attributes.getStringArray("additionalTypePackages");

        var builder = BeanDefinitionBuilder.genericBeanDefinition(CustomTypeScanner.class);
        builder.addPropertyValue("additionalTypePackages", value);
        registry.registerBeanDefinition("CustomTypeScanner", builder.getBeanDefinition());
    }
}
