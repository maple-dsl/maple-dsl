package com.mapledsl.nebula;

import com.mapledsl.core.MapleDslDialectFunctionRender;
import com.mapledsl.core.condition.common.Func;
import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;
import com.mapledsl.nebula.module.MapleNebulaDslModule;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * The MapleNebulaDslDialectFunctionRender class is a concrete implementation of the MapleDslDialectFunctionRender abstract class.
 * It provides functionality for rendering Maple DSL functions specific to the Nebula dialect.
 */
public class MapleNebulaDslDialectFunctionRender extends MapleDslDialectFunctionRender {
    @Override
    protected String toFunction(MapleDslDialectFunction<?> value) {
        if (value == null) return NULL;
        if (!functionRenderMap.containsKey(value.func())) throw new UnsupportedOperationException("Unsupported Func: " + value.func().name());
        return functionRenderMap.get(value.func()).apply(value.column()) + AS + value.alias();
    }

    @Override
    public String dialect() {
        return MapleNebulaDslModule.DIALECT;
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