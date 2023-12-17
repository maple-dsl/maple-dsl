package com.mapledsl.neo4j.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.module.MapleDslResultHandler;
import org.neo4j.driver.Value;

import java.util.function.BiFunction;

/**
 * Represents a result handler for Maple Neo4j DSL queries.
 * It extends the Maple DSL result handler and provides a way to identify and transform the inbound data.
 *
 * @param <OUT> The type of the outbound data.
 */
public interface MapleNeo4jDslResultHandler<OUT> extends MapleDslResultHandler<Value, OUT> {
    @Override
    default Class<Value> inboundType() {
        return Value.class;
    }

    /**
     * Returns a MapleNeo4jDslResultHandler that can be used to identify and transform the inbound data.
     *
     * @param <OUT>     The type of the outbound data.
     * @param outbound  The class representing the outbound data.
     * @param transform A bi-function that takes a Value object and a MapleDslConfiguration object
     *                  and returns the transformed outbound data of type OUT.
     * @return A MapleNebulaDslResultHandler that applies the transform function to the inbound data.
     */
    static <OUT> MapleNeo4jDslResultHandler<OUT> identify(Class<OUT> outbound, BiFunction<Value, MapleDslConfiguration, OUT> transform) {
        return new MapleNeo4jDslResultHandler<OUT>() {

            @Override
            public OUT apply(Value in, MapleDslConfiguration context) {
                return transform.apply(in, context);
            }

            @Override
            public Class<OUT> outboundType() {
                return outbound;
            }
        };
    }
}
