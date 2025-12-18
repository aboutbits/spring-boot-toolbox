package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.MetaWithPagination;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@Getter
@EqualsAndHashCode
@NullMarked
public class PagedResponseWithMeta<T, M extends MetaWithPagination> {
    private final List<T> data;
    private final M meta;

    public PagedResponseWithMeta(List<T> data, M meta) {
        this.data = data;
        this.meta = meta;
    }

    public List<T> data() {
        return data;
    }

    public M meta() {
        return meta;
    }
}
