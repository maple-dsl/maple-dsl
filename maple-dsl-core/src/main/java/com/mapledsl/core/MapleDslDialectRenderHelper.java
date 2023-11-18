package com.mapledsl.core;

public interface MapleDslDialectRenderHelper {
    String NULL  = "\r", AS = " AS ", COMMA = ",", DOT = ".", PAREN_L = "(", PAREN_R = ")", BLANK = " ";

    static String escaped(Object value) {
        return "\"" + value + "\"";
    }

    static String numeric(Number value) {
        return identify(value.longValue());
    }

    static String identify(Object value) {
        return value + "";
    }
}
