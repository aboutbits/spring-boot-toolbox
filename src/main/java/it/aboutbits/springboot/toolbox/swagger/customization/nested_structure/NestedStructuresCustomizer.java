package it.aboutbits.springboot.toolbox.swagger.customization.nested_structure;

import io.swagger.v3.oas.models.OpenAPI;
import it.aboutbits.springboot.toolbox.swagger.SwaggerMetaUtil;
import org.springdoc.core.customizers.OpenApiCustomizer;

/**
 * We assume that records that are inner classes of other records should be treated as substructure.
 * As such, we set the corresponding metadata for the FE code-generator.
 */
public class NestedStructuresCustomizer implements OpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        var components = openApi.getComponents();
        if (components != null && components.getSchemas() != null) {
            for (var schemaName : components.getSchemas().keySet()) {
                var schema = components.getSchemas().get(schemaName);
                if (schema != null) {
                    try {
                        // Assume that schema names correspond to fully qualified class names
                        var clazz = Class.forName(schemaName);
                        if (isChildRecordOfOtherRecord(clazz)) {
                            schema.setDescription(SwaggerMetaUtil.setIsNestedStructure(
                                    schema.getDescription(),
                                    true
                            ));
                        }

                    } catch (ClassNotFoundException ignored) {
                        // do nothing
                    }
                }
            }
        }
    }

    private static boolean isChildRecordOfOtherRecord(Class<?> clazz) {
        return clazz.getEnclosingClass() != null && clazz.isRecord() && clazz.getEnclosingClass().isRecord();
    }
}
