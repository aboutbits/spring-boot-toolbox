package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapIntegerRecord;

public final class WrapIntegerRecordJavaType extends WrappedIntegerJavaType<WrapIntegerRecord> implements AutoRegisteredJavaType<WrapIntegerRecord> {
    public WrapIntegerRecordJavaType() {
        super(WrapIntegerRecord.class);
    }
}
