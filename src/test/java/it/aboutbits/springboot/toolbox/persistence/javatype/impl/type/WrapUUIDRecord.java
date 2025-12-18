package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
public record WrapUUIDRecord(UUID value) implements CustomType<UUID> {

}
