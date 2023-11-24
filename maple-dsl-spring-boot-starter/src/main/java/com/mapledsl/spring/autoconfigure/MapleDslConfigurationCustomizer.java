package com.mapledsl.spring.autoconfigure;

import com.mapledsl.core.MapleDslConfiguration;

@FunctionalInterface
public interface MapleDslConfigurationCustomizer {
    void customize(MapleDslConfiguration configuration);
}
