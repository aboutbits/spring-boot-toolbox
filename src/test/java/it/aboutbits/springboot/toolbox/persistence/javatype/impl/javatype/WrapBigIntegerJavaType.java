package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBigIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigInteger;

public final class WrapBigIntegerJavaType extends WrappedBigIntegerJavaType<WrapBigInteger> implements AutoRegisteredJavaType<WrapBigInteger> {
    public WrapBigIntegerJavaType() {
        super(WrapBigInteger.class);
    }
}
