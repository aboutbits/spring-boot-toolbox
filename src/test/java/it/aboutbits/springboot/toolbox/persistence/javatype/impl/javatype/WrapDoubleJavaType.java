package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedDoubleJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDouble;

public final class WrapDoubleJavaType extends WrappedDoubleJavaType<WrapDouble> implements AutoRegisteredJavaType<WrapDouble> {
    public WrapDoubleJavaType() {
        super(WrapDouble.class);
    }
}
