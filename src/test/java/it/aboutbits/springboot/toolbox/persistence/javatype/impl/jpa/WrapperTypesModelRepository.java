package it.aboutbits.springboot.toolbox.persistence.javatype.impl.jpa;

import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigDecimalRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBigIntegerRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBooleanClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapBooleanRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapByteClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapByteRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapCharacterClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapCharacterRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDoubleClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapDoubleRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloatClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapFloatRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapIntegerClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapIntegerRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLongClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapLongRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimalClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapScaledBigDecimalRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShortClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapShortRecord;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapStringClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapStringRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WrapperTypesModelRepository extends JpaRepository<WrapperTypesModel, WrapperTypesModel.ID> {
    Optional<WrapperTypesModel> findByBigDecimalValue(WrapBigDecimalRecord value);

    Optional<WrapperTypesModel> findByBigIntegerValue(WrapBigIntegerRecord value);

    Optional<WrapperTypesModel> findByDoubleValue(WrapDoubleRecord value);

    Optional<WrapperTypesModel> findByFloatValue(WrapFloatRecord value);

    Optional<WrapperTypesModel> findByIntegerValue(WrapIntegerRecord value);

    Optional<WrapperTypesModel> findByLongValue(WrapLongRecord value);

    Optional<WrapperTypesModel> findByScaledBigDecimalValue(WrapScaledBigDecimalRecord value);

    Optional<WrapperTypesModel> findByShortValue(WrapShortRecord value);

    Optional<WrapperTypesModel> findByStringValue(WrapStringRecord value);

    Optional<WrapperTypesModel> findByBoolValue(WrapBooleanRecord value);

    Optional<WrapperTypesModel> findByByteValue(WrapByteRecord value);

    Optional<WrapperTypesModel> findByCharValue(WrapCharacterRecord value);

    Optional<WrapperTypesModel> findByBigDecimalValueClass(WrapBigDecimalClass value);

    Optional<WrapperTypesModel> findByBigIntegerValueClass(WrapBigIntegerClass value);

    Optional<WrapperTypesModel> findByDoubleValueClass(WrapDoubleClass value);

    Optional<WrapperTypesModel> findByFloatValueClass(WrapFloatClass value);

    Optional<WrapperTypesModel> findByIntegerValueClass(WrapIntegerClass value);

    Optional<WrapperTypesModel> findByLongValueClass(WrapLongClass value);

    Optional<WrapperTypesModel> findByScaledBigDecimalValueClass(WrapScaledBigDecimalClass value);

    Optional<WrapperTypesModel> findByShortValueClass(WrapShortClass value);

    Optional<WrapperTypesModel> findByStringValueClass(WrapStringClass value);

    Optional<WrapperTypesModel> findByBoolValueClass(WrapBooleanClass value);

    Optional<WrapperTypesModel> findByByteValueClass(WrapByteClass value);

    Optional<WrapperTypesModel> findByCharValueClass(WrapCharacterClass value);
}
