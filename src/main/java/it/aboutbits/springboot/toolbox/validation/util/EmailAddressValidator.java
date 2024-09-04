package it.aboutbits.springboot.toolbox.validation.util;

import org.apache.commons.validator.routines.EmailValidator;

public final class EmailAddressValidator {
    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private EmailAddressValidator() {
    }

    /**
     * Validates if the provided email address is in a proper format.
     *
     * @param email the email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_VALIDATOR.isValid(email);
    }

    public static boolean isNotValid(String email) {
        return !isValid(email);
    }
}
