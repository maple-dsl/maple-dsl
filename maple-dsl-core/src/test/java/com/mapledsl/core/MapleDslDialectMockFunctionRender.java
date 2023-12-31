package com.mapledsl.core;

import com.mapledsl.core.condition.common.Func;
import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class MapleDslDialectMockFunctionRender extends MapleDslDialectFunctionRender implements MapleDslDialectRenderHelper {

    @Override
    protected String toFunction(MapleDslDialectFunction<?> value) {
        if (value == null) return NULL;
        if (functionRenderMap.containsKey(value.func())) throw new UnsupportedOperationException("Unsupported Func: " + value.func().name());
        return functionRenderMap.get(value.func()).apply(value.column()) + AS + value.alias();
    }

    @Override
    public String dialect() {
        return MapleDslMockModule.DIALECT;
    }

    private final Map<Func, UnaryOperator<String>> functionRenderMap = new EnumMap<Func, UnaryOperator<String>>(Func.class) {
        {
            put(Func.SUM, column -> "SUM(" + column + ")");
            put(Func.AVG, column -> "AVG(" + column + ")");
            put(Func.MAX, column -> "MAX(" + column + ")");
            put(Func.MIN, column -> "MIN(" + column + ")");
            put(Func.CNT, column -> column == null || column.trim().isEmpty() ? "COUNT(*)" : "COUNT(" + column + ")");
        }
    };
}
