package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import it.aboutbits.springboot.toolbox.type.Iban;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record BodyWithIban(
        Iban iban
) {
}
