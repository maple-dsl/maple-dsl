package com.mapledsl.core;

import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.condition.wrapper.MapleDslDialectPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

public abstract class MapleDslDialectPredicateRender extends MapleDslDialectBaseRender implements AttributeRenderer<MapleDslDialectPredicate> {
    protected abstract String vertex(@NotNull String ref, @Nullable String label, String column);
    protected abstract String edge(@NotNull String ref, @Nullable String label, String column);
    protected abstract String inV(@NotNull String ref, @Nullable String label, String column);
    protected abstract String outV(@NotNull String ref, @Nullable String label, String column);

    protected abstract String op(OP op);

    private String column(MapleDslDialectPredicate<?> predicate) {
        if (predicate.v())    return vertex(predicate.ref(), predicate.label(context), predicate.column());
        if (predicate.e())    return edge(predicate.ref(), predicate.label(context), predicate.column());
        if (predicate.in())   return inV(predicate.ref(), predicate.label(context), predicate.column());
        if (predicate.out())  return outV(predicate.ref(), predicate.label(context), predicate.column());

        throw new UnsupportedOperationException();
    }

    @Override
    public final String toString(MapleDslDialectPredicate predicate, String formatString, Locale locale) {
        if (predicate == null) return NULL;

        final String column = column(predicate);
        final String op     = op(predicate.op());
        final String value  = predicate.value(context);

        if (!predicate.hasNext()) {
            if (predicate.hasSuffix) return column + op + value + PAREN_R;
            return column + op + value;
        }

        final String connection = op(predicate.connection);
        final String nextPredicate = toString(predicate.next, formatString, locale);

        if (nextPredicate.trim().isEmpty()) return column + op + value;
        if (predicate.hasPrefix) return column + op + value + connection + PAREN_L + nextPredicate;
        if (predicate.hasSuffix) return column + op + value + PAREN_R + connection + nextPredicate;
        else return column + op + value + connection + nextPredicate;
    }

    @Override
    public MapleDslDialectPredicateRender bind(MapleDslConfiguration context) {
        super.bind(context);
        return this;
    }
}
