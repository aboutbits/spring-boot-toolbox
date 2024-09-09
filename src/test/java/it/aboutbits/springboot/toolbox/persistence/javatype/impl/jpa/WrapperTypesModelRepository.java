package it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa;

import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimal;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigInteger;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDouble;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloat;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapInteger;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLong;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimal;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShort;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapString;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WrapperTypesModelRepository extends JpaRepository<WrapperTypesModel, WrapperTypesModel.ID> {
    Optional<WrapperTypesModel> findByBigDecimalValue(WrapBigDecimal value);

    Optional<WrapperTypesModel> findByBigIntegerValue(WrapBigInteger value);

    Optional<WrapperTypesModel> findByDoubleValue(WrapDouble value);

    Optional<WrapperTypesModel> findByFloatValue(WrapFloat value);

    Optional<WrapperTypesModel> findByIntegerValue(WrapInteger value);

    Optional<WrapperTypesModel> findByLongValue(WrapLong value);

    Optional<WrapperTypesModel> findByScaledBigDecimalValue(WrapScaledBigDecimal value);

    Optional<WrapperTypesModel> findByShortValue(WrapShort value);

    Optional<WrapperTypesModel> findByStringValue(WrapString value);
}
