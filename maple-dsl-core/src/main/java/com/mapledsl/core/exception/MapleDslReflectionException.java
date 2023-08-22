package com.mapledsl.core.exception;

/**
 * @author bofa1ex
 * @since 2023/8/14
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
