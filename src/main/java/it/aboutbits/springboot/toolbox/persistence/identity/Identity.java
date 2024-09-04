package it.aboutbits.springboot.toolbox.persistence.identity;

import it.aboutbits.springboot.toolbox.persistence.WrappedValue;

import java.io.Serializable;

/**
 * The Identity interface represents a serializable entity that wraps a value.
 * A class implementing Identity is considered to be the primary key of an entity type.
 *
 * @param <T> the type of the value being wrapped
 */
public interface Identity<T> extends WrappedValue<T>, Serializable {
}
