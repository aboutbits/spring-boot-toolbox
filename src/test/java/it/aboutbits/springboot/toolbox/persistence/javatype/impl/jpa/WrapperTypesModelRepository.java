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
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapEnumClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapEnumRecord;
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
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapUUIDClass;
import it.aboutbits.springboot.toolbox.persistence.javatype.impl.type.WrapUUIDRecord;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@NullMarked
public interface WrapperTypesModelRepository extends JpaRepository<WrapperTypesModel, WrapperTypesModel.ID> {
    Optional<WrapperTypesModel> findByBigDecimalValue(@Nullable WrapBigDecimalRecord value);

    Optional<WrapperTypesModel> findByBigIntegerValue(@Nullable WrapBigIntegerRecord value);

    Optional<WrapperTypesModel> findByDoubleValue(@Nullable WrapDoubleRecord value);

    Optional<WrapperTypesModel> findByFloatValue(@Nullable WrapFloatRecord value);

    Optional<WrapperTypesModel> findByIntegerValue(@Nullable WrapIntegerRecord value);

    Optional<WrapperTypesModel> findByLongValue(@Nullable WrapLongRecord value);

    Optional<WrapperTypesModel> findByScaledBigDecimalValue(@Nullable WrapScaledBigDecimalRecord value);

    Optional<WrapperTypesModel> findByShortValue(@Nullable WrapShortRecord value);

    Optional<WrapperTypesModel> findByStringValue(@Nullable WrapStringRecord value);

    Optional<WrapperTypesModel> findByBoolValue(@Nullable WrapBooleanRecord value);

    Optional<WrapperTypesModel> findByByteValue(@Nullable WrapByteRecord value);

    Optional<WrapperTypesModel> findByCharValue(@Nullable WrapCharacterRecord value);

    Optional<WrapperTypesModel> findByUuidValue(@Nullable WrapUUIDRecord value);

    Optional<WrapperTypesModel> findByUuidValueAsString(@Nullable WrapUUIDRecord value);

    Optional<WrapperTypesModel> findByBigDecimalValueClass(@Nullable WrapBigDecimalClass value);

    Optional<WrapperTypesModel> findByBigIntegerValueClass(@Nullable WrapBigIntegerClass value);

    Optional<WrapperTypesModel> findByDoubleValueClass(@Nullable WrapDoubleClass value);

    Optional<WrapperTypesModel> findByFloatValueClass(@Nullable WrapFloatClass value);

    Optional<WrapperTypesModel> findByIntegerValueClass(@Nullable WrapIntegerClass value);

    Optional<WrapperTypesModel> findByLongValueClass(@Nullable WrapLongClass value);

    Optional<WrapperTypesModel> findByScaledBigDecimalValueClass(@Nullable WrapScaledBigDecimalClass value);

    Optional<WrapperTypesModel> findByShortValueClass(@Nullable WrapShortClass value);

    Optional<WrapperTypesModel> findByStringValueClass(@Nullable WrapStringClass value);

    Optional<WrapperTypesModel> findByBoolValueClass(@Nullable WrapBooleanClass value);

    Optional<WrapperTypesModel> findByByteValueClass(@Nullable WrapByteClass value);

    Optional<WrapperTypesModel> findByCharValueClass(@Nullable WrapCharacterClass value);

    Optional<WrapperTypesModel> findByUuidValueClass(@Nullable WrapUUIDClass value);

    Optional<WrapperTypesModel> findByUuidValueClassAsString(@Nullable WrapUUIDClass value);

    Optional<WrapperTypesModel> findByEnumValue(@Nullable WrapEnumRecord value);

    Optional<WrapperTypesModel> findByEnumValueClass(@Nullable WrapEnumClass value);
}
