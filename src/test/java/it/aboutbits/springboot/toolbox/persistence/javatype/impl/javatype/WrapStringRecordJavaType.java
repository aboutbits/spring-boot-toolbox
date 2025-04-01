package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedStringJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapStringRecord;

public final class WrapStringRecordJavaType extends WrappedStringJavaType<WrapStringRecord> implements AutoRegisteredJavaType<WrapStringRecord> {
    public WrapStringRecordJavaType() {
        super(WrapStringRecord.class);
    }
}
