package it.aboutbits.springboot.toolbox.exception;

import jakarta.validation.Path;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class ConstraintViolationExceptionBuilderTest {
    @Test
    void causedBy_shouldParseThePropertyPath() {
        var exception = ConstraintViolationExceptionBuilder.constraintViolation()
                .causedBy("items[1].name", "must not be blank")
                .create();

        assertThat(exception.getConstraintViolations()).singleElement().satisfies(violation -> {
            assertThat(violation.getMessage()).isEqualTo("must not be blank");
            assertThat(violation.getPropertyPath()).hasToString("items[1].name");

            var nodes = StreamSupport.stream(violation.getPropertyPath().spliterator(), false).toList();
            assertThat(nodes).extracting(Path.Node::getName).containsExactly("items", "name");
            assertThat(nodes).extracting(Path.Node::getIndex).containsExactly(null, 1);
        });
    }

    @Test
    void hasCauses_withoutCauses_shouldReturnFalse() {
        assertThat(ConstraintViolationExceptionBuilder.constraintViolation().hasCauses()).isFalse();
    }
}
