package com.mapledsl.core.module;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The MapleDslParameterHandlerCollector interface represents a collector of MapleDslParameterHandlers.
 */
public interface MapleDslParameterHandlerCollector {
    String version();
    MapleDslParameterHandler<Object> nullParameterHandler();
    Set<MapleDslParameterHandler<?>> parameterHandlers();

    static Set<MapleDslParameterHandler<?>> defaultParameterHandlers() {
        return Arrays.stream(DefaultMapleDslParameterHandlers.values()).map(DefaultMapleDslParameterHandlers::parameterHandler).collect(Collectors.toSet());
    }

    static MapleDslParameterHandler<Object> defaultNullParameterHandler() {
        return new DefaultMapleDslNullParameterHandler();
    }
}
