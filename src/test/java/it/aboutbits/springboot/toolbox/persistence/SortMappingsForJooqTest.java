package it.aboutbits.springboot.toolbox.persistence;

import it.aboutbits.springboot.toolbox.parameter.SortParameter;
import org.jooq.impl.DSL;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class SortMappingsForJooqTest {
    private enum ESort implements SortParameter.Definition {
        Property1,
        Property2
    }

    @Test
    void map_shouldReturnMappingWithFieldObject() {
        var field = DSL.field("my_column");
        var mapping = SortMappingsForJooq.map(ESort.Property1, field);

        assertThat(mapping.property()).isEqualTo(ESort.Property1);
        assertThat(mapping.column()).isEqualTo(field);
    }

    @Test
    void of_shouldCreateSortMappingsForJooq() {
        var field1 = DSL.field("col1");
        var mappings = SortMappingsForJooq.of(
                SortMappingsForJooq.map(ESort.Property1, field1)
        );

        assertThat(mappings).isInstanceOf(SortMappingsForJooq.class);
        assertThat(mappings.get(ESort.Property1)).isEqualTo(field1);
    }

    @Test
    void buildSpringSortWithJooqFields() {
        var mappings = SortMappingsForJooq.of(
                SortMappingsForJooq.map(ESort.Property1, DSL.field("my_col"))
        );
        var sortParameter = SortParameter.by(ESort.Property1, Sort.Direction.DESC);

        var result = sortParameter.buildSort(mappings);

        var order = result.getOrderFor("my_col");
        assertThat(order).isNotNull();
        assertThat(Objects.requireNonNull(order).getDirection()).isEqualTo(Sort.Direction.DESC);
    }
}
