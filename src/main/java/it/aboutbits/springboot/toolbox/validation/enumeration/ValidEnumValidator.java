package it.aboutbits.springboot.toolbox.validation.enumeration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Set<String> enumValues;

    @Override
    public void initialize(ValidEnum validEnum) {
        var enumClass = validEnum.enumClass();

        enumValues = new HashSet<>();
        for (Enum<?> anEnum : enumClass.getEnumConstants()) {
            enumValues.add(anEnum.name());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return enumValues.contains(value);
    }
}
