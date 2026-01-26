package it.aboutbits.springboot.toolbox.swagger.customization.force_schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import it.aboutbits.springboot.toolbox.reflection.util.ClassScannerUtil;
import it.aboutbits.springboot.toolbox.swagger.annotation.ForceSwaggerSchema;
import it.aboutbits.springboot.toolbox.swagger.annotation.ForceSwaggerSchemaIgnore;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@NullUnmarked
class ForceSchemaCustomizerTest {

    @ForceSwaggerSchema(includeSubTypes = true)
    public static class AnnotatedClass {
        public String field;

        public static class InnerClass {
            public String innerField;
        }

        public record InnerRecord(String recordField) {
        }

        private static class PrivateInnerClass {
            public String privateField;
        }
    }

    @ForceSwaggerSchema(includeSubTypes = false)
    public static class AnnotatedClassWithoutSubTypes {
        public String field;

        public static class InnerClass {
            public String innerField;
        }
    }

    @ForceSwaggerSchema(includeSubTypes = true)
    public static class BaseClass {
    }

    public static class SubClass extends BaseClass {
        public static class SubInnerClass {
        }
    }

    @ForceSwaggerSchema(includeSubTypes = true)
    public static class ClassWithIgnoredMembers {
        public String field;

        @ForceSwaggerSchemaIgnore
        public static class IgnoredInnerClass {
            public String innerField;
        }

        public static class NotIgnoredInnerClass {
            public String innerField;
        }
    }

    @ForceSwaggerSchema(includeSubTypes = true)
    public static class BaseWithIgnoredSubClass {
    }

    @ForceSwaggerSchemaIgnore
    public static class IgnoredSubClass extends BaseWithIgnoredSubClass {
    }

    public static class NotIgnoredSubClass extends BaseWithIgnoredSubClass {
    }

    @ForceSwaggerSchema
    @ForceSwaggerSchemaIgnore
    public static class AnnotatedAndIgnored {
    }

    @Test
    void shouldIncludeSubTypesWhenEnabled() {
        var classScanner = mock(ClassScannerUtil.ClassScanner.class);
        when(classScanner.getClassesAnnotatedWith(ForceSwaggerSchema.class)).thenReturn(Set.of(
                AnnotatedClass.class,
                BaseClass.class
        ));
        when(classScanner.getSubTypesOf(BaseClass.class)).thenReturn(Set.of(SubClass.class));

        var modelResolver = new ModelResolver(new ObjectMapper());
        var customizer = new ForceSchemaCustomizer(modelResolver, classScanner);
        var openApi = new OpenAPI();

        customizer.customise(openApi);

        var schemas = openApi.getComponents().getSchemas();
        assertThat(schemas).containsKey(AnnotatedClass.class.getSimpleName());
        assertThat(schemas).containsKey(AnnotatedClass.InnerClass.class.getSimpleName());
        assertThat(schemas).containsKey(AnnotatedClass.InnerRecord.class.getSimpleName());
        assertThat(schemas).doesNotContainKey(AnnotatedClass.PrivateInnerClass.class.getSimpleName());

        assertThat(schemas).containsKey(BaseClass.class.getSimpleName());
        assertThat(schemas).containsKey(SubClass.class.getSimpleName());
        assertThat(schemas).containsKey(SubClass.SubInnerClass.class.getSimpleName());
    }

    @Test
    void shouldNotIncludeSubTypesWhenDisabled() {
        var classScanner = mock(ClassScannerUtil.ClassScanner.class);
        when(classScanner.getClassesAnnotatedWith(ForceSwaggerSchema.class)).thenReturn(Set.of(
                AnnotatedClassWithoutSubTypes.class));

        var modelResolver = new ModelResolver(new ObjectMapper());
        var customizer = new ForceSchemaCustomizer(modelResolver, classScanner);
        var openApi = new OpenAPI();

        customizer.customise(openApi);

        var schemas = openApi.getComponents().getSchemas();
        assertThat(schemas).containsKey(AnnotatedClassWithoutSubTypes.class.getSimpleName());
        assertThat(schemas).doesNotContainKey(AnnotatedClassWithoutSubTypes.InnerClass.class.getSimpleName());
    }

    @Test
    void shouldExcludeIgnoredClasses() {
        var classScanner = mock(ClassScannerUtil.ClassScanner.class);
        when(classScanner.getClassesAnnotatedWith(ForceSwaggerSchema.class)).thenReturn(Set.of(
                ClassWithIgnoredMembers.class,
                BaseWithIgnoredSubClass.class
        ));
        when(classScanner.getSubTypesOf(BaseWithIgnoredSubClass.class)).thenReturn(Set.of(
                IgnoredSubClass.class,
                NotIgnoredSubClass.class
        ));

        var modelResolver = new ModelResolver(new ObjectMapper());
        var customizer = new ForceSchemaCustomizer(modelResolver, classScanner);
        var openApi = new OpenAPI();

        customizer.customise(openApi);

        var schemas = openApi.getComponents().getSchemas();
        assertThat(schemas).containsKey(ClassWithIgnoredMembers.class.getSimpleName());
        assertThat(schemas).containsKey(ClassWithIgnoredMembers.NotIgnoredInnerClass.class.getSimpleName());
        assertThat(schemas).doesNotContainKey(ClassWithIgnoredMembers.IgnoredInnerClass.class.getSimpleName());

        assertThat(schemas).containsKey(BaseWithIgnoredSubClass.class.getSimpleName());
        assertThat(schemas).containsKey(NotIgnoredSubClass.class.getSimpleName());
        assertThat(schemas).doesNotContainKey(IgnoredSubClass.class.getSimpleName());
    }

    @Test
    void shouldExcludeClassWhenBothAnnotatedAndIgnored() {
        var classScanner = mock(ClassScannerUtil.ClassScanner.class);
        when(classScanner.getClassesAnnotatedWith(ForceSwaggerSchema.class)).thenReturn(Set.of(
                AnnotatedAndIgnored.class
        ));

        var modelResolver = new ModelResolver(new ObjectMapper());
        var customizer = new ForceSchemaCustomizer(modelResolver, classScanner);
        var openApi = new OpenAPI();

        customizer.customise(openApi);

        var schemas = openApi.getComponents().getSchemas();
        assertThat(schemas).doesNotContainKey(AnnotatedAndIgnored.class.getSimpleName());
    }
}
