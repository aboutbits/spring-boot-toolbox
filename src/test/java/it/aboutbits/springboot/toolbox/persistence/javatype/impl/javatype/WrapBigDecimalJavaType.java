package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimal;

public final class WrapBigDecimalJavaType extends WrappedBigDecimalJavaType<WrapBigDecimal> implements AutoRegisteredJavaType<WrapBigDecimal> {
    public WrapBigDecimalJavaType() {
        super(WrapBigDecimal.class);
    }
}
