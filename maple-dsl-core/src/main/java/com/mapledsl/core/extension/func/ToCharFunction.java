package com.mapledsl.core.extension.func;

/**
 * This functional interface represents a function that takes an object and returns a char value.
 */
@FunctionalInterface
public interface ToCharFunction {
    /**
     * Applies this function to the given object and returns a char value.
     *
     * @param bean the object to apply the function to
     * @return the char value resulting from applying this function to the given object
     */
    char applyAsChar(Object bean);
}
