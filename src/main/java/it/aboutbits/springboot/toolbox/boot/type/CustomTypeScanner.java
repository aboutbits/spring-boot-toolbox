package it.aboutbits.springboot.toolbox.boot.type;

import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomTypeScanner {
    public static final String LIBRARY_BASE_PACKAGE_NAME = "it.aboutbits.springboot.toolbox";

    private String[] packageNamesToScan;
    private ClassScannerUtil.ClassScanner classScanner;

    public void setAdditionalTypePackages(String[] additionalTypePackages) {
        var tmp = new ArrayList<String>();
        tmp.add(LIBRARY_BASE_PACKAGE_NAME);
        tmp.addAll(Arrays.stream(additionalTypePackages)
                           .filter(item -> !item.isBlank())
                           .collect(Collectors.toSet()));

        this.packageNamesToScan = tmp.toArray(new String[0]);

        classScanner = ClassScannerUtil.getScannerForPackages(packageNamesToScan);
    }

    @PostConstruct
    public void init() {
        log.info("CustomTypeConfiguration enabled. Scanning: {}", Arrays.toString(packageNamesToScan));
    }

    @SuppressWarnings("rawtypes")
    public Set<Class<? extends CustomType>> findAllCustomTypeRecords() {
        return classScanner.getSubTypesOf(CustomType.class).stream()
                .filter(Record.class::isAssignableFrom)
                .collect(Collectors.toSet());
    }
}
