package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedLongJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLongClass;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapLongClassJavaType extends WrappedLongJavaType<WrapLongClass> implements AutoRegisteredJavaType<WrapLongClass> {
    public WrapLongClassJavaType() {
        super(WrapLongClass.class);
    }
}
