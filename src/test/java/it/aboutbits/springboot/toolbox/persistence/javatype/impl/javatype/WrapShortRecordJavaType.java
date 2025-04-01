package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedShortJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShortRecord;

public final class WrapShortRecordJavaType extends WrappedShortJavaType<WrapShortRecord> implements AutoRegisteredJavaType<WrapShortRecord> {
    public WrapShortRecordJavaType() {
        super(WrapShortRecord.class);
    }
}
