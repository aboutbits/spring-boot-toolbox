package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import it.aboutbits.springboot.toolbox.type.Iban;

public record BodyWithIban(
        Iban iban
) {
}
