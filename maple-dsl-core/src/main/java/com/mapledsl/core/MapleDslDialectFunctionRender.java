package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;
import org.stringtemplate.v4.AttributeRenderer;

public abstract class MapleDslDialectFunctionRender implements AttributeRenderer<MapleDslDialectFunction>, MapleDslDialectAware, MapleDslDialectContextAware, MapleDslDialectRenderHelper {
    protected MapleDslConfiguration context;

    @Override
    public MapleDslDialectFunctionRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }
}
