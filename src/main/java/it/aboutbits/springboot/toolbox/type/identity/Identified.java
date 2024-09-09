package it.aboutbits.springboot.toolbox.type.identity;

public interface Identified<ID extends EntityId<?>> {
    ID getId();
}
