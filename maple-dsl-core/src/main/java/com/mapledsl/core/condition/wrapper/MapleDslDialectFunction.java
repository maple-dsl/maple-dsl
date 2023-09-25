package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.condition.common.Func;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MapleDslDialectFunction {
    private final @Nullable String column;
    private final @NotNull String alias;
    private final @NotNull Func func;
    public MapleDslDialectFunction next;

    MapleDslDialectFunction(@NotNull Func func, @NotNull String alias) {
        this.column = null;
        this.alias = alias;
        this.func = func;
    }

    MapleDslDialectFunction(@NotNull String column, @NotNull Func func, @NotNull String alias) {
        this.column = column;
        this.alias = alias;
        this.func = func;
    }

    public String column() {
        return column;
    }

    public String alias() {
        return alias;
    }

    public Func func() {
        return func;
    }

    public boolean hasNext() {
        return next != null;
    }
}