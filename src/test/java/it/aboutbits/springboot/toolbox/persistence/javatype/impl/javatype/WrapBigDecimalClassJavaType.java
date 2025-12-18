package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedBigDecimalJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalClass;
import org.jspecify.annotations.NullMarked;

@NullMarked

public final class WrapBigDecimalClassJavaType extends WrappedBigDecimalJavaType<WrapBigDecimalClass> implements AutoRegisteredJavaType<WrapBigDecimalClass> {
    public WrapBigDecimalClassJavaType() {
        super(WrapBigDecimalClass.class);
    }
}
