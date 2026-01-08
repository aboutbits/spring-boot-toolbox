package it.aboutbits.springboot.toolbox.type.identity;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Identified<ID extends EntityId<?>> {
    @Nullable
    ID getId();
}
