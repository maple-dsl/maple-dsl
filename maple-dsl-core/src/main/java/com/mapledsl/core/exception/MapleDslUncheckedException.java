package com.mapledsl.core.exception;

/**
 * Exception class for unchecked errors related to the Maple Domain-Specific Language (DSL).
 */
public class MapleDslUncheckedException extends MapleDslException {

    public MapleDslUncheckedException(String message) {
        super(message);
    }

    public MapleDslUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapleDslUncheckedException(Throwable cause) {
        super(cause);
    }
}
