package it.aboutbits.springboot.toolbox.boot.type;

import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.jackson.CustomTypeDeserializer;
import it.aboutbits.springboot.toolbox.type.jackson.CustomTypeSerializer;
import it.aboutbits.springboot.toolbox.type.mvc.CustomTypePropertyEditor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class CustomTypeConfiguration {
    public static final String LIBRARY_BASE_PACKAGE_NAME = "it.aboutbits.springboot.toolbox";

    private String[] packageNamesToScan;
    private ClassScannerUtil.ClassScanner classScanner;

    public void setAdditionalTypePackages(String[] additionalTypePackages) {
        var tmp = new ArrayList<String>();
        tmp.add(LIBRARY_BASE_PACKAGE_NAME);
        tmp.addAll(Arrays.asList(additionalTypePackages));

        this.packageNamesToScan = tmp.toArray(new String[0]);

        classScanner = ClassScannerUtil.getScannerForPackages(packageNamesToScan);
    }

    @PostConstruct
    public void init() {
        log.info("CustomTypeConfiguration enabled. Scanning: {}", Arrays.toString(packageNamesToScan));
    }

    @ControllerAdvice
    public class CustomTypePropertyBinder {
        @InitBinder
        public void initBinder(WebDataBinder binder) {
            var types = findAllCustomTypeRecords();
            for (var clazz : types) {
                binder.registerCustomEditor(clazz, new CustomTypePropertyEditor<>(clazz));
            }
        }
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        var types = findAllCustomTypeRecords();

        var deserializers = types.stream()
                .map(CustomTypeDeserializer::new)
                .toList()
                .toArray(new CustomTypeDeserializer[types.size()]);

        classScanner.close();

        return builder -> builder
                .serializers(new CustomTypeSerializer())
                .deserializers(deserializers);
    }

    @SuppressWarnings("rawtypes")
    private Set<Class<? extends CustomType>> findAllCustomTypeRecords() {
        return classScanner.getSubTypesOf(CustomType.class).stream()
                .filter(Record.class::isAssignableFrom)
                .collect(Collectors.toSet());
    }
}
