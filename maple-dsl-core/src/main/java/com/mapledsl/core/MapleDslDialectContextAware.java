package com.mapledsl.core;

@FunctionalInterface
public interface MapleDslDialectContextAware {
    MapleDslDialectContextAware bind(MapleDslConfiguration context);
}
