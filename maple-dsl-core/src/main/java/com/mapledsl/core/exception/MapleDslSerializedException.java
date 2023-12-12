package com.mapledsl.core.exception;

/**
 * Exception class for Maple Domain-Specific Language (DSL) related errors
 * that occur during serialization.
 */
public class MapleDslSerializedException extends MapleDslException {
    public MapleDslSerializedException(String message) {
        super(message);
    }

    public MapleDslSerializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapleDslSerializedException(Throwable cause) {
        super(cause);
    }
}
