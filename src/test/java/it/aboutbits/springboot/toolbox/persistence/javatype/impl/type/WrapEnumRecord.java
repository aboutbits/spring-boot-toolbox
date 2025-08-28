package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;

public record WrapEnumRecord(SampleEnum value) implements CustomType<SampleEnum> {
}
