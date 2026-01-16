package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.assertj.core.api.Assertions.assertThat;

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
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var subClassSchema = new Schema<Object>();
        subClassSchema.setName(SubClass.class.getName());
        subClassSchema.addProperty("baseField", new StringSchema());
        subClassSchema.addProperty("subField", new StringSchema());

        components.addSchemas(SubClass.class.getName(), subClassSchema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var required = subClassSchema.getRequired();

        assertThat(required).as("subField should be required").contains("subField");
        assertThat(required).as("baseField should NOT be required").doesNotContain("baseField");
    }

    @Test
    void shouldFindAnnotationOnGetter() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(MethodAnnotated.class.getName());
        schema.addProperty("annotatedGetter", new StringSchema());

        components.addSchemas(MethodAnnotated.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var required = schema.getRequired();
        assertThat(required).as("annotatedGetter should NOT be required").isNullOrEmpty();
    }

    @Test
    void shouldFindAnnotationOnDirectMethod() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(DirectMethodAnnotated.class.getName());
        schema.addProperty("directMethod", new StringSchema());

        components.addSchemas(DirectMethodAnnotated.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var required = schema.getRequired();
        assertThat(required).as("directMethod should NOT be required").isNullOrEmpty();
    }

    @Test
    void shouldHandleConcatenatedFqns() {
        // given
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

        // when
        customizer.customise(openApi);

        // then
        var required = schema.getRequired();
        assertThat(required).as("baseField should NOT be required even with concatenated FQN").isNullOrEmpty();
    }

    @Test
    void shouldNotThrowWhenFieldNotFound() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(SubClass.class.getName());
        schema.addProperty("nonExistent", new StringSchema());

        components.addSchemas(SubClass.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var required = schema.getRequired();
        assertThat(required).as("nonExistent field should be considered required if not found and not nullable")
                .contains("nonExistent");
    }

    @Test
    void shouldFindCustomNullableAnnotation() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(CustomNullableClass.class.getName());
        schema.addProperty("customNullableField", new StringSchema());

        components.addSchemas(CustomNullableClass.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var required = schema.getRequired();
        assertThat(required).as("customNullableField should NOT be required").isNullOrEmpty();
    }

    public static class Nest {
        @Retention(RetentionPolicy.RUNTIME)
        public @interface Nullable {
        }
    }

    public static class CustomNullableClass {
        @Nest.Nullable
        private String customNullableField;

        @SuppressWarnings("NullAway")
        public String getCustomNullableField() {
            return customNullableField;
        }
    }
}
