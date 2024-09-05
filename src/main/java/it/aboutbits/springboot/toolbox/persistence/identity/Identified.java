package it.aboutbits.springboot.toolbox.persistence.identity;

public interface Identified<ID extends Identity<?>> {
    ID getId();
}
