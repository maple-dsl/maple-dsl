package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.model.Model;

public final class MapleDslDialectSelection<M extends Model<?>> extends MapleDslDialectBase<M> {
    private final String[] columns;
    private final String[] aliases;
    private boolean all;
    public MapleDslDialectSelection<M> next;

    MapleDslDialectSelection(boolean all) {
        this.columns = null;
        this.aliases = null;
        this.all = all;
    }

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

    public String[] aliases() {
        return aliases;
    }

    public String[] columns() {
        return columns;
    }

    @Override
    public MapleDslDialectSelection<M> setOut(boolean out) {
        super.setOut(out);
        return this;
    }

    @Override
    public MapleDslDialectSelection<M> setIn(boolean in) {
        super.setIn(in);
        return this;
    }

    @Override
    public MapleDslDialectSelection<M> setE(boolean e) {
        super.setE(e);
        return this;
    }

    @Override
    public MapleDslDialectSelection<M> setV(boolean v) {
        super.setV(v);
        return this;
    }

    @Override
    public MapleDslDialectSelection<M> setInstantiatedAlias(String instantiatedAlias) {
        super.setInstantiatedAlias(instantiatedAlias);
        return this;
    }

    @Override
    public MapleDslDialectSelection<M> setInstantiatedLabel(String instantiatedLabel) {
        super.setInstantiatedLabel(instantiatedLabel);
        return this;
    }

    @Override
    public MapleDslDialectSelection<M> setInstantiatedLabelClazz(Class<M> instantiatedLabelClazz) {
        super.setInstantiatedLabelClazz(instantiatedLabelClazz);
        return this;
    }

    @Override
    MapleDslDialectSelection<M> merge(MapleDslDialectBase<M> merge) {
        super.merge(merge);
        return this;
    }
}
