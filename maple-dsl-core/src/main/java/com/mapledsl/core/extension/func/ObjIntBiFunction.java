package com.mapledsl.core.extension.func;

import com.mapledsl.core.exception.MapleDslBindingException;

/**
 * Functional interface for applying a function that takes an object and an integer input and returns an object.
 */
@FunctionalInterface
public interface ObjIntBiFunction {
    /**
     * Applies a function that takes an object and an integer input and returns an object.
     *
     * @param bean  the object to apply the function to
     * @param value the integer input to pass to the function
     * @return the result of applying the function
     * @throws MapleDslBindingException if an error occurs during the function application
     */
    Object apply(Object bean, int value);
}
