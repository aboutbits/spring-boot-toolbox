package it.aboutbits.springboot.toolbox.autoconfiguration.persistence;

import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import lombok.SneakyThrows;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.java.JavaType;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public abstract class AbstractCustomTypeContributor implements TypeContributor {
    @SuppressWarnings("rawtypes")
    private final Set<Class<? extends AutoRegisteredJavaType>> relevantTypes;

    protected AbstractCustomTypeContributor(String... packageNames) {
        var classScanner = ClassScannerUtil.getScannerForPackages(packageNames);

        this.relevantTypes = findAllRelevantTypes(classScanner);
    }

    @SneakyThrows({InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class})
    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        for (var type : relevantTypes) {
            typeContributions.contributeJavaType(
                    (JavaType<?>) type.getConstructors()[0].newInstance()
            );
        }
    }

    @SuppressWarnings("rawtypes")
    private static Set<Class<? extends AutoRegisteredJavaType>> findAllRelevantTypes(ClassScannerUtil.ClassScanner classScanner) {
        return classScanner.getSubTypesOf(AutoRegisteredJavaType.class);
    }
}
