package com.mapledsl.nebula;

import com.mapledsl.core.MapleDslDialectFunctionRender;
import com.mapledsl.core.condition.common.Func;
import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;

public class MapleNebulaDslDialectFunctionRender extends MapleDslDialectFunctionRender {

    @Override
    public String toString(MapleDslDialectFunction value, String formatString, Locale locale) {
        if (value == null)  return NULL;

        if (!value.hasNext()) return toFunction(value);
        final String nextFunction = toString(value.next, formatString, locale);
        if (nextFunction.trim().isEmpty()) return toFunction(value);
        return toFunction(value) + COMMA + nextFunction;
    }

    private String toFunction(MapleDslDialectFunction value) {
        if (value == null) return NULL;
        if (functionRenderMap.containsKey(value.func())) throw new UnsupportedOperationException("Unsupported Func: " + value.func().name());
        return functionRenderMap.get(value.func()).apply(value.column()) + AS + value.alias();
    }

    @Override
    public String dialect() {
        return "nebula";
    }

    private final Map<Func, UnaryOperator<String>> functionRenderMap = new EnumMap<Func, UnaryOperator<String>>(Func.class) {
        {
            put(Func.SUM, column -> "SUM($-." + column + ")");
            put(Func.AVG, column -> "AVG($-." + column + ")");
            put(Func.MAX, column -> "MAX($-." + column + ")");
            put(Func.MIN, column -> "MIN($-." + column + ")");
            put(Func.CNT, column -> column == null || column.trim().isEmpty() ? "COUNT(*)" : "COUNT($-." + column + ")");
        }
    };
}