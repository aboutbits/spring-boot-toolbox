package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class ListResponse<T> extends ListResponseWithMeta<T, Meta.EmptyMeta> {
    public ListResponse(List<T> data) {
        super(data, new Meta.EmptyMeta());
    }
}
