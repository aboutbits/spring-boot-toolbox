package it.aboutbits.springboot.toolbox.exception;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ClientRuntimeException extends RuntimeException {
    public ClientRuntimeException() {
    }

    public ClientRuntimeException(ExceptionMessageDefinition message) {
        super(message.code());
    }

    public ClientRuntimeException(String message) {
        super(message);
    }

    public ClientRuntimeException(ExceptionMessageDefinition message, Throwable cause) {
        super(message.code(), cause);
    }

    public ClientRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientRuntimeException(Throwable cause) {
        super(cause);
    }

    public ClientRuntimeException(
            ExceptionMessageDefinition message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message.code(), cause, enableSuppression, writableStackTrace);
    }

    public ClientRuntimeException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
