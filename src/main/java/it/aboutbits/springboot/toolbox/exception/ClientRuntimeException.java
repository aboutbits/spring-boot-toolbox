package it.aboutbits.springboot.toolbox.exception;

import lombok.Getter;
import org.jspecify.annotations.NullMarked;

@Getter
@NullMarked
public class ClientRuntimeException extends RuntimeException {

    private final Object[] args;

    public ClientRuntimeException() {
        this.args = new Object[0];
    }

    public ClientRuntimeException(ExceptionMessageDefinition message) {
        super(message.code());
        this.args = new Object[0];
    }

    public ClientRuntimeException(ExceptionMessageDefinition message, Object... args) {
        super(message.code());
        this.args = args;
    }

    public ClientRuntimeException(String message) {
        super(message);
        this.args = new Object[0];
    }

    public ClientRuntimeException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public ClientRuntimeException(ExceptionMessageDefinition message, Throwable cause) {
        super(message.code(), cause);
        this.args = new Object[0];
    }

    public ClientRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.args = new Object[0];
    }

    public ClientRuntimeException(Throwable cause) {
        super(cause);
        this.args = new Object[0];
    }

    public ClientRuntimeException(
            ExceptionMessageDefinition message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message.code(), cause, enableSuppression, writableStackTrace);
        this.args = new Object[0];
    }

    public ClientRuntimeException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.args = new Object[0];
    }
}
