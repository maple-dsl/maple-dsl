package com.mapledsl.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.StringJoiner;

public class MapleDslDialectMockSelectionRender extends MapleDslDialectSelectionRender implements MapleDslDialectRenderConstants {

    @Override
    protected String vertexRef(@NotNull String alias) {
        return "V" + DOT + alias;
    }

    @Override
    protected String edgeRef(@NotNull String alias) {
        return "E" + DOT + alias;
    }

    @Override
    protected String inVRef(@Nullable String label, @NotNull String alias) {
        return "IN" + BLANK + "V" + DOT + label + AS + alias;
    }

    @Override
    protected String outVRef(@Nullable String label, @NotNull String alias) {
        return "OUT" + BLANK + "V" + DOT + label + AS + alias;
    }

    @Override
    protected String vertex(@Nullable String label, String[] columns, String[] alias) {
        return render(VERTEX, label, columns, alias);
    }

    @Override
    protected String edge(@Nullable String label, String[] columns, String[] alias) {
        return render(EDGE, label, columns, alias);
    }

    @Override
    protected String inV(@Nullable String label, String[] columns, String[] alias) {
        return render(IN_VERTEX, label, columns, alias);
    }

    @Override
    protected String outV(@Nullable String label, String[] columns, String[] alias) {
        return render(OUT_VERTEX, label, columns, alias);
    }

    public static String render(String type, String label, String[] columns, String[] alias) {
        final StringJoiner builder = new StringJoiner(COMMA);
        for (int i = 0; i < columns.length; i++) {
            final String column = columns[i];
            final String alia = alias[i];
            if (label == null) builder.add(type + DOT + column + AS + alia);
            else builder.add(type + DOT + label + DOT + column + AS + alia);
        }

        return builder.toString();
    }

    @Override
    public String dialect() {
        return MapleDslMockModule.DIALECT;
    }
}
