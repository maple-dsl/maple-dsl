package com.mapledsl.core;

import com.mapledsl.core.MapleDslDialectPredicateRender.PredicateRendererModel;
import com.mapledsl.core.MapleDslDialectSelectionRender.SelectionRendererModel;
import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.condition.common.P;
import com.mapledsl.core.condition.common.T;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

public abstract class MapleDslDialectPredicateRender implements AttributeRenderer<PredicateRendererModel<?>> {
    protected MapleDslConfiguration context;

    protected abstract String vertex(@Nullable String label, String column);
    protected abstract String edge(@Nullable String label, String column);
    protected abstract String inV(@Nullable String label, String column);
    protected abstract String outV(@Nullable String label, String column);

    protected abstract String op(OP op);

    private String column(PredicateRendererModel<?> predicate) {
        if (predicate.v)    return vertex(predicate.base.label(context), predicate.base.column());
        if (predicate.e)    return edge(predicate.base.label(context), predicate.base.column());
        if (predicate.in)   return inV(predicate.base.label(context), predicate.base.column());
        if (predicate.out)  return outV(predicate.base.label(context), predicate.base.column());

        throw new MapleDslBindingException("");
    }

    @Override
    @SuppressWarnings("ClassEscapesDefinedScope")
    public final String toString(PredicateRendererModel<?> predicate, String formatString, Locale locale) {
        if (predicate.base == null) return "";

        final String column = column(predicate);
        final String op     = op(predicate.base.op());
        final String value  = predicate.base.value(context);

        if (!predicate.base.hasNext()) return column + op + value;
        final String nestConnection = op(predicate.base.connection());
        final String nestPredicate = toString(predicate.next(), formatString, locale);

        if (nestPredicate.trim().isEmpty()) return column + op + value;
        return "(" + column + op + value + nestPredicate + nestConnection + ")";
    }

    public MapleDslDialectPredicateRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }

    static <V extends Model.V> PredicateRendererModel<V> v(P<V> base) {
        return new PredicateRendererModel<V>(base) {{ v = true; }};
    }

    static <E extends Model.E> PredicateRendererModel<E> e(P<E> base) {
        return new PredicateRendererModel<E>(base) {{ e = true; }};
    }

    static <V extends Model.V> PredicateRendererModel<V> in(P<V> base) {
        return new PredicateRendererModel<V>(base) {{ in = true; }};
    }

    static <V extends Model.V> PredicateRendererModel<V> out(P<V> base) {
        return new PredicateRendererModel<V>(base) {{ out = true; }};
    }

    static class PredicateRendererModel<M extends Model<?>> {
        P<M> base;
        boolean in = false, out = false, v = false, e = false;

        PredicateRendererModel(P<M> base) {
            this.base = base;
        }

        PredicateRendererModel<M> next() {
            base = base.next();
            return this;
        }
    }
}
