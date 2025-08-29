package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedEnumJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapEnumClass;

public final class WrapEnumClassJavaType extends WrappedEnumJavaType<WrapEnumClass> implements AutoRegisteredJavaType<WrapEnumClass> {
    public WrapEnumClassJavaType() {
        super(WrapEnumClass.class);
    }
}
