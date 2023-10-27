package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
@FunctionalInterface
public interface MapleDslParameterHandler extends BiFunction<Object, MapleDslConfiguration, String> {

    default Object compose(@NotNull Object parameter, MapleDslConfiguration configuration) {
        return parameter;
    }

    static MapleDslParameterHandler identity() {
        return (parameter, ctx) -> parameter + "";
    }

    static MapleDslParameterHandler escaped() {
        return (parameter, ctx) -> "\"" + parameter + "\"";
    }
}