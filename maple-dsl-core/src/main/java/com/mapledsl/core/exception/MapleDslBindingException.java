package com.mapledsl.core.exception;

/**
 * Exception class for Maple Domain-Specific Language (DSL) binding related errors.
 */
public class MapleDslBindingException extends MapleDslException {
    public MapleDslBindingException(String message) {
        super(message);
    }

    public MapleDslBindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapleDslBindingException(Throwable cause) {
        super(cause);
    }
}
