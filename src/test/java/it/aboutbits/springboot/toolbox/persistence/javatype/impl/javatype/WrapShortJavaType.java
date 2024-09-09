package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedShortJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShort;

public final class WrapShortJavaType extends WrappedShortJavaType<WrapShort> implements AutoRegisteredJavaType<WrapShort> {
    public WrapShortJavaType() {
        super(WrapShort.class);
    }
}
