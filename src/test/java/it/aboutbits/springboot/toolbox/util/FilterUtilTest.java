package it.aboutbits.springboot.toolbox.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FilterUtilTest {
    @Nested
    class FilterToSet {
        @Test
        @DisplayName("Should filter Collection to Set using predicate")
        void shouldFilterCollectionToSetUsingPredicate() {
            // given
            var numbers = Arrays.asList(1, 2, 3, 4, 5);

            // when
            var result = FilterUtil.filterToSet(numbers, n -> n > 2);

            // then
            assertThat(result).containsExactlyInAnyOrder(3, 4, 5);
        }

        @Test
        @DisplayName("Should handle empty Collection when filtering to Set")
        void shouldHandleEmptyCollectionWhenFilteringToSet() {
            // given
            var emptyList = Collections.<Integer>emptyList();

            // when
            var result = FilterUtil.filterToSet(emptyList, n -> n > 2);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should remove duplicates when filtering Collection to Set")
        void shouldRemoveDuplicatesWhenFilteringCollectionToSet() {
            // given
            var numbersWithDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3);

            // when
            var result = FilterUtil.filterToSet(numbersWithDuplicates, n -> n >= 2);

            // then
            assertThat(result)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(2, 3);
        }

        @Test
        @DisplayName("Should filter Streamable to Set using predicate")
        void shouldFilterStreamableToSetUsingPredicate() {
            // given
            var numbers = Streamable.of(1, 2, 3, 4, 5);

            // when
            var result = FilterUtil.filterToSet(numbers, n -> n % 2 == 0);

            // then
            assertThat(result).containsExactlyInAnyOrder(2, 4);
        }

        @Test
        @DisplayName("Should handle empty Streamable when filtering to Set")
        void shouldHandleEmptyStreamableWhenFilteringToSet() {
            // given
            var emptyStreamable = Streamable.<Integer>empty();

            // when
            var result = FilterUtil.filterToSet(emptyStreamable, n -> n > 2);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should remove duplicates when filtering Stream to Set")
        void shouldRemoveDuplicatesWhenFilteringStreamToSet() {
            // given
            var numbersWithDuplicates = Stream.of(1, 2, 2, 3, 3, 3);

            // when
            var result = FilterUtil.filterToSet(numbersWithDuplicates, n -> n >= 2);

            // then
            assertThat(result)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(2, 3);
        }
    }

    @Nested
    class FilterToList {
        @Test
        @DisplayName("Should filter Collection to List using predicate")
        void shouldFilterCollectionToListUsingPredicate() {
            // given
            var numbers = Arrays.asList(1, 2, 3, 4, 5);

            // when
            var result = FilterUtil.filterToList(numbers, n -> n % 2 == 1);

            // then
            assertThat(result).containsExactly(1, 3, 5);
        }

        @Test
        @DisplayName("Should handle empty Collection when filtering to List")
        void shouldHandleEmptyCollectionWhenFilteringToList() {
            // given
            var emptyList = Collections.<Integer>emptyList();

            // when
            var result = FilterUtil.filterToList(emptyList, n -> n > 2);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should preserve duplicates when filtering Collection to List")
        void shouldPreserveDuplicatesWhenFilteringCollectionToList() {
            // given
            var numbersWithDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3);

            // when
            var result = FilterUtil.filterToList(numbersWithDuplicates, n -> n >= 2);

            // then
            assertThat(result)
                    .hasSize(5)
                    .containsExactly(2, 2, 3, 3, 3);
        }

        @Test
        @DisplayName("Should filter Streamable to List using predicate")
        void shouldFilterStreamableToListUsingPredicate() {
            // given
            var numbers = Streamable.of(1, 2, 3, 4, 5);

            // when
            var result = FilterUtil.filterToList(numbers, n -> n < 4);

            // then
            assertThat(result).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Should handle empty Streamable when filtering to List")
        void shouldHandleEmptyStreamableWhenFilteringToList() {
            // given
            var emptyStreamable = Streamable.<Integer>empty();

            // when
            var result = FilterUtil.filterToList(emptyStreamable, n -> n > 2);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should preserve duplicates when filtering Stream to List")
        void shouldPreserveDuplicatesWhenFilteringStreamToList() {
            // given
            var numbersWithDuplicates = Stream.of(1, 2, 2, 3, 3, 3);

            // when
            var result = FilterUtil.filterToList(numbersWithDuplicates, n -> n >= 2);

            // then
            assertThat(result)
                    .hasSize(5)
                    .containsExactly(2, 2, 3, 3, 3);
        }
    }

    @Nested
    class FilterToStream {
        @Test
        @DisplayName("Should filter Collection to Stream using predicate")
        void shouldFilterCollectionToStreamUsingPredicate() {
            // given
            var numbers = Arrays.asList(1, 2, 3, 4, 5);

            // when
            var resultStream = FilterUtil.filterToStream(numbers, n -> n >= 4);

            // then
            var result = resultStream.toList();
            assertThat(result).containsExactly(4, 5);
        }

        @Test
        @DisplayName("Should handle empty Collection when filtering to Stream")
        void shouldHandleEmptyCollectionWhenFilteringToStream() {
            // given
            var emptyList = Collections.<Integer>emptyList();

            // when
            var resultStream = FilterUtil.filterToStream(emptyList, n -> n > 2);

            // then
            assertThat(resultStream.toList()).isEmpty();
        }

        @Test
        @DisplayName("Should filter Streamable to Stream using predicate")
        void shouldFilterStreamableToStreamUsingPredicate() {
            // given
            var numbers = Streamable.of(1, 2, 3, 4, 5);

            // when
            var resultStream = FilterUtil.filterToStream(numbers, n -> n <= 2);

            // then
            var result = resultStream.toList();
            assertThat(result).containsExactly(1, 2);
        }

        @Test
        @DisplayName("Should handle empty Streamable when filtering to Stream")
        void shouldHandleEmptyStreamableWhenFilteringToStream() {
            // given
            var emptyStreamable = Streamable.<Integer>empty();

            // when
            var resultStream = FilterUtil.filterToStream(emptyStreamable, n -> n > 2);

            // then
            assertThat(resultStream.toList()).isEmpty();
        }

        @Test
        @DisplayName("Should filter Stream to Stream using predicate")
        void shouldFilterStreamToStreamUsingPredicate() {
            // given
            var numbers = Stream.of(1, 2, 3, 4, 5);

            // when
            var resultStream = FilterUtil.filterToStream(numbers, n -> n % 2 == 0);

            // then
            var result = resultStream.toList();
            assertThat(result).containsExactly(2, 4);
        }

        @Test
        @DisplayName("Should handle empty Stream when filtering to Stream")
        void shouldHandleEmptyStreamWhenFilteringToStream() {
            // given
            var emptyStream = Stream.<Integer>empty();

            // when
            var resultStream = FilterUtil.filterToStream(emptyStream, n -> n > 2);

            // then
            assertThat(resultStream.toList()).isEmpty();
        }
    }
}
