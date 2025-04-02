package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBigIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerRecord;

public final class WrapBigIntegerRecordJavaType extends WrappedBigIntegerJavaType<WrapBigIntegerRecord> implements AutoRegisteredJavaType<WrapBigIntegerRecord> {
    public WrapBigIntegerRecordJavaType() {
        super(WrapBigIntegerRecord.class);
    }
}
