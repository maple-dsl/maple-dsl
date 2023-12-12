package com.mapledsl.cypher;

import com.mapledsl.core.MapleDslDialectPredicateRender;
import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.model.Model;
import com.mapledsl.cypher.module.MapleCypherDslModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

/**
 * The class {@code MapleCypherDslDialectPredicateRender} is a subclass of {@code MapleDslDialectPredicateRender}
 * that represents a predicate renderer for the Maple Cypher DSL dialect.
 * It overrides various methods to provide custom rendering logic for vertices, edges, and comparisons.
 */
public class MapleCypherDslDialectPredicateRender extends MapleDslDialectPredicateRender {
    @Override
    public String dialect() {
        return MapleCypherDslModule.DIALECT;
    }

    @Override
    protected String vertex(@NotNull String ref, @Nullable String label, String column) {
        if (Model.ID.equals(column))    return "id(" + ref + ")";
        if (Model.TAG.equals(column))   return "head(labels(" + ref + "))";

        return ref + DOT + column;
    }

    @Override
    protected String edge(@NotNull String ref, @Nullable String label, String column) {
        if (Model.E.SRC.equals(column)) return "src(" + ref + ")";
        if (Model.E.DST.equals(column)) return "dst(" + ref + ")";
        if (Model.TAG.equals(column))   return "type(" + ref + ")";

        return ref + DOT + column;
    }

    @Override
    protected String inV(@NotNull String ref, @Nullable String label, String column) {
        return vertex(ref, label, column);
    }

    @Override
    protected String outV(@NotNull String ref, @Nullable String label, String column) {
        return vertex(ref, label, column);
    }

    @Override
    protected String op(OP op) {
        if (!operatorRenderMap.containsKey(op)) throw new UnsupportedOperationException("Unsupported OP: " + op.name());
        return operatorRenderMap.get(op);
    }

    private final Map<OP, String> operatorRenderMap = new EnumMap<OP, String>(OP.class) {
        {
            put(OP.AND, " AND ");
            put(OP.OR, " OR ");
            put(OP.XOR, " XOR ");

            put(OP.ASSIGN, " = ");

            put(OP.EQ, " == ");
            put(OP.NE, " != ");
            put(OP.LT, " < ");
            put(OP.LE, " <= ");
            put(OP.GT, " > ");
            put(OP.GE, " >= ");

            put(OP.ISNULL, " IS NULL ");
            put(OP.NOT_NULL, " IS NOT NULL ");
            put(OP.IN, " IN ");
            put(OP.NOT_IN, " NOT IN ");
            put(OP.CONTAINS, " CONTAINS ");
            put(OP.STARTS_WITH, " STARTS WITH ");
            put(OP.NOT_STARTS_WITH, " NOT STARTS WITH ");
            put(OP.ENDS_WITH, " ENDS WITH ");
            put(OP.NOT_ENDS_WITH, " NOT ENDS WITH ");
        }
    };
}
