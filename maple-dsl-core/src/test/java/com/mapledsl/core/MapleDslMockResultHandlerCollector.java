package com.mapledsl.core;

import com.mapledsl.core.module.MapleDslResultHandler;
import com.mapledsl.core.module.MapleDslResultHandlerCollector;

import java.util.Collections;
import java.util.Map;

public class MapleDslMockResultHandlerCollector implements MapleDslResultHandlerCollector {
    @Override
    public String version() {
        return MapleDslMockModule.VERSION;
    }

    @Override
    public Map<Class<?>, MapleDslResultHandler<?, ?>> get() {
        return Collections.singletonMap(
                Object.class, new MapleDslMockDefaultResultHandler()
        );
    }

    static class MapleDslMockDefaultResultHandler implements MapleDslResultHandler<Object, Object> {

        @Override
        public Object apply(Object o, MapleDslConfiguration configuration) {
            return null;
        }
    }
}
