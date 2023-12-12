package com.mapledsl.core.extension.func;

/**
 * The ObjBooleanConsumer interface represents an operation that accepts an object and a boolean value
 * and performs some action on the object based on the value.
 */
@FunctionalInterface
public interface ObjBooleanConsumer {
    /**
     * Accepts an object and a boolean value and performs some action on the object based on the value.
     *
     * @param bean The object on which the action is performed.
     * @param value The boolean value used to determine the action to be performed.
     */
    void accept(Object bean, boolean value);
}
