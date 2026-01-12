package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedFloatJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloatClass;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapFloatClassJavaType extends WrappedFloatJavaType<WrapFloatClass> implements AutoRegisteredJavaType<WrapFloatClass> {
    public WrapFloatClassJavaType() {
        super(WrapFloatClass.class);
    }
}
