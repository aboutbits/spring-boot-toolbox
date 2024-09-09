package it.aboutbits.springboot.toolbox.autoconfiguration.web;

import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
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

        this.relevantTypes = findAllCustomTypeRecords(classScanner);
    }

    @SuppressWarnings("rawtypes")
    public static Set<Class<? extends CustomType>> findAllCustomTypeRecords(ClassScannerUtil.ClassScanner classScanner) {
        return classScanner.getSubTypesOf(CustomType.class).stream()
                .filter(Record.class::isAssignableFrom)
                .collect(Collectors.toSet());
    }
}
