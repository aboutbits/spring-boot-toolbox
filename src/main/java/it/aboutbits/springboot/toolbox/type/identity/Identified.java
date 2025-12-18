package it.aboutbits.springboot.toolbox.type.identity;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Identified<ID extends EntityId<?>> {
    ID getId();
}
