package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedCharacterJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapCharacterRecord;

public final class WrapCharacterRecordJavaType extends WrappedCharacterJavaType<WrapCharacterRecord> implements AutoRegisteredJavaType<WrapCharacterRecord> {
    public WrapCharacterRecordJavaType() {
        super(WrapCharacterRecord.class);
    }
}
