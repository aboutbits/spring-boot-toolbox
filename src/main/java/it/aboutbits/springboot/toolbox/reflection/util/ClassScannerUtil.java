package it.aboutbits.springboot.toolbox.reflection.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.jspecify.annotations.NullMarked;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

@NullMarked
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
        public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
            var classInfoList = clazz.isInterface()
                    ? scanResult.getClassesImplementing(clazz)
                    : scanResult.getSubclasses(clazz);
            return classInfoList.loadClasses()
                    .stream()
                    .map(item -> (Class<? extends T>) item)
                    .collect(Collectors.toSet());
        }

        public Set<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> clazz) {
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
