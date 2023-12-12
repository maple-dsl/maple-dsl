package com.mapledsl.core.extension.func;

/**
 * Represents a function that accepts an object and returns a float value.
 *
 * <p>This is a functional interface whose functional method is {@link #applyAsFloat(Object)}.</p>
 */
@FunctionalInterface
public interface ToFloatFunction {
    /**
     * Applies this function to the given object and returns a float value.
     *
     * @param bean the object to apply the function to
     * @return the float value produced by applying the function
     */
    float applyAsFloat(Object bean);
}
