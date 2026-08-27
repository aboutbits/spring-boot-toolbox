package it.aboutbits.springboot.toolbox.parameter;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class PageParameterTest {
    @Test
    void shouldUseDefaults_forNullPageAndSize() {
        // when
        var parameter = PageParameter.of(null, null);

        // then
        assertThat(parameter.page()).isZero();
        assertThat(parameter.size()).isEqualTo(PageParameter.DEFAULT_PAGE_SIZE());
    }

    @Test
    void shouldClampPageToZero_forNegativePage() {
        // when
        var parameter = PageParameter.of(-5, 10);

        // then
        assertThat(parameter.page()).isZero();
    }

    @Test
    void shouldClampSizeToMaximum_forSizeAboveMaximum() {
        // when
        var parameter = PageParameter.of(0, PageParameter.MAX_PAGE_SIZE() + 1);

        // then
        assertThat(parameter.size()).isEqualTo(PageParameter.MAX_PAGE_SIZE());
    }

    @Test
    void shouldClampSizeToOne_forZeroSize() {
        // when
        var parameter = PageParameter.of(0, 0);

        // then
        assertThat(parameter.size()).isEqualTo(1);
    }

    @Test
    void shouldClampSizeToOne_forNegativeSize() {
        // when
        var parameter = PageParameter.of(0, -10);

        // then
        assertThat(parameter.size()).isEqualTo(1);
    }

    @Test
    void shouldCreateValidPageRequest_forZeroSize() {
        // when
        var pageRequest = PageParameter.of(0, 0).toPageRequest();

        // then
        assertThat(pageRequest.getPageNumber()).isZero();
        assertThat(pageRequest.getPageSize()).isEqualTo(1);
    }
}
