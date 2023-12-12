package com.mapledsl.core.extension.func;

/**
 * Functional interface for a function that accepts an object and a char value
 * and produces a result of type Object.
 */
@FunctionalInterface
public interface ObjCharBiFunction {
    /**
     * Applies a function that accepts an object and a char value.
     *
     * @param bean the object to apply the function on
     * @param value the char value to pass to the function
     * @return the result of applying the function
     */
    Object apply(Object bean, char value);
}
