package com.mapledsl.core.module;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
public interface MapleDslParameterHandlerCollector extends Supplier<Map<Class<?>, MapleDslParameterHandler>> {
    String version();

    static Map<Class<?>, MapleDslParameterHandler> defaultParameterHandlers() {
        return Arrays.stream(DefaultMapleDslParameterHandlers.values())
                .collect(Collectors.toMap(it -> it.parameterType, Function.identity()));
    }
}
