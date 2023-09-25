package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.model.Model;

public class MapleDslDialectBase<M extends Model<?>> {
    protected String instantiatedLabel;
    protected Class<M> instantiatedLabelClazz;
    private boolean in = false, out = false, v = false, e = false;

    public String label(MapleDslConfiguration ctx) {
        if (instantiatedLabel != null && !instantiatedLabel.trim().isEmpty()) return instantiatedLabel;
        if (instantiatedLabelClazz == null) return null;
        return ctx.getLabel(instantiatedLabelClazz);
    }

    public MapleDslDialectBase<M> setInstantiatedLabel(String instantiatedLabel) {
        this.instantiatedLabel = instantiatedLabel;
        return this;
    }

    public MapleDslDialectBase<M> setInstantiatedLabelClazz(Class<M> instantiatedLabelClazz) {
        this.instantiatedLabelClazz = instantiatedLabelClazz;
        return this;
    }

    public MapleDslDialectBase<M> setIn(boolean in) {
        this.in = in;
        return this;
    }

    public MapleDslDialectBase<M> setOut(boolean out) {
        this.out = out;
        return this;
    }

    public MapleDslDialectBase<M> setV(boolean v) {
        this.v = v;
        return this;
    }

    public MapleDslDialectBase<M> setE(boolean e) {
        this.e = e;
        return this;
    }

    public boolean in() {
        return in;
    }

    public boolean out() {
        return out;
    }

    public boolean v() {
        return v;
    }

    public boolean e() {
        return e;
    }
}
