package com.mapledsl.core.exception;

/**
 * @author bofa1ex
 * @since 2023/8/14
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
