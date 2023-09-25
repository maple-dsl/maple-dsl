package com.mapledsl.core;

import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.condition.wrapper.MapleDslDialectPredicate;
import com.mapledsl.core.exception.MapleDslBindingException;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

public abstract class MapleDslDialectPredicateRender extends MapleDslDialectBaseRender implements AttributeRenderer<MapleDslDialectPredicate> {
    protected abstract String vertex(@Nullable String label, String column);
    protected abstract String edge(@Nullable String label, String column);
    protected abstract String inV(@Nullable String label, String column);
    protected abstract String outV(@Nullable String label, String column);

    protected abstract String op(OP op);

    private String column(MapleDslDialectPredicate<?> predicate) {
        if (predicate.v())    return vertex(predicate.label(context), predicate.column());
        if (predicate.e())    return edge(predicate.label(context), predicate.column());
        if (predicate.in())   return inV(predicate.label(context), predicate.column());
        if (predicate.out())  return outV(predicate.label(context), predicate.column());

        throw new MapleDslBindingException(NULL);
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
