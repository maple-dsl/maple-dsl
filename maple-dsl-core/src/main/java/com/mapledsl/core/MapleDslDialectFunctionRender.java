package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;
import org.stringtemplate.v4.AttributeRenderer;

public abstract class MapleDslDialectFunctionRender extends MapleDslDialectBaseRender implements AttributeRenderer<MapleDslDialectFunction> {

    @Override
    public MapleDslDialectFunctionRender bind(MapleDslConfiguration context) {
        super.bind(context);
        return this;
    }
}
