package it.aboutbits.springboot.toolbox.autoconfiguration.web;

import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.reflection.util.CustomTypeReflectionUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@NullMarked
public class CustomTypeScanner {
    private static final String LIBRARY_BASE_PACKAGE_NAME = "it.aboutbits.springboot.toolbox";

    @SuppressWarnings("rawtypes")
    private Set<Class<? extends CustomType>> relevantTypes = new HashSet<>();

    public void setAdditionalTypePackages(String[] additionalTypePackages) {
        var tmp = new ArrayList<String>();
        tmp.add(LIBRARY_BASE_PACKAGE_NAME);
        tmp.addAll(Arrays.stream(additionalTypePackages)
                           .filter(item -> !item.isBlank())
                           .collect(Collectors.toSet()));

        var packageNamesToScan = tmp.toArray(new String[0]);

        log.info("CustomTypeConfiguration enabled. Scanning: {}", Arrays.toString(packageNamesToScan));
        var classScanner = ClassScannerUtil.getScannerForPackages(packageNamesToScan);

        this.relevantTypes = findAllCustomTypes(classScanner);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Set<Class<? extends CustomType>> findAllCustomTypes(ClassScannerUtil.ClassScanner classScanner) {
        return classScanner.getSubTypesOf(CustomType.class).stream()
                .filter(item ->
                                !item.isInterface()
                                        && !item.isAnonymousClass()
                                        && !Modifier.isAbstract(item.getModifiers())
                                        && !item.isAnnotationPresent(DisableCustomTypeConfiguration.class)
                )
                .filter(item -> {
                    if (item.isEnum()) {
                        return true;
                    }

                    try {
                        var constructor = CustomTypeReflectionUtil.getCustomTypeConstructor((Class<? extends CustomType<?>>) item);
                        var wrappedType = constructor.getParameterTypes()[0];

                        if (!CustomTypeReflectionUtil.isSupportedWrappedType(wrappedType)) {
                            log.debug("CustomType {} has an unsupported wrapped type {} and will be ignored.", item.getName(), wrappedType.getName());
                            return false;
                        }

                        return true;
                    } catch (NoSuchMethodException _) {
                        log.debug("CustomType {} is missing the required constructor and will be ignored.", item.getName());

                        return false;
                    }
                })
                .collect(Collectors.toSet());
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DisableCustomTypeConfiguration {

    }
}
