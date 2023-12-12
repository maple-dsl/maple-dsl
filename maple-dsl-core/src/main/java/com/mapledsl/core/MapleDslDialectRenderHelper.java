package com.mapledsl.core;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

/**
 * The MapleDslDialectRenderHelper interface defines a set of helper methods for rendering DSL expressions
 * in the Maple DSL dialect.
 */
public interface MapleDslDialectRenderHelper {
    String NULL  = "\r", AS = " AS ", COMMA = ",", DOT = ".", PAREN_L = "(", PAREN_R = ")", BLANK = " ";

    Escaper quoteEscaper = Escapers.builder().addEscape('\"', "\\\"").build();

    static String quote(Object value) {
        return "\"" + quoteEscaper.escape(value.toString()) + "\"";
    }

    static String numeric(Number value) {
        return identify(value.longValue());
    }

    static String identify(Object value) {
        return value + "";
    }
}
