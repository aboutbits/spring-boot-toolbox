package it.aboutbits.springboot.toolbox.boot.mvc.body;

import it.aboutbits.springboot.toolbox.type.Iban;

public record BodyWithIban(
        Iban iban
) {
}
