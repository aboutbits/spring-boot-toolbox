package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.MetaWithPagination;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@NullMarked
public final class PagedResponse<T> extends PagedResponseWithMeta<T, MetaWithPagination> {

    public PagedResponse(List<T> data, MetaWithPagination meta) {
        super(data, meta);
    }

    public static <T> PagedResponse<T> of(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                MetaWithPagination.of(page)
        );
    }

    public static <I, O> PagedResponse<O> of(Page<I> page, Function<I, O> converter) {
        var mapped = page.map(converter);

        return new PagedResponse<>(
                mapped.getContent(),
                MetaWithPagination.of(page)
        );
    }
}
