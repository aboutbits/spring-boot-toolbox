package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedStringJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapString;

public final class WrapStringJavaType extends WrappedStringJavaType<WrapString> implements AutoRegisteredJavaType<WrapString> {
    public WrapStringJavaType() {
        super(WrapString.class);
    }
}
