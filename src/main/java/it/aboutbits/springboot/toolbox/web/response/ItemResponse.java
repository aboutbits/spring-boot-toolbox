package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ItemResponse<T> extends ItemResponseWithMeta<T, Meta.EmptyMeta> {
    public ItemResponse(T data) {
        super(data, new Meta.EmptyMeta());
    }
}
