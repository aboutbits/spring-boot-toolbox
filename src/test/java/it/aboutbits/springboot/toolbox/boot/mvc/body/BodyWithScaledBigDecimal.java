package it.aboutbits.springboot.toolbox.boot.mvc.body;

import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;

public record BodyWithScaledBigDecimal(
        ScaledBigDecimal scaledBigDecimal
) {
}
