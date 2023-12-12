package com.mapledsl.core.exception;

/**
 * Exception class for reflection related errors in the Maple Domain-Specific Language (DSL).
 */
public class MapleDslReflectionException extends MapleDslException {
    public MapleDslReflectionException(String message) {
        super(message);
    }

    public MapleDslReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapleDslReflectionException(Throwable cause) {
        super(cause);
    }
}
