package it.aboutbits.springboot.toolbox.util;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@NullMarked
class NullUtilTest {

    @Test
    void nonNullOrFail_givenNonNullValue_shouldReturnValue() {
        // given
        var value = "test";

        // when
        var result = NullUtil.nonNullOrFail(value);

        // then
        assertThat(result).isEqualTo(value);
    }

    @Test
    void nonNullOrFail_givenNullValue_shouldThrowIllegalStateException() {
        // given
        String value = null;

        // then
        assertThatIllegalStateException()
                .isThrownBy(() -> /* when */ NullUtil.nonNullOrFail(value))
                .withMessage("Required non-null value, but got null.");
    }

    @Test
    void nonNullOrFailWithMessage_givenNonNullValue_shouldReturnValue() {
        // given
        var value = "test";
        var message = "Custom error message";

        // when
        var result = NullUtil.nonNullOrFail(value, message);

        // then
        assertThat(result).isEqualTo(value);
    }

    @Test
    void nonNullOrFailWithMessage_givenNullValue_shouldThrowIllegalStateExceptionWithCustomMessage() {
        // given
        String value = null;
        var message = "Custom error message";

        // then
        assertThatIllegalStateException()
                .isThrownBy(() -> /* when */ NullUtil.nonNullOrFail(value, message))
                .withMessage(message);
    }

    @Test
    void nonNullOrFail_givenNonNullObject_shouldReturnSameInstance() {
        // given
        var value = new Object();

        // when
        var result = NullUtil.nonNullOrFail(value);

        // then
        assertThat(result).isSameAs(value);
    }

    @Test
    void nonNullOrFailWithMessage_givenNonNullObject_shouldReturnSameInstance() {
        // given
        var value = new Object();
        var message = "Custom error message";

        // when
        var result = NullUtil.nonNullOrFail(value, message);

        // then
        assertThat(result).isSameAs(value);
    }
}
