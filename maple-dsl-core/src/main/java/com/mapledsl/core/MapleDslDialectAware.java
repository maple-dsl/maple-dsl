package com.mapledsl.core;

/**
 * The MapleDslDialectAware interface represents a functional interface that provides the dialect of a Maple DSL.
 */
@FunctionalInterface
public interface MapleDslDialectAware {
    String dialect();
}
