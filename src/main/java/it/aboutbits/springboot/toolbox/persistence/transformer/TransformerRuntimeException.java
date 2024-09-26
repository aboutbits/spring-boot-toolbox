package it.aboutbits.springboot.toolbox.persistence.transformer;

public class TransformerRuntimeException extends RuntimeException {
    public TransformerRuntimeException() {
    }

    public TransformerRuntimeException(final String message) {
        super(message);
    }

    public TransformerRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransformerRuntimeException(final Throwable cause) {
        super(cause);
    }

    public TransformerRuntimeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
