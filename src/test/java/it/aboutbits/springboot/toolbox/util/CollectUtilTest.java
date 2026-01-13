package it.aboutbits.springboot.toolbox.util;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NullMarked
class CollectUtilTest {
    @Nested
    class CollectToSet {
        @Test
        @DisplayName("Should convert Collection to Set using mapper")
        void shouldConvertCollectionToSetUsingMapper() {
            // given
            var numbers = Arrays.asList(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(numbers, mapper);

            // then
            assertThat(result).containsExactlyInAnyOrder("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should convert Collection to Set using mapper")
        void shouldConvertCollectionToSetUsingMapperAndFilterNullValues() {
            // given
            var numbers = Arrays.asList("a", "b", "c", "d");
            var mapper = (NullableFunction<String, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(numbers, mapper);

            // then
            assertThat(result).containsExactlyInAnyOrder("a", "b", "c", "d");
        }

        @Test
        @DisplayName("Should handle empty Collection when converting to Set")
        void shouldHandleEmptyCollectionWhenConvertingToSet() {
            // given
            var emptyList = Collections.<Integer>emptyList();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(emptyList, mapper);

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should remove duplicates when converting Collection to Set")
        void shouldRemoveDuplicatesWhenConvertingCollectionToSet() {
            // given
            var numbersWithDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(numbersWithDuplicates, mapper);

            // then
            assertThat(result).containsExactlyInAnyOrder("1", "2", "3");
            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("Should convert Streamable to Set using mapper")
        void shouldConvertStreamableToSetUsingMapper() {
            // given
            var numbers = Streamable.of(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(numbers, mapper);

            // then
            assertThat(result).containsExactlyInAnyOrder("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should convert Streamable to Set using mapper")
        void shouldConvertStreamableToSetUsingMapperAndFilterNullValues() {
            // given
            var numbers = Streamable.of("a", "b", "c", "d");
            var mapper = (NullableFunction<String, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(numbers, mapper);

            // then
            assertThat(result).containsExactlyInAnyOrder("a", "b", "c", "d");
        }

        @Test
        @DisplayName("Should handle empty Streamable when converting to Set")
        void shouldHandleEmptyStreamableWhenConvertingToSet() {
            // given
            var emptyStreamable = Streamable.<Integer>empty();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(emptyStreamable, mapper);

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should convert Stream to Set using mapper")
        void shouldConvertStreamToSetUsingMapper() {
            // given
            var numbers = java.util.stream.Stream.of(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(numbers, mapper);

            // then
            assertThat(result).containsExactlyInAnyOrder("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should handle empty Stream when converting to Set")
        void shouldHandleEmptyStreamWhenConvertingToSet() {
            // given
            var emptyStream = java.util.stream.Stream.<Integer>empty();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(emptyStream, mapper);

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should remove duplicates when converting Stream to Set")
        void shouldRemoveDuplicatesWhenConvertingStreamToSet() {
            // given
            var numbersWithDuplicates = java.util.stream.Stream.of(1, 2, 2, 3, 3, 3);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToSet(numbersWithDuplicates, mapper);

            // then
            assertThat(result).containsExactlyInAnyOrder("1", "2", "3");
            assertEquals(3, result.size());
        }
    }

    @Nested
    class CollectToList {
        @Test
        @DisplayName("Should convert Collection to List using mapper")
        void shouldConvertCollectionToListUsingMapper() {
            // given
            var numbers = Arrays.asList(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(numbers, mapper);

            // then
            assertThat(result).containsExactly("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should handle empty Collection when converting to List")
        void shouldHandleEmptyCollectionWhenConvertingToList() {
            // given
            var emptyList = Collections.<Integer>emptyList();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(emptyList, mapper);

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should preserve duplicates when converting Collection to List")
        void shouldPreserveDuplicatesWhenConvertingCollectionToList() {
            // given
            var numbersWithDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(numbersWithDuplicates, mapper);

            // then
            assertThat(result).containsExactly("1", "2", "2", "3", "3", "3");
            assertEquals(6, result.size());
        }

        @Test
        @DisplayName("Should convert Streamable to List using mapper")
        void shouldConvertStreamableToListUsingMapper() {
            // given
            var numbers = Streamable.of(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(numbers, mapper);

            // then
            assertThat(result).containsExactly("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should handle empty Streamable when converting to List")
        void shouldHandleEmptyStreamableWhenConvertingToList() {
            // given
            var emptyStreamable = Streamable.<Integer>empty();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(emptyStreamable, mapper);

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should convert Stream to List using mapper")
        void shouldConvertStreamToListUsingMapper() {
            // given
            var numbers = java.util.stream.Stream.of(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(numbers, mapper);

            // then
            assertThat(result).containsExactly("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should handle empty Stream when converting to List")
        void shouldHandleEmptyStreamWhenConvertingToList() {
            // given
            var emptyStream = java.util.stream.Stream.<Integer>empty();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(emptyStream, mapper);

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should preserve duplicates when converting Stream to List")
        void shouldPreserveDuplicatesWhenConvertingStreamToList() {
            // given
            var numbersWithDuplicates = java.util.stream.Stream.of(1, 2, 2, 3, 3, 3);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var result = CollectUtil.collectToList(numbersWithDuplicates, mapper);

            // then
            assertThat(result).containsExactly("1", "2", "2", "3", "3", "3");
            assertEquals(6, result.size());
        }
    }

    @Nested
    class CollectToStream {
        @Test
        @DisplayName("Should convert Collection to Stream using mapper")
        void shouldConvertCollectionToStreamUsingMapper() {
            // given
            var numbers = Arrays.asList(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var resultStream = CollectUtil.collectToStream(numbers, mapper);
            var result = resultStream.toList();

            // then
            assertThat(result).containsExactly("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should handle empty Collection when converting to Stream")
        void shouldHandleEmptyCollectionWhenConvertingToStream() {
            // given
            var emptyList = Collections.<Integer>emptyList();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var resultStream = CollectUtil.collectToStream(emptyList, mapper);
            var result = resultStream.toList();

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should convert Streamable to Stream using mapper")
        void shouldConvertStreamableToStreamUsingMapper() {
            // given
            var numbers = Streamable.of(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var resultStream = CollectUtil.collectToStream(numbers, mapper);
            var result = resultStream.toList();

            // then
            assertThat(result).containsExactly("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should handle empty Streamable when converting to Stream")
        void shouldHandleEmptyStreamableWhenConvertingToStream() {
            // given
            var emptyStreamable = Streamable.<Integer>empty();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var resultStream = CollectUtil.collectToStream(emptyStreamable, mapper);
            var result = resultStream.toList();

            // then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should convert Stream to Stream using mapper")
        void shouldConvertStreamToStreamUsingMapper() {
            // given
            var numbers = java.util.stream.Stream.of(1, 2, 3, 4, 5);
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var resultStream = CollectUtil.collectToStream(numbers, mapper);
            var result = resultStream.toList();

            // then
            assertThat(result).containsExactly("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should handle empty Stream when converting to Stream")
        void shouldHandleEmptyStreamWhenConvertingToStream() {
            // given
            var emptyStream = java.util.stream.Stream.<Integer>empty();
            var mapper = (NullableFunction<Integer, String>) Object::toString;

            // when
            var resultStream = CollectUtil.collectToStream(emptyStream, mapper);
            var result = resultStream.toList();

            // then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class CollectToMap {
        @Test
        @DisplayName("Should convert Collection to Map using key and value mappers")
        void shouldConvertCollectionToMapUsingMappers() {
            // given
            var items = (Collection<String>) Arrays.asList("a", "bb", "ccc");

            // when
            var result = CollectUtil.collectToMap(items, String::length, Function.identity());

            // then
            assertThat(result)
                    .containsEntry(1, "a")
                    .containsEntry(2, "bb")
                    .containsEntry(3, "ccc")
                    .hasSize(3);
        }

        @Test
        @DisplayName("Should throw on duplicate keys according to Collectors.toMap default behavior")
        void shouldThrowOnDuplicateKeys() {
            // given
            var items = (Collection<String>) Arrays.asList("a", "b"); // both have length of 1

            // when / then
            assertThatIllegalStateException().isThrownBy(
                    () ->
                            CollectUtil.collectToMap(
                                    items,
                                    String::length,
                                    Function.identity()
                            )
            );
        }

        @Test
        @DisplayName("Should convert Streamable to Map using key and value mappers")
        void shouldConvertStreamableToMapUsingMappers() {
            // given
            var items = Streamable.of("x", "yy");

            // when
            var result = CollectUtil.collectToMap(items, String::length, Function.identity());

            // then
            assertThat(result)
                    .containsEntry(1, "x")
                    .containsEntry(2, "yy")
                    .hasSize(2);
        }

        @Test
        @DisplayName("Should convert Stream to Map using key and value mappers")
        void shouldConvertStreamToMapUsingMappers() {
            // given
            var items = Stream.of("m", "nn");

            // when
            var result = CollectUtil.collectToMap(items, String::length, Function.identity());

            // then
            assertThat(result)
                    .containsEntry(1, "m")
                    .containsEntry(2, "nn")
                    .hasSize(2);
        }

        @Test
        @DisplayName("Should return empty map for empty collection")
        void shouldReturnEmptyMapForEmptyCollection() {
            // given
            var items = Collections.<String>emptyList();

            // when
            var result = CollectUtil.collectToMap(items, String::length, Function.identity());

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty map for empty streamable")
        void shouldReturnEmptyMapForEmptyStreamable() {
            // given
            var items = Streamable.<String>empty();

            // when
            var result = CollectUtil.collectToMap(items, String::length, Function.identity());

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty map for empty stream")
        void shouldReturnEmptyMapForEmptyStream() {
            // given
            var items = Stream.<String>empty();

            // when
            var result = CollectUtil.collectToMap(items, String::length, Function.identity());

            // then
            assertThat(result).isEmpty();
        }
    }
}
