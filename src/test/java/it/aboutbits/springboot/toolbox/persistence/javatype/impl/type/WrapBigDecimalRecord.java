package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;

@NullMarked
public record WrapBigDecimalRecord(BigDecimal value) implements CustomType<BigDecimal> {

}
