package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalRecord;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapBigDecimalRecordJavaType extends WrappedBigDecimalJavaType<WrapBigDecimalRecord> implements AutoRegisteredJavaType<WrapBigDecimalRecord> {
    public WrapBigDecimalRecordJavaType() {
        super(WrapBigDecimalRecord.class);
    }
}
