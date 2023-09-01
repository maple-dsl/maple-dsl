package com.mapledsl.core;

import com.mapledsl.core.MapleDslDialectSelectionRender.SelectionRendererModel;
import com.mapledsl.core.condition.common.T;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

public abstract class MapleDslDialectSelectionRender implements AttributeRenderer<SelectionRendererModel<?>> {
    protected MapleDslConfiguration context;

    protected abstract String vertexRef(@Nullable String label, @NotNull String alias);
    protected abstract String edgeRef(@Nullable String label, @NotNull String alias);
    protected abstract String inVRef(@Nullable String label, @NotNull String alias);
    protected abstract String outVRef(@Nullable String label, @NotNull String alias);

    protected abstract String vertex(@Nullable String label, String[] columns, String[] alias);
    protected abstract String edge(@Nullable String label, String[] columns, String[] alias);
    protected abstract String inV(@Nullable String label, String[] columns, String[] alias);
    protected abstract String outV(@Nullable String label, String[] columns, String[] alias);

    @Override
    @SuppressWarnings("ClassEscapesDefinedScope")
    public final String toString(SelectionRendererModel<?> value, String formatString, Locale locale) {
        if (value.base == null)         return "";
        if (value.base.isNotPresent())  return "";
        if (value.base.isAllPresent())  {
            if (value.v)    return vertexRef(value.base.label(context), value.base.alias());
            if (value.e)    return edgeRef(value.base.label(context), value.base.alias());
            if (value.in)   return inVRef(value.base.label(context), value.base.alias());
            if (value.out)  return outVRef(value.base.label(context), value.base.alias());

            throw new MapleDslBindingException("");
        }

        if (value.v)    return vertex(value.base.label(context), value.base.columns(), value.base.aliases());
        if (value.e)    return edge(value.base.label(context), value.base.columns(), value.base.aliases());
        if (value.in)   return inV(value.base.label(context), value.base.columns(), value.base.aliases());
        if (value.out)  return outV(value.base.label(context), value.base.columns(), value.base.aliases());

        throw new MapleDslBindingException("");
    }

    public MapleDslDialectSelectionRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }

    static <V extends Model.V> SelectionRendererModel<V> v(T<V> base) {
        return new SelectionRendererModel<V>(base) {{ v = true; }};
    }

    static <E extends Model.E> SelectionRendererModel<E> e(T<E> base) {
        return new SelectionRendererModel<E>(base) {{ e = true; }};
    }

    static <V extends Model.V> SelectionRendererModel<V> in(T<V> base) {
        return new SelectionRendererModel<V>(base) {{ in = true; }};
    }

    static <V extends Model.V> SelectionRendererModel<V> out(T<V> base) {
        return new SelectionRendererModel<V>(base) {{ out = true; }};
    }

    static class SelectionRendererModel<M extends Model<?>> {
        T<M> base;
        boolean in = false, out = false, v = false, e = false;

        SelectionRendererModel(T<M> base) {
            this.base = base;
        }
    }
}
