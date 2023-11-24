package com.mapledsl.core.module;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
public interface MapleDslResultHandlerCollector extends Supplier<Map<Class<?>, MapleDslResultHandler<?,?>>> {
    String version();

    static Map<Class<?>, MapleDslResultHandler<?,?>> defaultResultHandlers() {
        return Arrays.stream(DefaultMapleDslResultHandlers.values())
                .collect(Collectors.toMap(DefaultMapleDslResultHandlers::getResultType, DefaultMapleDslResultHandlers::getResultHandler));
    }
}