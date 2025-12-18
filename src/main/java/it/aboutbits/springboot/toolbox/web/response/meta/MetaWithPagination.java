package it.aboutbits.springboot.toolbox.web.response.meta;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;

@Getter
@EqualsAndHashCode
@NullMarked
public class MetaWithPagination implements Meta {
    private final Pagination pagination;

    public MetaWithPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Pagination pagination() {
        return pagination;
    }

    public static MetaWithPagination of(Page<?> page) {
        return new MetaWithPagination(
                new MetaWithPagination.Pagination(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements()
                )
        );
    }

    public record Pagination(long page, int size, long totalElements) {
        public static Pagination of(Page<?> page) {
            return new Pagination(
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements()
            );
        }
    }
}
