package com.mapledsl.core.module;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a collector for Maple DSL result handlers.
 * It provides methods to retrieve the version, a set of result handlers, and a set of definition result handlers.
 */
public interface MapleDslResultHandlerCollector {
    String version();
    /**
     * Returns a set of Maple DSL result handlers.
     *
     * @return The set of Maple DSL result handlers.
     */
    Set<MapleDslResultHandler<?,?>> resultHandlers();
    /**
     * Retrieves a set of Maple DSL definition result handlers.
     *
     * @return The set of Maple DSL definition result handlers.
     */
    Set<MapleDslDefinitionResultHandler<?>> definitionResultHandlers();

    /**
     * Returns the default set of Maple DSL definition result handlers.
     *
     * @return The default set of Maple DSL definition result handlers.
     */
    static Set<MapleDslDefinitionResultHandler<?>> defaultDefinitionResultHandlers() {
        return Collections.emptySet();
    }

    /**
     * Returns a set of default Maple DSL result handlers.
     *
     * @return A set of default Maple DSL result handlers.
     */
    static Set<MapleDslResultHandler<?,?>> defaultResultHandlers() {
        return Arrays.stream(DefaultMapleDslResultHandlers.values())
                .flatMap(it -> Arrays.stream(it.handlers))
                .collect(Collectors.toSet());
    }
}