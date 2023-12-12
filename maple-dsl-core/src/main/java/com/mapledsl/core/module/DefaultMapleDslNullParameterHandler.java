package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import org.jetbrains.annotations.NotNull;

public class DefaultMapleDslNullParameterHandler implements MapleDslParameterHandler<Object> {
    @Override
    public Class<Object> parameterType() {
        return Object.class;
    }

    @Override
    public @NotNull String apply(Object param, MapleDslConfiguration context) {
        return "NULL";
    }
}
