package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedCharacterJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapCharacterClass;

public final class WrapCharacterClassJavaType extends WrappedCharacterJavaType<WrapCharacterClass> implements AutoRegisteredJavaType<WrapCharacterClass> {
    public WrapCharacterClassJavaType() {
        super(WrapCharacterClass.class);
    }
}
