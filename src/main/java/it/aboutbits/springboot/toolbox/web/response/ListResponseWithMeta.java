package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@Getter
@EqualsAndHashCode
@NullMarked
public class ListResponseWithMeta<T, M extends Meta> {
    private final List<T> data;
    private final M meta;

    public ListResponseWithMeta(List<T> data, M meta) {
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
