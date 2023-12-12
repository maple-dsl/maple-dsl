package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.MapleDslDialectSelection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

/**
 * This class is an abstract base class for rendering a {@link MapleDslDialectSelection} into a string representation.
 * It provides methods for rendering various types of elements like vertices, edges, in-vertices, and out-vertices.
 * Implementations of this class should provide the specific rendering logic for the target dialect.
 */
@SuppressWarnings("rawtypes")
public abstract class MapleDslDialectSelectionRender implements AttributeRenderer<MapleDslDialectSelection>, MapleDslDialectAware, MapleDslDialectContextAware, MapleDslDialectRenderHelper {
    protected MapleDslConfiguration context;

    protected abstract String vertexRef(@NotNull String alias);
    protected abstract String edgeRef(@NotNull String alias);
    protected abstract String inVRef(@NotNull String alias);
    protected abstract String outVRef(@NotNull String alias);

    protected abstract String vertex(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias);
    protected abstract String edge(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias);
    protected abstract String inV(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias);
    protected abstract String outV(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias);

    @Override
    public String toString(MapleDslDialectSelection value, String formatString, Locale locale) {
        if (value == null) return NULL;
        if (!value.hasNext()) return toSelection(value);

        final String nextSelection = toString(value.next, formatString, locale);
        if (nextSelection.equals(NULL)) return toSelection(value);

        return toSelection(value) + COMMA + nextSelection;
    }

    private String toSelection(MapleDslDialectSelection<?> value) {
        if (value.isAllPresent())  {
            if (value.v())    return vertexRef(value.ref());
            if (value.e())    return edgeRef(value.ref());
            if (value.in())   return inVRef(value.ref());
            if (value.out())  return outVRef(value.ref());

            throw new UnsupportedOperationException();
        }

        if (value.v())    return vertex(value.ref(), value.label(context), value.columns(), value.aliases());
        if (value.e())    return edge(value.ref(), value.label(context), value.columns(), value.aliases());

        if (value.in())   return inV(value.ref(), value.label(context), value.columns(), value.aliases());
        if (value.out())  return outV(value.ref(), value.label(context), value.columns(), value.aliases());

        throw new UnsupportedOperationException();
    }

    @Override
    public MapleDslDialectSelectionRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }
}
