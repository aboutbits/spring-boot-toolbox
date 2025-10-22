package it.aboutbits.springboot.toolbox.exception;

public class ServerRuntimeException extends RuntimeException {
    public ServerRuntimeException() {
    }

    public ServerRuntimeException(ExceptionMessageDefinition message) {
        super(message.code());
    }

    public ServerRuntimeException(String message) {
        super(message);
    }

    public ServerRuntimeException(ExceptionMessageDefinition message, Throwable cause) {
        super(message.code(), cause);
    }

    public ServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerRuntimeException(Throwable cause) {
        super(cause);
    }

    public ServerRuntimeException(
            ExceptionMessageDefinition message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message.code(), cause, enableSuppression, writableStackTrace);
    }

    public ServerRuntimeException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
