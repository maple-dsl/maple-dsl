package com.mapledsl.core.extension.func;

/**
 * Functional interface representing an operation that accepts an object of type T and a float value
 * and performs some operation on them.
 *
 * @param <T> the type of the object argument
 */
@FunctionalInterface
public interface ObjFloatConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param value the second input argument
     */
    void accept(T t, float value);
}
