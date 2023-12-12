package com.mapledsl.nebula.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.module.MapleDslResultHandler;
import com.vesoft.nebula.Value;

import java.util.function.BiFunction;

/**
 * Represents a handler for Maple Nebula DSL (Domain Specific Language) results.
 * It extends the MapleDslResultHandler interface and provides a specific implementation
 * for the inboundType() method.
 *
 * @param <OUT> The type of the outbound data.
 */
public interface MapleNebulaDslResultHandler<OUT> extends MapleDslResultHandler<Value, OUT> {
    @Override
    default Class<Value> inboundType() {
        return Value.class;
    }

    /**
     * Returns a MapleNebulaDslResultHandler that can be used to identify and transform the inbound data.
     *
     * @param <OUT>     The type of the outbound data.
     * @param outbound  The class representing the outbound data.
     * @param transform A bi-function that takes a Value object and a MapleDslConfiguration object
     *                  and returns the transformed outbound data of type OUT.
     * @return A MapleNebulaDslResultHandler that applies the transform function to the inbound data.
     */
    static <OUT> MapleNebulaDslResultHandler<OUT> identify(Class<OUT> outbound, BiFunction<Value, MapleDslConfiguration, OUT> transform) {
        return new MapleNebulaDslResultHandler<OUT>() {

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
