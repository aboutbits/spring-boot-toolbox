package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record WrapScaledBigDecimalRecord(ScaledBigDecimal value) implements CustomType<ScaledBigDecimal> {

}
