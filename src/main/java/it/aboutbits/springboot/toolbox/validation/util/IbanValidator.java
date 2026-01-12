package it.aboutbits.springboot.toolbox.validation.util;

import org.apache.commons.validator.routines.IBANValidator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class IbanValidator {
    private static final IBANValidator IBAN_VALIDATOR = new IBANValidator();

    private IbanValidator() {
    }

    /**
     * Validates whether the given IBAN (International Bank Account Number) is valid according to predefined rules.
     *
     * @param iban the IBAN to be validated
     * @return true if the IBAN is valid, false otherwise
     */
    public static boolean isValid(@Nullable String iban) {
        if (iban == null) {
            return false;
        }
        return IBAN_VALIDATOR.isValid(iban);
    }

    public static boolean isNotValid(@Nullable String iban) {
        return !isValid(iban);
    }
}
