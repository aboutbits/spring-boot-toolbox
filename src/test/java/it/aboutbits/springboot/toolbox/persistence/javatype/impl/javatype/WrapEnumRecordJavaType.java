package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedEnumJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapEnumRecord;

public final class WrapEnumRecordJavaType extends WrappedEnumJavaType<WrapEnumRecord> implements AutoRegisteredJavaType<WrapEnumRecord> {
    public WrapEnumRecordJavaType() {
        super(WrapEnumRecord.class);
    }
}
