package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBigIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerClass;

public final class WrapBigIntegerClassJavaType extends WrappedBigIntegerJavaType<WrapBigIntegerClass> implements AutoRegisteredJavaType<WrapBigIntegerClass> {
    public WrapBigIntegerClassJavaType() {
        super(WrapBigIntegerClass.class);
    }
}
