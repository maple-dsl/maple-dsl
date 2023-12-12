package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.condition.common.Func;
import com.mapledsl.core.model.Model;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@API(status = API.Status.INTERNAL)
public final class MapleDslDialectFunction<M extends Model<?>> extends MapleDslDialectBase<M> {
    private final @Nullable String column;
    private final @NotNull String alias;
    private final @NotNull Func func;
    public MapleDslDialectFunction<M> next;

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
