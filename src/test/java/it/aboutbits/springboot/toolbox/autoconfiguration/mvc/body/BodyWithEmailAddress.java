package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import it.aboutbits.springboot.toolbox.type.EmailAddress;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record BodyWithEmailAddress(
        EmailAddress emailAddress
) {
}
