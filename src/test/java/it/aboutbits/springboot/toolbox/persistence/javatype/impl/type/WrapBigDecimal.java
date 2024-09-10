package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;

import java.math.BigDecimal;

public record WrapBigDecimal(BigDecimal value) implements CustomType<BigDecimal> {

}
