package com.mapledsl.core.exception;

/**
 * @author bofa1ex
 * @since 2023/8/14
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
