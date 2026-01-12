package it.aboutbits.springboot.toolbox.persistence.javatype.impl.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedByteJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapByteClass;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WrapByteClassJavaType extends WrappedByteJavaType<WrapByteClass> implements AutoRegisteredJavaType<WrapByteClass> {
    public WrapByteClassJavaType() {
        super(WrapByteClass.class);
    }
}
