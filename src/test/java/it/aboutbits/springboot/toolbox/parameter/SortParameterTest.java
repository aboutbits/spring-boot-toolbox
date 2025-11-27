package it.aboutbits.springboot.toolbox.parameter;

import it.aboutbits.springboot.toolbox.persistence.SortMappings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static it.aboutbits.springboot.toolbox.persistence.SortMappings.map;
import static org.assertj.core.api.Assertions.assertThat;

class SortParameterTest {
    @SuppressWarnings("java:S115")
    private enum ESort implements SortParameter.Definition {
        Property1,
        Property2,
        property3,
        Property4,
        property5,
        propertyOldName1,
        propertyOldName2
    }

    private static final SortMappings<ESort> SORT_MAPPINGS = SortMappings.of(
            map(ESort.Property1, "Property1"),
            map(ESort.Property2, "Property2"),
            map(ESort.property3, "property3"),
            map(ESort.Property4, "Property4"),
            map(ESort.property5, "property5"),
            map(ESort.propertyOldName1, "property.new.name1"),
            map(ESort.propertyOldName2, "property.new.name2")
    );

    @Nested
    class Unsorted {
        @Test
        void unsorted() {
            var item = SortParameter.unsorted();

            assertThat(item.sortFields()).isEmpty();
        }
    }

    @Nested
    class By {
        @Test
        void byMultipleDefinitions_shouldCreateSortParameter() {
            // when
            var item = SortParameter.by(ESort.Property1, ESort.property3, ESort.Property2);

            // then
            assertThat(item.sortFields()).hasSize(3);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(0).direction()).isEqualTo(Sort.Direction.ASC);
            assertThat(item.sortFields().get(0).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
            assertThat(item.sortFields().get(1).property()).isEqualTo("property3");
            assertThat(item.sortFields().get(1).direction()).isEqualTo(Sort.Direction.ASC);
            assertThat(item.sortFields().get(1).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
            assertThat(item.sortFields().get(2).property()).isEqualTo("Property2");
            assertThat(item.sortFields().get(2).direction()).isEqualTo(Sort.Direction.ASC);
            assertThat(item.sortFields().get(2).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
        }

        @Test
        void bySingleDefinition_shouldCreateSortParameter() {
            // when
            var item = SortParameter.by(ESort.Property1);

            // then
            assertThat(item.sortFields()).hasSize(1);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(0).direction()).isEqualTo(Sort.Direction.ASC);
            assertThat(item.sortFields().get(0).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
        }

        @Test
        void byDefinitionAndDirection_shouldCreateSortParameter() {
            // when
            var item = SortParameter.by(ESort.Property1, Sort.Direction.DESC);

            // then
            assertThat(item.sortFields()).hasSize(1);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(0).direction()).isEqualTo(Sort.Direction.DESC);
            assertThat(item.sortFields().get(0).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
        }

        @Test
        void byDefinitionDirectionAndNullHandling_shouldCreateSortParameter() {
            // when
            var item = SortParameter.by(ESort.Property1, Sort.Direction.DESC, Sort.NullHandling.NULLS_FIRST);

            // then
            assertThat(item.sortFields()).hasSize(1);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(0).direction()).isEqualTo(Sort.Direction.DESC);
            assertThat(item.sortFields().get(0).nullHandling()).isEqualTo(Sort.NullHandling.NULLS_FIRST);
        }
    }

    @Nested
    class And {
        @Test
        void andMultipleDefinitions_shouldAddSortFields() {
            // given
            var item = SortParameter.by(ESort.Property1);

            // when
            item.and(ESort.property3, ESort.Property2);

            // then
            assertThat(item.sortFields()).hasSize(3);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(1).property()).isEqualTo("property3");
            assertThat(item.sortFields().get(1).direction()).isEqualTo(Sort.Direction.ASC);
            assertThat(item.sortFields().get(1).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
            assertThat(item.sortFields().get(2).property()).isEqualTo("Property2");
            assertThat(item.sortFields().get(2).direction()).isEqualTo(Sort.Direction.ASC);
            assertThat(item.sortFields().get(2).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
        }

        @Test
        void andDefinitionAndDirection_shouldAddSortField() {
            // given
            var item = SortParameter.by(ESort.Property1);

            // when
            item.and(ESort.property3, Sort.Direction.DESC);

            // then
            assertThat(item.sortFields()).hasSize(2);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(1).property()).isEqualTo("property3");
            assertThat(item.sortFields().get(1).direction()).isEqualTo(Sort.Direction.DESC);
            assertThat(item.sortFields().get(1).nullHandling()).isEqualTo(Sort.NullHandling.NATIVE);
        }

        @Test
        void andDefinitionDirectionAndNullHandling_shouldAddSortField() {
            // given
            var item = SortParameter.by(ESort.Property1);

            // when
            item.and(ESort.property3, Sort.Direction.DESC, Sort.NullHandling.NULLS_LAST);

            // then
            assertThat(item.sortFields()).hasSize(2);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(1).property()).isEqualTo("property3");
            assertThat(item.sortFields().get(1).direction()).isEqualTo(Sort.Direction.DESC);
            assertThat(item.sortFields().get(1).nullHandling()).isEqualTo(Sort.NullHandling.NULLS_LAST);
        }

        @Test
        void andChaining_shouldAddMultipleSortFields() {
            // given
            var item = SortParameter.by(ESort.Property1)
                    .and(ESort.property3, Sort.Direction.DESC)
                    .and(ESort.Property2, Sort.Direction.ASC, Sort.NullHandling.NULLS_FIRST);

            // then
            assertThat(item.sortFields()).hasSize(3);
            assertThat(item.sortFields().get(0).property()).isEqualTo("Property1");
            assertThat(item.sortFields().get(1).property()).isEqualTo("property3");
            assertThat(item.sortFields().get(1).direction()).isEqualTo(Sort.Direction.DESC);
            assertThat(item.sortFields().get(2).property()).isEqualTo("Property2");
            assertThat(item.sortFields().get(2).direction()).isEqualTo(Sort.Direction.ASC);
            assertThat(item.sortFields().get(2).nullHandling()).isEqualTo(Sort.NullHandling.NULLS_FIRST);
        }
    }

    @Nested
    class Or {
        @Test
        void orWithEmptySortFields_shouldReturnFallback() {
            // given
            var item = SortParameter.<ESort>unsorted();
            var fallback = SortParameter.by(ESort.Property1);

            // when
            var result = item.or(fallback);

            // then
            assertThat(result).isEqualTo(fallback);
            assertThat(result.sortFields()).hasSize(1);
            assertThat(result.sortFields().get(0).property()).isEqualTo("Property1");
        }

        @Test
        void orWithNullSortFields_shouldReturnFallback() {
            // given
            var item = new SortParameter<ESort>(null);
            var fallback = SortParameter.by(ESort.Property1);

            // when
            var result = item.or(fallback);

            // then
            assertThat(result).isEqualTo(fallback);
            assertThat(result.sortFields()).hasSize(1);
            assertThat(result.sortFields().get(0).property()).isEqualTo("Property1");
        }

        @Test
        void orWithNonEmptySortFields_shouldReturnThis() {
            // given
            var item = SortParameter.by(ESort.property3);
            var fallback = SortParameter.by(ESort.Property1);

            // when
            var result = item.or(fallback);

            // then
            assertThat(result).isEqualTo(item);
            assertThat(result.sortFields()).hasSize(1);
            assertThat(result.sortFields().get(0).property()).isEqualTo("property3");
        }
    }

    @Nested
    class BuildSortWithoutDefault {
        @Test
        void nullFieldList_shouldReturnUnsorted() {
            // given
            var item = new SortParameter<ESort>(null);

            var expected = SortParameter.<ESort>unsorted().buildSortWithoutDefault(SORT_MAPPINGS);

            // when
            var sort = item.buildSortWithoutDefault(SORT_MAPPINGS);

            // then
            assertThat(sort).isEqualTo(Sort.unsorted());
            assertThat(sort).isEqualTo(expected);
        }

        @Test
        void emptySortFieldList_shouldReturnUnsorted() {
            // given
            var item = new SortParameter<ESort>(Collections.emptyList());

            var expected = SortParameter.<ESort>unsorted().buildSortWithoutDefault(SORT_MAPPINGS);

            // when
            var sort = item.buildSortWithoutDefault(SORT_MAPPINGS);

            // then
            assertThat(sort).isEqualTo(Sort.unsorted());
            assertThat(sort).isEqualTo(expected);
        }
    }

    @Nested
    class BuildSort {
        @Test
        void nullFieldList_shouldReturnSortedById() {
            // given
            var item = new SortParameter<ESort>(null);

            var expected = SortParameter.<ESort>unsorted().buildSort(SORT_MAPPINGS);

            // when
            var sort = item.buildSort(SORT_MAPPINGS);

            // then
            assertThat(sort).isEqualTo(Sort.by("id"));
            assertThat(sort).isEqualTo(expected);
        }

        @Test
        void emptySortFieldList_shouldSortedById() {
            // given
            var item = new SortParameter<ESort>(Collections.emptyList());

            var expected = SortParameter.<ESort>unsorted().buildSort(SORT_MAPPINGS);

            // when
            var sort = item.buildSort(SORT_MAPPINGS);

            // then
            assertThat(sort).isEqualTo(Sort.by("id"));
            assertThat(sort).isEqualTo(expected);
        }

        @Test
        void sortFieldListWithoutMapping_shouldReturnSort() {
            // given
            var item = new SortParameter<ESort>(List.of(
                    new SortParameter.SortField("property5", Sort.Direction.ASC, Sort.NullHandling.NULLS_FIRST),
                    new SortParameter.SortField("Property2", Sort.Direction.DESC, Sort.NullHandling.NATIVE),
                    new SortParameter.SortField("Property1", Sort.Direction.ASC, Sort.NullHandling.NULLS_FIRST),
                    new SortParameter.SortField("property3", Sort.Direction.DESC, Sort.NullHandling.NATIVE),
                    new SortParameter.SortField("Property4", Sort.Direction.DESC, Sort.NullHandling.NULLS_LAST)
            ));

            // when
            var sort = item.buildSort(SORT_MAPPINGS);

            // then
            assertThat(sort).isEqualTo(Sort.by(
                    new Sort.Order(Sort.Direction.ASC, "property5", Sort.NullHandling.NULLS_FIRST),
                    new Sort.Order(Sort.Direction.DESC, "Property2", Sort.NullHandling.NATIVE),
                    new Sort.Order(Sort.Direction.ASC, "Property1", Sort.NullHandling.NULLS_FIRST),
                    new Sort.Order(Sort.Direction.DESC, "property3", Sort.NullHandling.NATIVE),
                    new Sort.Order(Sort.Direction.DESC, "Property4", Sort.NullHandling.NULLS_LAST),
                    new Sort.Order(Sort.Direction.ASC, "id")
            ));
        }

        @Test
        void sortFieldListWithMapping_shouldReturnSort() {
            var item = new SortParameter<ESort>(List.of(
                    new SortParameter.SortField("propertyOldName1", Sort.Direction.ASC, Sort.NullHandling.NATIVE),
                    new SortParameter.SortField("propertyOldName2", Sort.Direction.DESC, Sort.NullHandling.NULLS_LAST),
                    new SortParameter.SortField(
                            "propertyNoMapping",
                            Sort.Direction.DESC,
                            Sort.NullHandling.NULLS_FIRST
                    )
            ));

            // when
            var sort = item.buildSort(SORT_MAPPINGS);

            // then
            assertThat(sort).isEqualTo(Sort.by(
                    new Sort.Order(Sort.Direction.ASC, "property.new.name1", Sort.NullHandling.NATIVE),
                    new Sort.Order(Sort.Direction.DESC, "property.new.name2", Sort.NullHandling.NULLS_LAST),
                    new Sort.Order(Sort.Direction.ASC, "id")
            ));
        }

        @Test
        void sortFieldListWithNoMapping_shouldOmit() {
            var item = new SortParameter<ESort>(List.of(
                    new SortParameter.SortField(
                            "propertyNoMapping",
                            Sort.Direction.DESC,
                            Sort.NullHandling.NULLS_FIRST
                    )
            ));

            // when
            var sort = item.buildSort(SORT_MAPPINGS);

            // then
            assertThat(sort).isEqualTo(Sort.by("id"));
        }
    }
}
