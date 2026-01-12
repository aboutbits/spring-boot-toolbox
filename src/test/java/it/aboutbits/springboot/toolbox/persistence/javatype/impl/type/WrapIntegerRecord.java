package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record WrapIntegerRecord(Integer value) implements CustomType<Integer> {

}
