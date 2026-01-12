package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedScaledBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimalRecord;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapScaledBigDecimalRecordJavaType extends WrappedScaledBigDecimalJavaType<WrapScaledBigDecimalRecord> implements AutoRegisteredJavaType<WrapScaledBigDecimalRecord> {
    public WrapScaledBigDecimalRecordJavaType() {
        super(WrapScaledBigDecimalRecord.class);
    }
}
