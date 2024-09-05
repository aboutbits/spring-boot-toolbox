package it.aboutbits.springboot.toolbox.boot.persistence;

import lombok.SneakyThrows;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.java.JavaType;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public abstract class AbstractCustomTypeContributor implements TypeContributor {
    private final Reflections reflections;

    protected AbstractCustomTypeContributor(String... packageNames) {
        var packageToScan = new ConfigurationBuilder().forPackages(packageNames);
        reflections = new Reflections(packageToScan);
    }

    @SneakyThrows({InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class})
    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        var types = findAllTypes();

        for (var type : types) {
            typeContributions.contributeJavaType(
                    (JavaType<?>) type.getConstructors()[0].newInstance()
            );
        }
    }

    @SuppressWarnings("rawtypes")
    private Set<Class<? extends AutoRegisteredJavaType>> findAllTypes() {
        return reflections.getSubTypesOf(AutoRegisteredJavaType.class);
    }
}
