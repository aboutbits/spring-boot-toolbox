package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBooleanJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBooleanClass;

public final class WrapBooleanClassJavaType extends WrappedBooleanJavaType<WrapBooleanClass> implements AutoRegisteredJavaType<WrapBooleanClass> {
    public WrapBooleanClassJavaType() {
        super(WrapBooleanClass.class);
    }
}
