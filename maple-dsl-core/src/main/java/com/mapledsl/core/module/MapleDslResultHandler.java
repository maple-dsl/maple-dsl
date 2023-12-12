package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;

import java.util.function.BiFunction;

/**
 * Represents a handler for Maple DSL results.
 * It defines the inbound and outbound types that the handler can process,
 * and provides a method to transform the inbound type to the outbound type.
 *
 * @param <IN> The type of the inbound data.
 * @param <OUT> The type of the outbound data.
 */
public interface MapleDslResultHandler<IN, OUT> extends BiFunction<IN, MapleDslConfiguration, OUT> {
    /**
     * Returns the class representing the type of the inbound data for this Maple DSL result handler.
     *
     * @return The class representing the type of the inbound data.
     */
    Class<IN> inboundType();
    /**
     * Returns the class representing the type of the outbound data for this Maple DSL result handler.
     *
     * @return The class representing the type of the outbound data.
     */
    Class<OUT> outboundType();

    /**
     * Creates a MapleDslResultHandler instance that represents an identity transformation for the given inbound and outbound types.
     *
     * @param <IN>       The type of the inbound data.
     * @param <OUT>      The type of the outbound data.
     * @param inbound    The class representing the type of the inbound data.
     * @param outbound   The class representing the type of the outbound data.
     * @param transform  A function that performs the identity transformation on the inbound data.
     * @return A MapleDslResultHandler instance that represents an identity transformation.
     */
    static <IN, OUT> MapleDslResultHandler<IN, OUT> identify(Class<IN> inbound, Class<OUT> outbound, BiFunction<IN, MapleDslConfiguration, OUT> transform) {
        return new MapleDslResultHandler<IN, OUT>() {

            @Override
            public OUT apply(IN in, MapleDslConfiguration context) {
                return transform.apply(in, context);
            }

            @Override
            public Class<IN> inboundType() {
                return inbound;
            }

            @Override
            public Class<OUT> outboundType() {
                return outbound;
            }
        };
    }
}
