package it.aboutbits.springboot.toolbox.persistence.identity;

public interface Identified<ID extends EntityId<?>> {
    ID getId();
}
