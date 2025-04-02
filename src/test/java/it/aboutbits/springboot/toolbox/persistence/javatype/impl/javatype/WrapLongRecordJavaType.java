package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLongRecord;

public final class WrapLongRecordJavaType extends WrappedLongJavaType<WrapLongRecord> implements AutoRegisteredJavaType<WrapLongRecord> {
    public WrapLongRecordJavaType() {
        super(WrapLongRecord.class);
    }
}
