package com.mapledsl.core;

import com.mapledsl.core.condition.common.OP;
import org.jetbrains.annotations.Nullable;

public class MapleDslDialectMockPredicateRender extends MapleDslDialectPredicateRender implements MapleDslDialectRenderConstants {
    @Override
    protected String vertex(@Nullable String label, String column) {
        return render(VERTEX, label, column);
    }

    @Override
    protected String edge(@Nullable String label, String column) {
        return render(EDGE, label, column);
    }

    @Override
    protected String inV(@Nullable String label, String column) {
        return render(IN_VERTEX, label, column);
    }

    @Override
    protected String outV(@Nullable String label, String column) {
        return render(OUT_VERTEX, label, column);
    }

    @Override
    protected String op(OP op) {
        return op.name();
    }

    public static String render(String type, String label, String column) {
        if (label == null || label.trim().isEmpty()) return type + DOT + column;
        return type + DOT + label + DOT + column;
    }

    @Override
    public String dialect() {
        return MapleDslMockModule.DIALECT;
    }
}
