package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;

public record WrapByteRecord(Byte value) implements CustomType<Byte> {

}
