package it.aboutbits.springboot.toolbox.util;

import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import it.aboutbits.springboot.toolbox.type.identity.Identified;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class MapByIdentityTest {
    @Nested
    class Of {
        @Test
        void collection() {
            // given
            var id1 = new TestEntity.ID(1L);
            var id2 = new TestEntity.ID(2L);

            var entity1 = new TestEntity(id1);
            var entity2 = new TestEntity(id2);

            var entities = List.of(entity1, entity2);

            // when
            var result = MapByIdentity.of(entities);

            // then
            assertThat(result).hasSize(2)
                    .containsEntry(id1, entity1)
                    .containsEntry(id2, entity2);
        }

        @Test
        void stream() {
            // given
            var id1 = new TestEntity.ID(1L);
            var id2 = new TestEntity.ID(2L);

            var entity1 = new TestEntity(id1);
            var entity2 = new TestEntity(id2);

            var entityStream = Stream.of(entity1, entity2);

            // when
            var result = MapByIdentity.of(entityStream);

            // then
            assertThat(result).hasSize(2)
                    .containsEntry(id1, entity1)
                    .containsEntry(id2, entity2);
        }

        @Test
        void streamable() {
            // given
            var id1 = new TestEntity.ID(1L);
            var id2 = new TestEntity.ID(2L);

            var entity1 = new TestEntity(id1);
            var entity2 = new TestEntity(id2);

            var streamable = Streamable.of(entity1, entity2);

            // when
            var result = MapByIdentity.of(streamable);

            // then
            assertThat(result).hasSize(2)
                    .containsEntry(id1, entity1)
                    .containsEntry(id2, entity2);
        }

        @Test
        void emptyCollection() {
            // given
            var entities = List.<TestEntity>of();

            // when
            var result = MapByIdentity.of(entities);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void emptyStream() {
            // given
            var emptyStream = Stream.<TestEntity>empty();

            // when
            var result = MapByIdentity.of(emptyStream);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void emptyStreamable() {
            // given
            var emptyStreamable = Streamable.<TestEntity>empty();

            // when
            var result = MapByIdentity.of(emptyStreamable);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Getter
    private static class TestEntity implements Identified<TestEntity.ID> {
        private final ID id;

        TestEntity(ID id) {
            this.id = id;
        }

        public record ID(
                Long value
        ) implements EntityId<Long>, Comparable<ID> {
            @Override
            public String toString() {
                return String.valueOf(value());
            }

            @Override
            public int compareTo(ID other) {
                return Long.compare(this.value, other.value);
            }
        }
    }
}
