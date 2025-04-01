package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedScaledBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimalClass;

public final class WrapScaledBigDecimalClassJavaType extends WrappedScaledBigDecimalJavaType<WrapScaledBigDecimalClass> implements AutoRegisteredJavaType<WrapScaledBigDecimalClass> {
    public WrapScaledBigDecimalClassJavaType() {
        super(WrapScaledBigDecimalClass.class);
    }
}
