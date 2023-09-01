package com.mapledsl.core.module;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
public interface MapleDslResultHandlerCollector extends Supplier<Map<Class<?>, MapleDslResultHandler<?,?>>> {
    String version();
}