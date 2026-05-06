package it.aboutbits.springboot.toolbox.reflection.util;

import lombok.SneakyThrows;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NullMarked
public final class ClassScannerUtil {

    private ClassScannerUtil() {
    }

    public static ClassScanner getScannerForPackages(String... packages) {
        return new ClassScanner(packages);
    }

    public static final class ClassScanner {
        private final String[] packages;

        private ClassScanner(String... packages) {
            this.packages = packages;
        }

        public String[] getScannedPackages() {
            return packages;
        }

        @SuppressWarnings("unchecked")
        public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
            var scanner = createScanner();
            scanner.addIncludeFilter(new AssignableTypeFilter(clazz));
            return Arrays.stream(packages)
                    .flatMap(pkg -> scanner.findCandidateComponents(pkg).stream())
                    .map(bd -> (Class<? extends T>) loadClass(Objects.requireNonNull(bd.getBeanClassName())))
                    .filter(c -> !c.equals(clazz))
                    .collect(Collectors.toSet());
        }

        public Set<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> clazz) {
            var scanner = createScanner();
            scanner.addIncludeFilter(new AnnotationTypeFilter(clazz));
            return Arrays.stream(packages)
                    .flatMap(pkg -> scanner.findCandidateComponents(pkg).stream())
                    .map(bd -> loadClass(Objects.requireNonNull(bd.getBeanClassName())))
                    .collect(Collectors.toSet());
        }

        private static ClassPathScanningCandidateComponentProvider createScanner() {
            return new ClassPathScanningCandidateComponentProvider(false) {
                @Override
                protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                    return true;
                }
            };
        }

        @SneakyThrows(ClassNotFoundException.class)
        private static Class<?> loadClass(String className) {
            return Class.forName(className);
        }
    }
}
