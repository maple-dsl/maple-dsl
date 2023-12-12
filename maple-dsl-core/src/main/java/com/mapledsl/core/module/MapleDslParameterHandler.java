package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * The MapleDslParameterHandler interface represents a functional interface that can be used to process parameters in a Maple DSL configuration.
 * It extends the BiFunction interface, which takes two arguments: the parameter value and the MapleDslConfiguration context, and returns a String value.
 *
 * @param <P> the type of the parameter to be processed
 */
public interface MapleDslParameterHandler<P> extends BiFunction<P, MapleDslConfiguration, String> {
    Class<P> parameterType();

    /**
     * Process the parameter as string value
     *
     * @param param   parameter value
     * @param context configuration(globalLocale,globalDateFormat,etc)
     * @return parameter string value
     */
    @NotNull String apply(P param, MapleDslConfiguration context);

    static <P> MapleDslParameterHandler<P> identify(Class<P> parameterType) {
        return new MapleDslParameterHandler<P>() {
            @Override
            public @NotNull String apply(P param, MapleDslConfiguration context) {
                return param.toString();
            }

            @Override
            public Class<P> parameterType() {
                return parameterType;
            }
        };
    }
}