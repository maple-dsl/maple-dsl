package com.mapledsl.core.exception;

/**
 * Exception class for Maple Domain-Specific Language (DSL) related errors.
 */
public class MapleDslException extends RuntimeException {
    public MapleDslException(String message) {
        super(message);
    }

    public MapleDslException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapleDslException(Throwable cause) {
        super(cause);
    }

    public static Object throwEX(String message) {
        throw new MapleDslException(message);
    }
}
