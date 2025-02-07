package it.aboutbits.springboot.toolbox.web.response;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class PagedResponseTest {
    @Test
    void shouldReturnPagedResponseWithDataAndMeta_forSingleParameterOfMethod() {
        // given
        var content = List.of("item1", "item2", "item3");
        var page = new PageImpl<>(content, PageRequest.of(1, 3), 10);

        // when
        var pagedResponse = PagedResponse.of(page);

        // then
        assertThat(pagedResponse).isNotNull();
        assertThat(pagedResponse.data()).isEqualTo(content);
        assertThat(pagedResponse.meta().pagination().page()).isEqualTo(1);
        assertThat(pagedResponse.meta().pagination().size()).isEqualTo(3);
        assertThat(pagedResponse.meta().pagination().totalElements()).isEqualTo(10);
    }

    @Test
    void shouldReturnPagedResponseWithMappedDataAndMeta_forTwoParameterOfMethod() {
        // given
        var content = List.of(1, 2, 3);
        var page = new PageImpl<>(content, PageRequest.of(2, 5), 15);
        Function<Integer, String> converter = Object::toString;

        // when
        var pagedResponse = PagedResponse.of(page, converter);

        // then
        assertThat(pagedResponse).isNotNull();
        assertThat(pagedResponse.data()).isEqualTo(List.of("1", "2", "3"));
        assertThat(pagedResponse.meta().pagination().page()).isEqualTo(2);
        assertThat(pagedResponse.meta().pagination().size()).isEqualTo(5);
        assertThat(pagedResponse.meta().pagination().totalElements()).isEqualTo(15);
    }

    @Test
    void shouldHandleEmptyPageCorrectly() {
        // given
        var page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        // when
        var pagedResponse = PagedResponse.of(page);

        // then
        assertThat(pagedResponse).isNotNull();
        assertThat(pagedResponse.data()).isEmpty();
        assertThat(pagedResponse.meta().pagination().page()).isZero();
        assertThat(pagedResponse.meta().pagination().size()).isEqualTo(10);
        assertThat(pagedResponse.meta().pagination().totalElements()).isZero();
    }

    @Test
    void shouldHandlePageWithCustomConverterCorrectly() {
        // given
        var content = List.of(10, 20, 30);
        var page = new PageImpl<>(content, PageRequest.of(3, 3), 24);
        Function<Integer, String> converter = val -> "Value-" + val;

        // when
        var pagedResponse = PagedResponse.of(page, converter);

        // then
        assertThat(pagedResponse).isNotNull();
        assertThat(pagedResponse.data()).isEqualTo(List.of("Value-10", "Value-20", "Value-30"));
        assertThat(pagedResponse.meta().pagination().page()).isEqualTo(3);
        assertThat(pagedResponse.meta().pagination().size()).isEqualTo(3);
        assertThat(pagedResponse.meta().pagination().totalElements()).isEqualTo(24);
    }

    @Test
    void shouldHandleEmptyPageWithCustomConverterCorrectly() {
        // given
        var page = new PageImpl<Integer>(List.of(), PageRequest.of(2, 4), 0);
        Function<Integer, String> converter = val -> "Value-" + val;

        // when
        var pagedResponse = PagedResponse.of(page, converter);

        // then
        assertThat(pagedResponse).isNotNull();
        assertThat(pagedResponse.data()).isEmpty();
        assertThat(pagedResponse.meta().pagination().page()).isEqualTo(2);
        assertThat(pagedResponse.meta().pagination().size()).isEqualTo(4);
        assertThat(pagedResponse.meta().pagination().totalElements()).isZero();
    }
}
