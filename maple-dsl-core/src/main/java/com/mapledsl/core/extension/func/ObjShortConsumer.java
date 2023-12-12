package com.mapledsl.core.extension.func;

/**
 * Functional interface that represents an operation that accepts an object of type T and a short value,
 * and performs some action on them.
 *
 * @param <T> the type of the object argument
 */
@FunctionalInterface
public interface ObjShortConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param value the second input argument
     */
    void accept(T t, short value);
}
