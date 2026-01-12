package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedIntegerJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapIntegerClass;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapIntegerClassJavaType extends WrappedIntegerJavaType<WrapIntegerClass> implements AutoRegisteredJavaType<WrapIntegerClass> {
    public WrapIntegerClassJavaType() {
        super(WrapIntegerClass.class);
    }
}
