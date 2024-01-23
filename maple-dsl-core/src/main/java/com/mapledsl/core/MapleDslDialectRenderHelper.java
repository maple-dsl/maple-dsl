package com.mapledsl.core;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.mapledsl.core.model.ID;

/**
 * The MapleDslDialectRenderHelper interface defines a set of helper methods for rendering DSL expressions
 * in the Maple DSL dialect.
 */
public interface MapleDslDialectRenderHelper {
    String NULL  = "\r", AS = " AS ", COMMA = ",", DOT = ".", PAREN_L = "(", PAREN_R = ")", BLANK = " ";

    Escaper quoteEscaper = Escapers.builder().addEscape('\"', "\\\"").build();

    static String quote(String value) {
        return "\"" + quoteEscaper.escape(value) + "\"";
    }

    static String numeric(Number value) {
        return identify(value.longValue());
    }

    static String identify(Object value) {
        if (value instanceof ID) {
            final ID ID = (ID) value;
            return ID.quoted() ? quote(ID.fragment()) : ID.fragment();
        }

        return value instanceof CharSequence ? quote(value + "") : value + "";
    }
}
