package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

/**
 * This abstract class {@code MapleDslDialectFunctionRender} is an implementation of the {@code AttributeRenderer} interface and
 * provides functionality for rendering Maple DSL functions in different dialects. It is also {@code MapleDslDialectAware},
 * {@code MapleDslDialectContextAware}, and {@code MapleDslDialectRenderHelper}.
 * <p></p>
 * The class provides the following methods:
 * - {@code toString(MapleDslDialectFunction, String, Locale)}: Converts a {@code MapleDslDialectFunction} object to a string representation.
 * - {@code bind(MapleDslConfiguration)}: Binds a {@code MapleDslConfiguration} object to the context.
 * <p></p>
 * This is an abstract class and cannot be instantiated directly. Instead, it should be extended by concrete implementations.
 * The concrete implementations should implement the abstract method {@code toFunction} which converts a {@code MapleDslDialectFunction}
 * object to a string representation specific to the dialect.
 * <p></p>
 * Usage examples of subclasses of {@code MapleDslDialectFunctionRender}:
 * - {@code MapleDslDialectMockFunctionRender}: implements the {@code toFunction} method for the Maple DSL mock module.
 * - {@code MapleNebulaDslDialectFunctionRender}: implements the {@code toFunction} method for the Maple DSL Nebula module.
 * - {@code MapleCypherDslDialectFunctionRender}: implements the {@code toFunction} method for the Maple DSL Cypher module.
 */
public abstract class MapleDslDialectFunctionRender implements AttributeRenderer<MapleDslDialectFunction>, MapleDslDialectAware, MapleDslDialectContextAware, MapleDslDialectRenderHelper {
    protected MapleDslConfiguration context;
    protected abstract String toFunction(MapleDslDialectFunction<?> value);

    @Override
    public String toString(MapleDslDialectFunction value, String formatString, Locale locale) {
        if (value == null)  return NULL;

        final String curFunction = toFunction(value);
        if (!value.hasNext()) return curFunction;

        final String nextFunction = toString(value.next, formatString, locale);
        if (nextFunction.equals(NULL)) return curFunction;

        return curFunction + COMMA + nextFunction;
    }

    @Override
    public MapleDslDialectFunctionRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }
}
