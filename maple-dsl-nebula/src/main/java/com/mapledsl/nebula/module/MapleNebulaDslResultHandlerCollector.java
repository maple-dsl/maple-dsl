package com.mapledsl.nebula.module;

import com.mapledsl.core.module.MapleDslDefinitionResultHandler;
import com.mapledsl.core.module.MapleDslResultHandler;
import com.mapledsl.core.module.MapleDslResultHandlerCollector;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a collector for Maple DSL result handlers specific to the Maple Nebula dialect.
 */
public class MapleNebulaDslResultHandlerCollector implements MapleDslResultHandlerCollector {
    @Override
    public String version() {
        return MapleNebulaDslModule.VERSION;
    }

    @Override
    public Set<MapleDslResultHandler<?, ?>> resultHandlers() {
        return Arrays.stream(DefaultMapleNebulaDslResultHandlers.values())
                .map(it -> it.handler)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MapleDslDefinitionResultHandler<?>> definitionResultHandlers() {
        return Collections.singleton(new DefaultMapleNebulaDslDefinitionResultHandler());
    }

}
