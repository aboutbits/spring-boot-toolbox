package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedFloatJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloat;

public final class WrapFloatJavaType extends WrappedFloatJavaType<WrapFloat> implements AutoRegisteredJavaType<WrapFloat> {
    public WrapFloatJavaType() {
        super(WrapFloat.class);
    }
}
