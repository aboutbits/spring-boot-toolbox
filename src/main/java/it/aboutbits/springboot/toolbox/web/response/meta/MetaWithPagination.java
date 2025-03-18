package it.aboutbits.springboot.toolbox.web.response.meta;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.domain.Page;

@Getter
@EqualsAndHashCode
public class MetaWithPagination implements Meta {
    private final Pagination pagination;

    public MetaWithPagination(@NonNull Pagination pagination) {
        this.pagination = pagination;
    }

    public Pagination pagination() {
        return pagination;
    }

    public static MetaWithPagination of(@NonNull Page<?> page) {
        return new MetaWithPagination(
                new MetaWithPagination.Pagination(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements()
                )
        );
    }

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
