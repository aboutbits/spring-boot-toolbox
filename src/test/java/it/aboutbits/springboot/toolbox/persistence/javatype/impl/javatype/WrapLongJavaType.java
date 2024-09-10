package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLong;

public final class WrapLongJavaType extends WrappedLongJavaType<WrapLong> implements AutoRegisteredJavaType<WrapLong> {
    public WrapLongJavaType() {
        super(WrapLong.class);
    }
}
