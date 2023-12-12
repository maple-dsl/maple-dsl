package com.mapledsl.core.extension.func;

/**
 * Represents an operation that accepts two input arguments and performs an action on them.
 * This is a functional interface whose functional method is {@link #accept(Object, char)}.
 *
 * @param <T> the type of the first argument
 */
@FunctionalInterface
public interface ObjCharConsumer<T> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param value the second input argument
     */
    void accept(T t, char value);
}
