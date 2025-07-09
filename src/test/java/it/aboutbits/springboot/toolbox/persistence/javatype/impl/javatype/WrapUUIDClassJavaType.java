package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedUUIDJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapUUIDClass;

public final class WrapUUIDClassJavaType extends WrappedUUIDJavaType<WrapUUIDClass> implements AutoRegisteredJavaType<WrapUUIDClass> {
    public WrapUUIDClassJavaType() {
        super(WrapUUIDClass.class);
    }
}
