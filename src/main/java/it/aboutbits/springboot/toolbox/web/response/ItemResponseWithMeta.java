package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;

@Getter
@EqualsAndHashCode
@NullMarked
public class ItemResponseWithMeta<T, M extends Meta> {
    private final T data;
    private final M meta;

    public ItemResponseWithMeta(T data, M meta) {
        this.data = data;
        this.meta = meta;
    }

    public T data() {
        return data;
    }

    public M meta() {
        return meta;
    }
}
