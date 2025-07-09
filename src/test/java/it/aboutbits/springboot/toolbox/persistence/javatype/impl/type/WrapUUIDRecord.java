package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;

import java.util.UUID;

public record WrapUUIDRecord(UUID value) implements CustomType<UUID> {

}
