package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;

import java.util.function.BiFunction;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
public interface MapleDslResultHandler<IN, OUT> extends BiFunction<IN, MapleDslConfiguration, OUT> {
    Class<OUT> resultType();

    static <IN, OUT> MapleDslResultHandler<IN, OUT> newInstance(Class<OUT> resultType, BiFunction<IN, MapleDslConfiguration, OUT> func) {
        return new MapleDslResultHandler<IN, OUT>() {
            @Override
            public OUT apply(IN source, MapleDslConfiguration ctx) {
                return func.apply(source, ctx);
            }

            @Override
            public Class<OUT> resultType() {
                return resultType;
            }
        };
    }
}
