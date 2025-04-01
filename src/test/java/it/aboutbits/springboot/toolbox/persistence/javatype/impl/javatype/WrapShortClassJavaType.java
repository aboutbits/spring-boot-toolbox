package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedShortJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShortClass;

public final class WrapShortClassJavaType extends WrappedShortJavaType<WrapShortClass> implements AutoRegisteredJavaType<WrapShortClass> {
    public WrapShortClassJavaType() {
        super(WrapShortClass.class);
    }
}
