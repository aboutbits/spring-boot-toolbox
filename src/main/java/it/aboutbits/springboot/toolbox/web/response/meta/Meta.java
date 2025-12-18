package it.aboutbits.springboot.toolbox.web.response.meta;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Meta {
    record EmptyMeta() implements Meta {
    }
}
