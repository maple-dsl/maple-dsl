package com.mapledsl.cypher;

import com.mapledsl.core.MapleDslDialectSelectionRender;
import com.mapledsl.core.model.Model;
import com.mapledsl.cypher.module.MapleCypherDslModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.StringJoiner;

public class MapleCypherDslDialectSelectionRender extends MapleDslDialectSelectionRender {

    @Override
    public String dialect() {
        return MapleCypherDslModule.DIALECT;
    }

    @Override
    protected String vertexRef(@NotNull String alias) {
        return alias;
    }

    @Override
    protected String edgeRef(@NotNull String alias) {
        return alias;
    }

    @Override
    protected String inVRef(@NotNull String alias) {
        return alias;
    }

    @Override
    protected String outVRef(@NotNull String alias) {
        return alias;
    }

    @Override
    protected String vertex(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias) {
        if (notEquals(columns, alias)) return NULL;
        final String prefix = nullLabel(label) ? refAlias + DOT + label : refAlias;
        final StringJoiner joiner = new StringJoiner(COMMA);
        for (int i = 0; i < columns.length; i++) {
            if (Model.ID.equals(columns[i])) {
                joiner.add("id(" + refAlias + ")" + AS + alias[i]);
                continue;
            }
            if (Model.TAG.equals(columns[i])) {
                joiner.add("head(labels(" + refAlias + "))" + AS + alias[i]);
                continue;
            }

            joiner.add(prefix + DOT + columns[i] + AS + alias[i]);
        }

        return joiner.toString();
    }

    @Override
    protected String edge(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias) {
        if (notEquals(columns, alias)) return NULL;
        final String prefix = nullLabel(label) ? refAlias + DOT + label : refAlias;
        final StringJoiner joiner = new StringJoiner(COMMA);
        for (int i = 0; i < columns.length; i++) {
            if (Model.E.ID.equals(columns[i])) {
                joiner.add("id(" + refAlias + ")" + AS + alias[i]);
                continue;
            }
            if (Model.E.SRC.equals(columns[i])) {
                joiner.add("src(" + refAlias + ")" + AS + alias[i]);
                continue;
            }
            if (Model.E.DST.equals(columns[i])) {
                joiner.add("dst(" + refAlias + ")" + AS + alias[i]);
                continue;
            }
            if (Model.TAG.equals(columns[i])) {
                joiner.add("type(" + refAlias + ")" + AS + alias[i]);
                continue;
            }

            joiner.add(prefix + DOT + columns[i] + AS + alias[i]);
        }

        return joiner.toString();
    }

    @Override
    protected String inV(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias) {
        return vertex(refAlias, label, columns, alias);
    }

    @Override
    protected String outV(@NotNull String refAlias, @Nullable String label, String[] columns, String[] alias) {
        return vertex(refAlias, label, columns, alias);
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
