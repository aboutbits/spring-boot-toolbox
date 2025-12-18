package it.aboutbits.springboot.toolbox.autoconfiguration.persistence;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CustomTypeContributor extends AbstractCustomTypeContributor {
    public CustomTypeContributor() {
        super("it.aboutbits.springboot.toolbox");
    }
}
