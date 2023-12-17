package com.mapledsl.neo4j.module;

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
public class MapleNeo4jDslResultHandlerCollector implements MapleDslResultHandlerCollector {
    @Override
    public String version() {
        return MapleNeo4jDslModule.VERSION;
    }

    @Override
    public Set<MapleDslResultHandler<?, ?>> resultHandlers() {
        return Arrays.stream(DefaultMapleNeo4jDslResultHandlers.values())
                .map(it -> it.handler)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MapleDslDefinitionResultHandler<?>> definitionResultHandlers() {
        return Collections.singleton(new DefaultMapleNeo4jDslDefinitionResultHandler());
    }

}
