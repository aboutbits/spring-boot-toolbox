package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record BodyWithScaledBigDecimal(
        ScaledBigDecimal scaledBigDecimal
) {
}
