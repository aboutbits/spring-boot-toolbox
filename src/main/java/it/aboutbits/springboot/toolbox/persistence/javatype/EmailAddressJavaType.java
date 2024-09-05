package it.aboutbits.springboot.toolbox.persistence.javatype;

import it.aboutbits.springboot.toolbox.boot.persistence.AutoRegisteredJavaType;
import it.aboutbits.springboot.toolbox.persistence.javatype.base.WrappedStringJavaType;
import it.aboutbits.springboot.toolbox.type.EmailAddress;

public final class EmailAddressJavaType extends WrappedStringJavaType<EmailAddress> implements AutoRegisteredJavaType<EmailAddress> {
    public EmailAddressJavaType() {
        super(EmailAddress.class);
    }
}
