package com.mapledsl.core;

public abstract class MapleDslDialectBaseRender {
    protected static final String NULL  = "\r", AS = " AS ", COMMA = ",", DOT = ".", PAREN_L = "(", PAREN_R = ")", BLANK = " ";

    public abstract String dialect();
    protected MapleDslConfiguration context;

    public MapleDslDialectBaseRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }
}
