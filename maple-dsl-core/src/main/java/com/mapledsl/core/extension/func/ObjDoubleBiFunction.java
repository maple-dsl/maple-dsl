package com.mapledsl.core.extension.func;

/**
 * Functional interface representing a function that accepts an object and a double value.
 * It produces a result of type Object.
 */
@FunctionalInterface
public interface ObjDoubleBiFunction {
    /**
     * Applies a function to an object and a double value.
     *
     * @param bean  the object to apply the function to
     * @param value the double value to pass as an argument to the function
     * @return the result of applying the function to the object and the value
     */
    Object apply(Object bean, double value);
}
