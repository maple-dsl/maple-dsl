package com.mapledsl.core;

import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.condition.wrapper.MapleDslDialectPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

/**
 * This abstract class represents a predicate renderer for a specific Maple DSL dialect.
 * It provides methods for rendering various parts of a predicate query, including vertices, edges, and comparisons.
 * Subclasses must implement the vertex, edge, inV, and outV methods to provide custom rendering logic.
 * <p></p>
 * The class also provides an implementation for rendering the complete predicate query as a string.
 * The toString method takes a MapleDslDialectPredicate object and returns the rendered query.
 * <p></p>
 * Subclasses must also implement the dialect method to specify the dialect of the Maple DSL.
 * <p></p>
 * The class is parameterized with the type of the MapleDslDialectPredicate that it can render.
 * It implements the AttributeRenderer interface to specify the type it can render.
 * It also implements the MapleDslDialectContextAware, MapleDslDialectAware, and MapleDslDialectRenderHelper interfaces to provide additional context and rendering capabilities.
 * <p></p>
 * This class is intended to be extended by specific dialect predicate renderers.
 */
@SuppressWarnings("rawtypes")
public abstract class MapleDslDialectPredicateRender implements AttributeRenderer<MapleDslDialectPredicate>, MapleDslDialectContextAware, MapleDslDialectAware, MapleDslDialectRenderHelper {
    protected MapleDslConfiguration context;
    protected abstract String vertex(@NotNull String ref, @Nullable String label, String column);
    protected abstract String edge(@NotNull String ref, @Nullable String label, String column);
    protected abstract String inV(@NotNull String ref, @Nullable String label, String column);
    protected abstract String outV(@NotNull String ref, @Nullable String label, String column);

    protected abstract String op(OP op);

    /**
     * Returns the appropriate column based on the given MapleDslDialectPredicate.
     *
     * @param predicate the MapleDslDialectPredicate used to determine the column
     * @return the column based on the given predicate
     * @throws UnsupportedOperationException if the predicate does not match any conditions
     */
    private String column(MapleDslDialectPredicate<?> predicate) {
        if (predicate.v())    return vertex(predicate.ref(), predicate.label(context), predicate.column());
        if (predicate.e())    return edge(predicate.ref(), predicate.label(context), predicate.column());
        if (predicate.in())   return inV(predicate.ref(), predicate.label(context), predicate.column());
        if (predicate.out())  return outV(predicate.ref(), predicate.label(context), predicate.column());

        throw new UnsupportedOperationException();
    }

    private String value(MapleDslDialectPredicate<?> predicate) {
        return context.parameterized(predicate.value());
    }

    @Override
    public final String toString(MapleDslDialectPredicate predicate, String formatString, Locale locale) {
        if (predicate == null) return NULL;

        final String column = column(predicate);
        final String value  = value(predicate);
        final String op     = op(predicate.op());

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
        this.context = context;
        return this;
    }
}
