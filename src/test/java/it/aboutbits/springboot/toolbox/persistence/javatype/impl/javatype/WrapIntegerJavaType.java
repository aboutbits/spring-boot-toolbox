package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapInteger;

public final class WrapIntegerJavaType extends WrappedIntegerJavaType<WrapInteger> implements AutoRegisteredJavaType<WrapInteger> {
    public WrapIntegerJavaType() {
        super(WrapInteger.class);
    }
}
