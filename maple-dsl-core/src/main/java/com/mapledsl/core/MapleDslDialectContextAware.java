package com.mapledsl.core;

/**
 * The MapleDslDialectContextAware interface represents a functional interface that binds a MapleDslConfiguration context
 * to an object. Objects implementing this interface can be used to provide specific behavior based on the context in which
 * they are bound.
 */
@FunctionalInterface
public interface MapleDslDialectContextAware {
    MapleDslDialectContextAware bind(MapleDslConfiguration context);
}
