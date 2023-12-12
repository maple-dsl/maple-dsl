package com.mapledsl.core.exception;

/**
 * Exception class for errors that occur during the execution of Maple DSL.
 */
public class MapleDslExecutionException extends MapleDslException {
    public MapleDslExecutionException(String message) {
        super(message);
    }

    public MapleDslExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapleDslExecutionException(Throwable cause) {
        super(cause);
    }
}
