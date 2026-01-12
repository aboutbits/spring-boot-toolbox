package it.aboutbits.springboot.toolbox.persistence.javatype;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedStringJavaType;
import it.aboutbits.springboot.toolbox.type.EmailAddress;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EmailAddressJavaType extends WrappedStringJavaType<EmailAddress> implements AutoRegisteredJavaType<EmailAddress> {
    public EmailAddressJavaType() {
        super(EmailAddress.class);
    }
}
