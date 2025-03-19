package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import lombok.NonNull;

import java.util.List;

public final class ListResponse<T> extends ListResponseWithMeta<T, Meta.EmptyMeta> {
    public ListResponse(@NonNull List<T> data) {
        super(data, new Meta.EmptyMeta());
    }
}
