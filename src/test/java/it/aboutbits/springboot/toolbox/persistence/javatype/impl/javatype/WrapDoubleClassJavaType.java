package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedDoubleJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDoubleClass;

public final class WrapDoubleClassJavaType extends WrappedDoubleJavaType<WrapDoubleClass> implements AutoRegisteredJavaType<WrapDoubleClass> {
    public WrapDoubleClassJavaType() {
        super(WrapDoubleClass.class);
    }
}
