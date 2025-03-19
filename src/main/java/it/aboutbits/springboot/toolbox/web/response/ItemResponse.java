package it.aboutbits.springboot.toolbox.web.response;

import it.aboutbits.springboot.toolbox.web.response.meta.Meta;
import lombok.NonNull;

public final class ItemResponse<T> extends ItemResponseWithMeta<T, Meta.EmptyMeta> {
    public ItemResponse(@NonNull T data) {
        super(data, new Meta.EmptyMeta());
    }
}
