package it.aboutbits.springboot.toolbox.reflection.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.NonNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class ClassScannerUtil {
    private ClassScannerUtil() {
    }

    public static ClassScanner getScannerForPackages(String... packages) {
        return new ClassScanner(packages);
    }

    public static final class ClassScanner {
        private final ScanResult scanResult;

        private ClassScanner(String... packages) {
            try (var result = new ClassGraph()
                    .enableAllInfo()
                    .acceptPackages(packages)
                    .scan()) {
                this.scanResult = result;
            }
        }

        @SuppressWarnings("unchecked")
        public <T> Set<Class<? extends T>> getSubTypesOf(@NonNull Class<T> clazz) {
            return scanResult.getSubclasses(clazz).loadClasses()
                    .stream()
                    .map(item -> (Class<? extends T>) item)
                    .collect(Collectors.toSet());
        }

        public void close() {
            scanResult.close();
        }
    }
}
