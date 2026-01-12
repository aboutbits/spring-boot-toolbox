package it.aboutbits.springboot.toolbox.persistence.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedStringJavaType;
import it.aboutbits.springboot.toolbox.type.Iban;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class IbanJavaType extends WrappedStringJavaType<Iban> implements AutoRegisteredJavaType<Iban> {
    public IbanJavaType() {
        super(Iban.class);
    }
}
