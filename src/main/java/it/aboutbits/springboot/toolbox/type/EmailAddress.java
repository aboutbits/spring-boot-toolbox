package it.aboutbits.springboot.toolbox.type;

import it.aboutbits.springboot.toolbox.validation.util.EmailAddressValidator;
import lombok.NonNull;

/**
 * A record representing an email address, validated and stored in lowercase.
 * <p>
 * The EmailAddress record ensures that the provided email address string
 * is in a valid format and automatically converts it to lowercase for
 * consistent internal representation. It implements the Comparable
 * interface to allow comparison between different email addresses.
 * </p>
 *
 * @param value the email address string
 * @throws IllegalArgumentException if the provided email address is not in a valid format
 */
public record EmailAddress(String value) implements Comparable<EmailAddress> {
    public EmailAddress(String value) {
        if (value == null || EmailAddressValidator.isNotValid(value)) {
            throw new IllegalArgumentException("Value is not a valid email address: " + value);
        }

        this.value = value.toLowerCase();
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(@NonNull EmailAddress o) {
        return value().compareTo(o.value());
    }
}
