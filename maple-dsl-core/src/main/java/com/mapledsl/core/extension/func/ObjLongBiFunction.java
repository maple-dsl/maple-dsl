package com.mapledsl.core.extension.func;

/**
 * Functional interface for a function that takes an object and a long value and returns an object.
 */
@FunctionalInterface
public interface ObjLongBiFunction {
    /**
     * Applies the given long value to the given bean using the specified function.
     *
     * @param bean  the bean object to apply the value to
     * @param value the long value to be applied
     * @return the result after applying the value to the bean
     */
    Object apply(Object bean, long value);
}
