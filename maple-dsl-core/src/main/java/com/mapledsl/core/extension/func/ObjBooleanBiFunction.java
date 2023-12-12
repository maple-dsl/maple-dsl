package com.mapledsl.core.extension.func;

/**
 * Represents a functional interface that takes an object and a boolean value as input
 * and returns an object as output. Used for applying a function to a bean and a boolean value.
 */
@FunctionalInterface
public interface ObjBooleanBiFunction {
    /**
     * Applies a function to a bean and a boolean value.
     *
     * @param bean  the object to apply the function to
     * @param value the boolean value to pass to the function
     * @return the result of applying the function to the bean and the boolean value
     */
    Object apply(Object bean, boolean value);
}
