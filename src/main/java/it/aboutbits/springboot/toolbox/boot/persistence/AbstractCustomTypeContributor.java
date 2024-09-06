package it.aboutbits.springboot.toolbox.boot.persistence;

import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import lombok.SneakyThrows;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.java.JavaType;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public abstract class AbstractCustomTypeContributor implements TypeContributor {
    private final ClassScannerUtil.ClassScanner classScanner;

    protected AbstractCustomTypeContributor(String... packageNames) {
        classScanner = ClassScannerUtil.getScannerForPackages(packageNames);
    }

    @SneakyThrows({InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class})
    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        var types = findAllRelevantTypes();

        for (var type : types) {
            typeContributions.contributeJavaType(
                    (JavaType<?>) type.getConstructors()[0].newInstance()
            );
        }

        classScanner.close();
    }

    @SuppressWarnings("rawtypes")
    private Set<Class<? extends AutoRegisteredJavaType>> findAllRelevantTypes() {
        return classScanner.getSubTypesOf(AutoRegisteredJavaType.class);
    }
}
