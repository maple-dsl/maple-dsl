package com.mapledsl.nebula.module;

import com.mapledsl.core.module.MapleDslParameterHandler;
import com.mapledsl.core.module.MapleDslParameterHandlerCollector;

import java.util.Map;

public class MapleNebulaDslParameterHandlerCollector implements MapleDslParameterHandlerCollector {
    @Override
    public String version() {
        return MapleNebulaDslModule.VERSION;
    }

    @Override
    public Map<Class<?>, MapleDslParameterHandler> get() {
        return null;
    }
}
