package it.aboutbits.springboot.toolbox.type;

import it.aboutbits.springboot.toolbox.validation.util.IbanValidator;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * The {@code Iban} class represents an International Bank Account Number (IBAN) and provides functionality for validation
 * and comparison of IBANs. It ensures the valid format of the IBAN according to predefined rules.
 *
 * @param value the IBAN value, which must be a valid and non-null string
 */
@NullMarked
public record Iban(String value) implements CustomType<String>, Comparable<Iban> {
    public Iban(@Nullable String value) {
        if (value == null || IbanValidator.isNotValid(value.toUpperCase())) {
            throw new IllegalArgumentException("Value is not a valid IBAN: " + value);
        }

        this.value = value.toUpperCase();
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Retrieves the ABI (Bank Branch Code) if the IBAN belongs to an Italian bank.
     *
     * @return An {@code Optional} containing the ABI value if the IBAN is Italian, or an empty {@code Optional} otherwise.
     */

    public Optional<String> getAbiIfItalian() {
        var iban = value();

        if (!iban.startsWith("IT")) {
            return Optional.empty();
        }
        return Optional.of(iban.substring(5, 10));
    }

    @Override
    public int compareTo(Iban o) {
        return value().compareTo(o.value());
    }
}
