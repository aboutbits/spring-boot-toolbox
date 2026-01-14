package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NullUnmarked
class NullableCustomizerTest {

    public static class BaseClass {
        @org.springframework.lang.Nullable
        private String baseField;

        public String getBaseField() {
            return baseField;
        }
    }

    public static class SubClass extends BaseClass {
        private String subField;

        public String getSubField() {
            return subField;
        }
    }

    public static class MethodAnnotated {
        private String annotatedGetter;

        @jakarta.annotation.Nullable
        public String getAnnotatedGetter() {
            return annotatedGetter;
        }
    }

    public static class DirectMethodAnnotated {
        private String directMethod;

        @org.jspecify.annotations.Nullable
        public String directMethod() {
            return directMethod;
        }
    }

    @Test
    void shouldFindFieldInSuperClass() {
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var subClassSchema = new Schema<Object>();
        subClassSchema.setName(SubClass.class.getName());
        subClassSchema.addProperty("baseField", new StringSchema());
        subClassSchema.addProperty("subField", new StringSchema());

        components.addSchemas(SubClass.class.getName(), subClassSchema);
        openApi.setComponents(components);

        assertDoesNotThrow(() -> customizer.customise(openApi));

        List<String> required = subClassSchema.getRequired();
        assertTrue(required != null && required.contains("subField"), "subField should be required");
        assertTrue(required == null || !required.contains("baseField"), "baseField should NOT be required");
    }

    @Test
    void shouldFindAnnotationOnGetter() {
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(MethodAnnotated.class.getName());
        schema.addProperty("annotatedGetter", new StringSchema());

        components.addSchemas(MethodAnnotated.class.getName(), schema);
        openApi.setComponents(components);

        assertDoesNotThrow(() -> customizer.customise(openApi));

        List<String> required = schema.getRequired();
        assertTrue(required == null || !required.contains("annotatedGetter"), "annotatedGetter should NOT be required");
    }

    @Test
    void shouldFindAnnotationOnDirectMethod() {
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(DirectMethodAnnotated.class.getName());
        schema.addProperty("directMethod", new StringSchema());

        components.addSchemas(DirectMethodAnnotated.class.getName(), schema);
        openApi.setComponents(components);

        assertDoesNotThrow(() -> customizer.customise(openApi));

        List<String> required = schema.getRequired();
        assertTrue(required == null || !required.contains("directMethod"), "directMethod should NOT be required");
    }

    @Test
    void shouldHandleConcatenatedFqns() {
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        // Simulating the concatenated FQN pattern described in the issue
        var concatenatedFqn = SubClass.class.getName() + "Com.finstral.something";
        schema.setName(concatenatedFqn);
        schema.addProperty("baseField", new StringSchema());

        components.addSchemas(concatenatedFqn, schema);
        openApi.setComponents(components);

        assertDoesNotThrow(() -> customizer.customise(openApi));

        List<String> required = schema.getRequired();
        assertTrue(
                required == null || !required.contains("baseField"),
                "baseField should NOT be required even with concatenated FQN"
        );
    }

    @Test
    void shouldNotThrowWhenFieldNotFound() {
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(SubClass.class.getName());
        schema.addProperty("nonExistent", new StringSchema());

        components.addSchemas(SubClass.class.getName(), schema);
        openApi.setComponents(components);

        assertDoesNotThrow(() -> customizer.customise(openApi));

        List<String> required = schema.getRequired();
        assertTrue(
                required != null && required.contains("nonExistent"),
                "nonExistent field should be considered required if not found and not nullable"
        );
    }
}
