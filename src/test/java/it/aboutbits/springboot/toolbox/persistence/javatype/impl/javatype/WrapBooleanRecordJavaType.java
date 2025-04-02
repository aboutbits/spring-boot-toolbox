package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBooleanJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBooleanRecord;

public final class WrapBooleanRecordJavaType extends WrappedBooleanJavaType<WrapBooleanRecord> implements AutoRegisteredJavaType<WrapBooleanRecord> {
    public WrapBooleanRecordJavaType() {
        super(WrapBooleanRecord.class);
    }
}
