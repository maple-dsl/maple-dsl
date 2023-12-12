package com.mapledsl.core.extension.func;

/**
 * Represents a function that accepts an object and returns a boolean value.
 * This is a functional interface whose functional method is {@link #applyAsBoolean(Object)}.
 */
@FunctionalInterface
public interface ToBooleanFunction {
    /**
     * Applies the function to the given object and returns a boolean value.
     *
     * @param bean the object to apply the function to
     * @return the boolean value computed by applying the function to the object
     */
    boolean applyAsBoolean(Object bean);
}
