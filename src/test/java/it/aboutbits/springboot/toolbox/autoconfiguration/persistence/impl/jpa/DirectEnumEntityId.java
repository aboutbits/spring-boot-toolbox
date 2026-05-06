package it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa;

import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DirectEnumEntityId implements EntityId<DirectEnumEntityId> {
    VAL1,
    VAL2;

    @Override
    public DirectEnumEntityId value() {
        return this;
    }
}
