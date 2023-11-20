package com.mapledsl.cypher;

import com.mapledsl.core.MapleDslDialectFunctionRender;
import com.mapledsl.core.condition.common.Func;
import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;
import com.mapledsl.cypher.module.MapleCypherDslModule;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class MapleCypherDslDialectFunctionRender extends MapleDslDialectFunctionRender {
    @Override
    protected String toFunction(MapleDslDialectFunction<?> value) {
        if (value == null) return NULL;
        if (!functionRenderMap.containsKey(value.func())) throw new UnsupportedOperationException("Unsupported Func: " + value.func().name());
        return functionRenderMap.get(value.func()).apply(value.ref(), value.column()) + AS + value.alias();
    }

    @Override
    public String dialect() {
        return MapleCypherDslModule.DIALECT;
    }

    private final Map<Func, BinaryOperator<String>> functionRenderMap = new EnumMap<Func, BinaryOperator<String>>(Func.class) {
        {
            put(Func.SUM, (ref, column) -> "SUM(" + ref + DOT + column + ")");
            put(Func.AVG, (ref, column) -> "AVG(" + ref + DOT + column + ")");
            put(Func.MAX, (ref, column) -> "MAX(" + ref + DOT + column + ")");
            put(Func.MIN, (ref, column) -> "MIN(" + ref + DOT + column + ")");
            put(Func.CNT, (ref, column) -> column == null || column.trim().isEmpty() ? "COUNT(*)" : "COUNT(" + ref + DOT + column + ")");
        }
    };
}
