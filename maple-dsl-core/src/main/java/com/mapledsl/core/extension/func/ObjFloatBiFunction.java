package com.mapledsl.core.extension.func;

/**
 * Functional interface for a function that takes an object and a float value as input and produces an object as output.
 */
@FunctionalInterface
public interface ObjFloatBiFunction {
    /**
     * Applies a function to an object and a float value, producing an object as output.
     *
     * @param bean  the input object
     * @param value the float value
     * @return the output object
     */
    Object apply(Object bean, float value);
}
