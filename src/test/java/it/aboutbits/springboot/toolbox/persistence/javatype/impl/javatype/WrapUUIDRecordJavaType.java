package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedUUIDJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapUUIDRecord;

public final class WrapUUIDRecordJavaType extends WrappedUUIDJavaType<WrapUUIDRecord> implements AutoRegisteredJavaType<WrapUUIDRecord> {
    public WrapUUIDRecordJavaType() {
        super(WrapUUIDRecord.class);
    }
}
