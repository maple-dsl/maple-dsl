package com.mapledsl.core.extension.func;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
final class SneakyThrowUtil {

    @SuppressWarnings("unchecked")
    static <T extends Exception, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}