package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
@EqualsAndHashCode
public class ListResponseWithMeta<T, M extends Meta> {
    private final List<T> data;
    private final M meta;

    public ListResponseWithMeta(@NonNull List<T> data, @NonNull M meta) {
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
