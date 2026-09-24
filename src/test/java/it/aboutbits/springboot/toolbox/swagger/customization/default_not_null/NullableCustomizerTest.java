package it.aboutbits.springboot.toolbox.swagger.customization.default_not_null;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@NullUnmarked
class NullableCustomizerTest {

    public static class BaseClass {
        @Nullable
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

        @Nullable
        public String getAnnotatedGetter() {
            return annotatedGetter;
        }
    }

    public static class DirectMethodAnnotated {
        private String directMethod;

        @Nullable
        public String directMethod() {
            return directMethod;
        }
    }

    // Test classes for nullable type parameters in collections
    public static class ListWithNullableElements {
        private List<@Nullable String> items;

        public List<@Nullable String> getItems() {
            return items;
        }
    }

    public static class SetWithNullableElements {
        private Set<@Nullable String> items;

        public Set<@Nullable String> getItems() {
            return items;
        }
    }

    public static class NestedListWithNullableElements {
        private List<List<@Nullable String>> nestedItems;

        public List<List<@Nullable String>> getNestedItems() {
            return nestedItems;
        }
    }

    public static class ArrayWithNullableElements {
        private @Nullable String[] items;

        public @Nullable String[] getItems() {
            return items;
        }
    }

    public static class ListWithNonNullableElements {
        private List<String> items;

        public List<String> getItems() {
            return items;
        }
    }

    // Test classes for nullable values in maps
    public static class MapWithNullableValues {
        private Map<String, @Nullable String> values;

        public Map<String, @Nullable String> getValues() {
            return values;
        }
    }

    public static class ReferencedModel {
    }

    public static class MapWithNullableReferenceValues {
        private Map<String, @Nullable ReferencedModel> values;

        public Map<String, @Nullable ReferencedModel> getValues() {
            return values;
        }
    }

    public static class MapOfListsWithNullableElements {
        private Map<String, List<@Nullable String>> values;

        public Map<String, List<@Nullable String>> getValues() {
            return values;
        }
    }

    public static class ListOfMapsWithNullableValues {
        private List<Map<String, @Nullable String>> items;

        public List<Map<String, @Nullable String>> getItems() {
            return items;
        }
    }

    public static class MapWithNonNullableValues {
        private Map<String, String> values;

        public Map<String, String> getValues() {
            return values;
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
        var concatenatedFqn = SubClass.class.getName() + "It.aboutbits.something";
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

    @Test
    void shouldAddDescriptionForListWithNullableElements() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(ListWithNullableElements.class.getName());
        var itemsProperty = new ArraySchema();
        itemsProperty.setItems(new StringSchema());
        schema.addProperty("items", itemsProperty);

        components.addSchemas(ListWithNullableElements.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = (ArraySchema) schema.getProperties().get("items");
        assertThat(property.getItems().getDescription()).as("description should indicate nullable elements")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldAddDescriptionForSetWithNullableElements() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(SetWithNullableElements.class.getName());
        var itemsProperty = new ArraySchema();
        itemsProperty.setItems(new StringSchema());
        schema.addProperty("items", itemsProperty);

        components.addSchemas(SetWithNullableElements.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = (ArraySchema) schema.getProperties().get("items");
        assertThat(property.getItems().getDescription()).as("description should indicate nullable elements")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldAddDescriptionForNestedListWithNullableElements() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(NestedListWithNullableElements.class.getName());
        var nestedItemsProperty = new ArraySchema();
        var innerArray = new ArraySchema();
        innerArray.setItems(new StringSchema());
        nestedItemsProperty.setItems(innerArray);
        schema.addProperty("nestedItems", nestedItemsProperty);

        components.addSchemas(NestedListWithNullableElements.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = (ArraySchema) schema.getProperties().get("nestedItems");
        var innerItems = (ArraySchema) property.getItems();
        assertThat(innerItems.getItems().getDescription()).as("description should indicate nested nullable elements")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldAddDescriptionForArrayWithNullableElements() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(ArrayWithNullableElements.class.getName());
        var itemsProperty = new ArraySchema();
        itemsProperty.setItems(new StringSchema());
        schema.addProperty("items", itemsProperty);

        components.addSchemas(ArrayWithNullableElements.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = (ArraySchema) schema.getProperties().get("items");
        assertThat(property.getItems().getDescription()).as("description should indicate nullable elements")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldNotAddDescriptionForListWithNonNullableElements() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(ListWithNonNullableElements.class.getName());
        var itemsProperty = new ArraySchema();
        itemsProperty.setItems(new StringSchema());
        schema.addProperty("items", itemsProperty);

        components.addSchemas(ListWithNonNullableElements.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = schema.getProperties().get("items");
        assertThat(property.getDescription()).as("description should be null for non-nullable elements")
                .isNull();
    }

    @Test
    void shouldAddDescriptionForMapWithNullableValues() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(MapWithNullableValues.class.getName());
        var valuesProperty = new MapSchema();
        valuesProperty.setAdditionalProperties(new StringSchema());
        schema.addProperty("values", valuesProperty);

        components.addSchemas(MapWithNullableValues.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = schema.getProperties().get("values");
        var valueSchema = (Schema<?>) property.getAdditionalProperties();
        assertThat(valueSchema.getDescription()).as("description should indicate nullable values")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldAddDescriptionForMapWithNullableReferenceValues() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(MapWithNullableReferenceValues.class.getName());
        var valuesProperty = new MapSchema();
        valuesProperty.setAdditionalProperties(new Schema<Object>().$ref(ReferencedModel.class.getName()));
        schema.addProperty("values", valuesProperty);

        components.addSchemas(MapWithNullableReferenceValues.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = schema.getProperties().get("values");
        var valueSchema = (Schema<?>) property.getAdditionalProperties();
        assertThat(valueSchema.getDescription()).as("description should indicate nullable reference values")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldAddDescriptionForMapOfListsWithNullableElements() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(MapOfListsWithNullableElements.class.getName());
        var valuesProperty = new MapSchema();
        var valueArray = new ArraySchema();
        valueArray.setItems(new StringSchema());
        valuesProperty.setAdditionalProperties(valueArray);
        schema.addProperty("values", valuesProperty);

        components.addSchemas(MapOfListsWithNullableElements.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = schema.getProperties().get("values");
        var valueSchema = (ArraySchema) property.getAdditionalProperties();
        assertThat(valueSchema.getItems().getDescription()).as("description should indicate nullable elements of the values")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldAddDescriptionForListOfMapsWithNullableValues() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(ListOfMapsWithNullableValues.class.getName());
        var itemsProperty = new ArraySchema();
        var itemMap = new MapSchema();
        itemMap.setAdditionalProperties(new StringSchema());
        itemsProperty.setItems(itemMap);
        schema.addProperty("items", itemsProperty);

        components.addSchemas(ListOfMapsWithNullableValues.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = (ArraySchema) schema.getProperties().get("items");
        var valueSchema = (Schema<?>) property.getItems().getAdditionalProperties();
        assertThat(valueSchema.getDescription()).as("description should indicate nullable values of the elements")
                .isEqualTo("{\"isNullable\":true}");
    }

    @Test
    void shouldNotAddDescriptionForMapWithNonNullableValues() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(MapWithNonNullableValues.class.getName());
        var valuesProperty = new MapSchema();
        valuesProperty.setAdditionalProperties(new StringSchema());
        schema.addProperty("values", valuesProperty);

        components.addSchemas(MapWithNonNullableValues.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = schema.getProperties().get("values");
        var valueSchema = (Schema<?>) property.getAdditionalProperties();
        assertThat(valueSchema.getDescription()).as("description should be null for non-nullable values")
                .isNull();
    }

    @Test
    void shouldAppendToExistingDescription() {
        // given
        var customizer = new NullableCustomizer();
        var openApi = new OpenAPI();
        var components = new Components();

        var schema = new Schema<Object>();
        schema.setName(ListWithNullableElements.class.getName());
        var itemsProperty = new ArraySchema();
        var itemsSchema = new StringSchema();
        itemsSchema.setDescription("{\"isCustomType\":true}");
        itemsProperty.setItems(itemsSchema);
        schema.addProperty("items", itemsProperty);

        components.addSchemas(ListWithNullableElements.class.getName(), schema);
        openApi.setComponents(components);

        // when
        customizer.customise(openApi);

        // then
        var property = (ArraySchema) schema.getProperties().get("items");
        assertThat(property.getItems().getDescription()).as(
                        "description should append nullable info to existing description")
                .isEqualTo("{\"isCustomType\":true,\"isNullable\":true}");
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
