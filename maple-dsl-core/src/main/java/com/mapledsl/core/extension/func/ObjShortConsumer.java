package com.mapledsl.core.extension.func;

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
