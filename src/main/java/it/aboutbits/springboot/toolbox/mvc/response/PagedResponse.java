package it.aboutbits.springboot.toolbox.mvc.response;

import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(
        @NonNull
        List<T> data,
        @NonNull
        MetaWithPagination meta
) {

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
