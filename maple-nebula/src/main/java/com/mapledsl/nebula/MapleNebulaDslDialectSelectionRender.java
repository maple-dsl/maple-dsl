package com.mapledsl.nebula;

import com.mapledsl.core.MapleDslDialectSelectionRender;
import com.mapledsl.core.condition.wrapper.MapleDslDialectSelection;
import com.mapledsl.core.model.Model;
import com.mapledsl.nebula.model.NebulaModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.StringJoiner;

public class MapleNebulaDslDialectSelectionRender extends MapleDslDialectSelectionRender {

    @Override
    public String toString(MapleDslDialectSelection value, String formatString, Locale locale) {
        if ("only_column".equals(formatString)) {
            if (value == null)         return NULL;
            if (value.isNotPresent())  return NULL;

            if (!value.hasNext()) return String.join(COMMA, value.aliases());
            final String nextSelection = toString(value.next, formatString, locale);
            if (nextSelection.trim().isEmpty()) return String.join(COMMA, value.aliases());
            return String.join(COMMA, value.aliases()) + COMMA + nextSelection;
        }

        return super.toString(value, formatString, locale);
    }

    @Override
    protected String vertexRef(@NotNull String alias) {
        return "vertex" + AS + alias;
    }

    @Override
    protected String edgeRef(@NotNull String alias) {
        return "edge" + AS + alias;
    }

    @Override
    protected String inVRef(@Nullable String label, @NotNull String alias) {
        return nullLabel(label) ? "$^" + AS + alias : "$^" + DOT + label + AS + alias;
    }

    @Override
    protected String outVRef(@Nullable String label, @NotNull String alias) {
        return nullLabel(label) ? "$$" + AS + alias : "$$" + DOT + label + AS + alias;
    }

    @Override
    protected String vertex(@Nullable String label, String[] columns, String[] alias) {
        if (notEquals(columns, alias)) return NULL;
        final String prefix = nullLabel(label) ? "properties(vertex)" : label;
        final StringJoiner joiner = new StringJoiner(COMMA);
        for (int i = 0; i < columns.length; i++) {
            if (Model.ID.equals(columns[i])) {
                joiner.add("id(vertex)" + AS + alias[i]);
                continue;
            }
            if (Model.TAG.equals(columns[i])) {
                joiner.add("head(labels(vertex))" + AS + alias[i]);
                continue;
            }

            joiner.add(prefix + DOT + columns[i] + AS + alias[i]);
        }

        return joiner.toString();
    }

    @Override
    protected String edge(@Nullable String label, String[] columns, String[] alias) {
        if (notEquals(columns, alias)) return NULL;
        final String prefix = nullLabel(label) ? "properties(edge)" : label;
        final StringJoiner joiner = new StringJoiner(COMMA);
        for (int i = 0; i < columns.length; i++) {
            if (Model.E.SRC.equals(columns[i])) {
                joiner.add("src(edge)" + AS + alias[i]);
                continue;
            }
            if (Model.E.DST.equals(columns[i])) {
                joiner.add("dst(edge)" + AS + alias[i]);
                continue;
            }
            if (NebulaModel.E.RANK.equals(columns[i])) {
                joiner.add("rank(edge)" + AS + alias[i]);
                continue;
            }
            if (Model.TAG.equals(columns[i])) {
                joiner.add("type(edge)" + AS + alias[i]);
                continue;
            }

            joiner.add(prefix + DOT + columns[i] + AS + alias[i]);
        }

        return joiner.toString();
    }

    @Override
    protected String inV(@Nullable String label, String[] columns, String[] alias) {
        if (notEquals(columns, alias)) return NULL;
        final String prefix = nullLabel(label) ? "properties($^)" : "$^" + DOT + label;
        final StringJoiner joiner = new StringJoiner(COMMA);
        for (int i = 0; i < columns.length; i++) {
            if (Model.ID.equals(columns[i])) {
                joiner.add("id($^)" + AS + alias[i]);
                continue;
            }
            if (Model.TAG.equals(columns[i])) {
                joiner.add("head(labels($^))" + AS + alias[i]);
                continue;
            }

            joiner.add(prefix + DOT + columns[i] + AS + alias[i]);
        }

        return joiner.toString();
    }

    @Override
    protected String outV(@Nullable String label, String[] columns, String[] alias) {
        if (notEquals(columns, alias)) return NULL;
        final String prefix = nullLabel(label) ? "properties($$)" : "$$" + DOT + label;
        final StringJoiner joiner = new StringJoiner(COMMA);
        for (int i = 0; i < columns.length; i++) {
            if (Model.ID.equals(columns[i])) {
                joiner.add("id($$)" + AS + alias[i]);
                continue;
            }
            if (Model.TAG.equals(columns[i])) {
                joiner.add("head(labels($$))" + AS + alias[i]);
                continue;
            }

            joiner.add(prefix + DOT + columns[i] + AS + alias[i]);
        }

        return joiner.toString();
    }

    @Override
    public String dialect() {
        return "nebula";
    }

    private boolean nullLabel(String label) {
        return label == null || label.trim().isEmpty();
    }

    private boolean notEquals(String[] columns, String[] alias) {
        if (columns == null) return true;
        if (alias == null) return true;
        return columns.length != alias.length;
    }
}
