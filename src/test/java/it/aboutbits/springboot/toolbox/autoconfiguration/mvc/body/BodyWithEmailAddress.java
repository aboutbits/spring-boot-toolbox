package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import it.aboutbits.springboot.toolbox.type.EmailAddress;

public record BodyWithEmailAddress(
        EmailAddress emailAddress
) {
}
