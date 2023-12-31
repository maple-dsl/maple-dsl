package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.model.Model;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL)
public class MapleDslDialectBase<M extends Model<?>> {
    protected String instantiatedLabel;
    protected Class<M> instantiatedLabelClazz;
    protected String instantiatedAlias;
    private boolean in = false, out = false, v = false, e = false;

    public String label(MapleDslConfiguration ctx) {
        if (instantiatedLabel != null && !instantiatedLabel.trim().isEmpty()) return instantiatedLabel;
        if (instantiatedLabelClazz == null) return null;
        return ctx.label(instantiatedLabelClazz);
    }

    MapleDslDialectBase<M> merge(MapleDslDialectBase<M> merge) {
        this.instantiatedAlias = merge.instantiatedAlias;
        this.instantiatedLabel = merge.instantiatedLabel;
        this.instantiatedLabelClazz = merge.instantiatedLabelClazz;
        this.in = merge.in; this.out = merge.out; this.v = merge.v; this.e = merge.e;
        return this;
    }

    public MapleDslDialectBase<M> setInstantiatedLabel(String instantiatedLabel) {
        this.instantiatedLabel = instantiatedLabel;
        return this;
    }

    public MapleDslDialectBase<M> setInstantiatedLabelClazz(Class<M> instantiatedLabelClazz) {
        this.instantiatedLabelClazz = instantiatedLabelClazz;
        return this;
    }

    public MapleDslDialectBase<M> setInstantiatedAlias(String instantiatedAlias) {
        this.instantiatedAlias = instantiatedAlias;
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

    public String ref() {
        return instantiatedAlias;
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
