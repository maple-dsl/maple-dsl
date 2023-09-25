package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

public class MapleDslDialectSelection<M extends Model<?>> extends MapleDslDialectBase<M> {
    private @NotNull final String[] columns;
    private @NotNull final String[] aliases;

    public MapleDslDialectSelection<M> next;
    public boolean all;

    MapleDslDialectSelection(String column) {
        this(column, column);
    }

    MapleDslDialectSelection(String[] column) {
        this(column, column);
    }

    MapleDslDialectSelection(String column, String alias) {
        this.columns = new String[] { column };
        this.aliases = new String[] { alias };
    }

    MapleDslDialectSelection(String[] columns, String[] aliases) {
        this.columns = columns;
        this.aliases = aliases;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean isAllPresent() {
        return all;
    }

    public boolean isNotPresent() {
        return columns == null;
    }

    public String alias() {
        return aliases[0];
    }

    public String[] aliases() {
        return aliases;
    }

    public String[] columns() {
        return columns;
    }
}
