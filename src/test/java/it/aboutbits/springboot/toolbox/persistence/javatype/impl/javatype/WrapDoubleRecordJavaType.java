package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedDoubleJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDoubleRecord;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapDoubleRecordJavaType extends WrappedDoubleJavaType<WrapDoubleRecord> implements AutoRegisteredJavaType<WrapDoubleRecord> {
    public WrapDoubleRecordJavaType() {
        super(WrapDoubleRecord.class);
    }
}
