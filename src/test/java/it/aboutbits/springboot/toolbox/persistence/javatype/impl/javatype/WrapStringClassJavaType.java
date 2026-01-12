package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedStringJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapStringClass;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapStringClassJavaType extends WrappedStringJavaType<WrapStringClass> implements AutoRegisteredJavaType<WrapStringClass> {
    public WrapStringClassJavaType() {
        super(WrapStringClass.class);
    }
}
