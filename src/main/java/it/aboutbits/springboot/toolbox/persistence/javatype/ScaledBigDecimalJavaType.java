package it.aboutbits.springboot.toolbox.persistence.javatype;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;

public final class ScaledBigDecimalJavaType extends WrappedBigDecimalJavaType<ScaledBigDecimal> implements AutoRegisteredJavaType<ScaledBigDecimal> {
    public ScaledBigDecimalJavaType() {
        super(ScaledBigDecimal.class);
    }
}
