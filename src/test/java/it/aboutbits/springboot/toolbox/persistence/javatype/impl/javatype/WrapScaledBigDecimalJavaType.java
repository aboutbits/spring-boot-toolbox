package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedScaledBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimal;

public final class WrapScaledBigDecimalJavaType extends WrappedScaledBigDecimalJavaType<WrapScaledBigDecimal> implements AutoRegisteredJavaType<WrapScaledBigDecimal> {
    public WrapScaledBigDecimalJavaType() {
        super(WrapScaledBigDecimal.class);
    }
}
