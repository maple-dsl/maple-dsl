package com.mapledsl.core.exception;

/**
 * @author bofa1ex
 * @since 2023/8/14
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
