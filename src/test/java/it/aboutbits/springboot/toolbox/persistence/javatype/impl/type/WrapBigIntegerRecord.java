package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;

import java.math.BigInteger;

@NullMarked
public record WrapBigIntegerRecord(BigInteger value) implements CustomType<BigInteger> {

}
