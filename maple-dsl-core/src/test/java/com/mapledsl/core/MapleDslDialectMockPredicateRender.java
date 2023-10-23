package com.mapledsl.core;

import com.mapledsl.core.condition.common.OP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.mapledsl.core.MapleDslDialectRenderConstants.VERTEX;
import static com.mapledsl.core.MapleDslDialectRenderConstants.EDGE;
import static com.mapledsl.core.MapleDslDialectRenderConstants.IN_VERTEX;
import static com.mapledsl.core.MapleDslDialectRenderConstants.OUT_VERTEX;

public class MapleDslDialectMockPredicateRender extends MapleDslDialectPredicateRender {
    @Override
    protected String vertex(@NotNull String ref, @Nullable String label, String column) {
        return render(VERTEX, label, column);
    }

    @Override
    protected String edge(@NotNull String ref, @Nullable String label, String column) {
        return render(EDGE, label, column);
    }

    @Override
    protected String inV(@NotNull String ref, @Nullable String label, String column) {
        return render(IN_VERTEX, label, column);
    }

    @Override
    protected String outV(@NotNull String ref, @Nullable String label, String column) {
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
