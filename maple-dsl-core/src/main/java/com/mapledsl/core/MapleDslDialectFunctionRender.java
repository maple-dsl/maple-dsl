package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

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
