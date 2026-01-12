package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedByteJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapByteRecord;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapByteRecordJavaType extends WrappedByteJavaType<WrapByteRecord> implements AutoRegisteredJavaType<WrapByteRecord> {
    public WrapByteRecordJavaType() {
        super(WrapByteRecord.class);
    }
}
