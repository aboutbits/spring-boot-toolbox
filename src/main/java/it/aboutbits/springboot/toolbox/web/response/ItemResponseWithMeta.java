package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class ItemResponseWithMeta<T, M extends Meta> {
    private final T data;
    private final M meta;

    public ItemResponseWithMeta(@NonNull T data, @NonNull M meta) {
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
