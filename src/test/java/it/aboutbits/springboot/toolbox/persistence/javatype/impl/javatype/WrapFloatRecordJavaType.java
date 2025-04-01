package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedFloatJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloatRecord;

public final class WrapFloatRecordJavaType extends WrappedFloatJavaType<WrapFloatRecord> implements AutoRegisteredJavaType<WrapFloatRecord> {
    public WrapFloatRecordJavaType() {
        super(WrapFloatRecord.class);
    }
}
