package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
public interface MapleDslParameterHandler extends BiFunction<Object, MapleDslConfiguration, String> {
    Class<?> parameterType();

    default Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration configuration) {
        return parameter;
    }
}