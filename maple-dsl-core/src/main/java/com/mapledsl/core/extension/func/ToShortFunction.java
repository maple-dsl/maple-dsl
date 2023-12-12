package com.mapledsl.core.extension.func;

/**
 * Represents a function that accepts an object and returns a short value.
 *
 * @since [Specify the version when this class/feature was introduced]
 */
@FunctionalInterface
public interface ToShortFunction {
    /**
     * Applies the function to the specified bean and returns a short value.
     *
     * @param bean the object to apply the function on
     * @return the short value computed by the function
     */
    short applyAsShort(Object bean);
}
