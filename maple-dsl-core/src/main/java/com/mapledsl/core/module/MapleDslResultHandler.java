package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import java.util.function.BiFunction;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
@FunctionalInterface
public interface MapleDslResultHandler<IN, OUT> extends BiFunction<IN, MapleDslConfiguration, OUT> {
}
