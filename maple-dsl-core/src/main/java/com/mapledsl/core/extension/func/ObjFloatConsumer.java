package com.mapledsl.core.extension.func;

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
