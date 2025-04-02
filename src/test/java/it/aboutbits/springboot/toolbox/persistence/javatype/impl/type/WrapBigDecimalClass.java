package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public class WrapBigDecimalClass implements CustomType<BigDecimal> {
    private final BigDecimal value;
}
