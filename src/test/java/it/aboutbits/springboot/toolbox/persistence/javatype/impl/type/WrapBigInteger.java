package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;

import java.math.BigInteger;

public record WrapBigInteger(BigInteger value) implements CustomType<BigInteger> {

}
