package com.mapledsl.core.exception;

/**
 * @author bofa1ex
 * @since 2023/8/14
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
