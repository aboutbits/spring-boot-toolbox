package it.aboutbits.springboot.toolbox.web.response;

import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PagedResponse<T>(
        @NonNull
        List<T> data,
        @NonNull
        MetaWithPagination meta
) {

    public static <T> PagedResponse<T> of(@NonNull Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                new MetaWithPagination(
                        new MetaWithPagination.Pagination(
                                page.getNumber(),
                                page.getSize(),
                                page.getTotalElements()
                        )
                )
        );
    }

    public static <I, O> PagedResponse<O> of(@NonNull Page<I> page, @NonNull Function<I, O> converter) {
        var mapped = page.map(converter);

        return new PagedResponse<>(
                mapped.getContent(),
                new MetaWithPagination(
                        new MetaWithPagination.Pagination(
                                mapped.getNumber(),
                                mapped.getSize(),
                                mapped.getTotalElements()
                        )
                )
        );
    }

    public record MetaWithPagination(
            @NonNull
            Pagination pagination
    ) {
        public record Pagination(long page, int size, long totalElements) {
            public static Pagination of(@NonNull Page<?> page) {
                return new Pagination(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements()
                );
            }
        }
    }
}
