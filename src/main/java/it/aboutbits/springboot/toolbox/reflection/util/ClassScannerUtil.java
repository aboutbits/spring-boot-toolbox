package it.aboutbits.springboot.toolbox.reflection.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public final class ClassScannerUtil {
    private ClassScannerUtil() {
    }

    public static ClassScanner getScannerForPackages(String... packages) {
        return new ClassScanner(packages);
    }

    public static final class ClassScanner implements AutoCloseable {
        private final ScanResult scanResult;
        private final String[] packages;

        private ClassScanner(String... packages) {
            this.packages = packages;
            this.scanResult = new ClassGraph()
                    .enableAllInfo()
                    .acceptPackages(packages)
                    .scan();
        }

        public String[] getScannedPackages() {
            return packages;
        }

        @SuppressWarnings("unchecked")
        public <T> Set<Class<? extends T>> getSubTypesOf(@NonNull Class<T> clazz) {
            return scanResult.getClassesImplementing(clazz).loadClasses()
                    .stream()
                    .map(item -> (Class<? extends T>) item)
                    .collect(Collectors.toSet());
        }

        public Set<Class<?>> getClassesAnnotatedWith(@NonNull Class<? extends Annotation> clazz) {
            var result = scanResult.getClassesWithAnnotation(clazz);
            return result.stream().map(
                    ClassInfo::loadClass
            ).collect(Collectors.toSet());
        }

        @Override
        public void close() {
            scanResult.close();
        }
    }
}
