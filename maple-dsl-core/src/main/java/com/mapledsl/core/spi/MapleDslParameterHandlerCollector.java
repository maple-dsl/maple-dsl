package com.mapledsl.core.spi;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
public interface MapleDslParameterHandlerCollector extends Supplier<Map<Class<?>, MapleDslParameterHandler>> {
    String version();
}
