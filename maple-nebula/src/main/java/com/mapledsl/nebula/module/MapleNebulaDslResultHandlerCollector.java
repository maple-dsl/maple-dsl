package com.mapledsl.nebula.module;

import com.mapledsl.core.module.MapleDslResultHandler;
import com.mapledsl.core.module.MapleDslResultHandlerCollector;

import java.util.Map;

public class MapleNebulaDslResultHandlerCollector implements MapleDslResultHandlerCollector {
    @Override
    public String version() {
        return MapleNebulaDslModule.VERSION;
    }

    @Override
    public Map<Class<?>, MapleDslResultHandler<?, ?>> get() {
        return null;
    }
}
