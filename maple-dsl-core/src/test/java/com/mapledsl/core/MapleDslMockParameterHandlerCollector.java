package com.mapledsl.core;

import com.mapledsl.core.module.MapleDslParameterHandler;
import com.mapledsl.core.module.MapleDslParameterHandlerCollector;

import java.util.Collections;
import java.util.Set;

public class MapleDslMockParameterHandlerCollector implements MapleDslParameterHandlerCollector {
    @Override
    public String version() {
        return MapleDslMockModule.VERSION;
    }

    @Override
    public MapleDslParameterHandler<Object> nullParameterHandler() {
        return null;
    }

    @Override
    public Set<MapleDslParameterHandler<?>> parameterHandlers() {
        return Collections.emptySet();
    }
}
