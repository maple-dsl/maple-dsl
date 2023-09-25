package com.mapledsl.core;

import com.mapledsl.core.module.MapleDslParameterHandler;
import com.mapledsl.core.module.MapleDslParameterHandlerCollector;

import java.util.Collections;
import java.util.Map;

public class MapleDslMockParameterHandlerCollector implements MapleDslParameterHandlerCollector {
    @Override
    public String version() {
        return MapleDslMockModule.VERSION;
    }

    @Override
    public Map<Class<?>, MapleDslParameterHandler> get() {
        return Collections.singletonMap(
                void.class, new MapleDslMockNullParameterHandler()
        );
    }

    static class MapleDslMockNullParameterHandler implements MapleDslParameterHandler {

        @Override
        public String apply(Object o, MapleDslConfiguration configuration) {
            return null;
        }
    }

}
