package com.mapledsl.core.extension.func;

/**
 * Utility class for throwing checked exceptions as unchecked exceptions.
 */
final class SneakyThrowUtil {

    /**
     * Throws a checked exception as an unchecked exception.
     *
     * @param <T> The type of the checked exception
     * @param <R> The type of the return value
     * @param t The throwable to be thrown
     * @return This method does not return as it throws a checked exception as an unchecked exception
     * @throws T The throwable casted from the input throwable
     */
    @SuppressWarnings("unchecked")
    static <T extends Exception, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}