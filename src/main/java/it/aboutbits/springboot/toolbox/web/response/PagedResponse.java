package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.MetaWithPagination;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public final class PagedResponse<T> extends PagedResponseWithMeta<T, MetaWithPagination> {

    public PagedResponse(@NonNull List<T> data, @NonNull MetaWithPagination meta) {
        super(data, meta);
    }

    public static <T> PagedResponse<T> of(@NonNull Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                MetaWithPagination.of(page)
        );
    }

    public static <I, O> PagedResponse<O> of(@NonNull Page<I> page, @NonNull Function<I, O> converter) {
        var mapped = page.map(converter);

        return new PagedResponse<>(
                mapped.getContent(),
                MetaWithPagination.of(page)
        );
    }
}
