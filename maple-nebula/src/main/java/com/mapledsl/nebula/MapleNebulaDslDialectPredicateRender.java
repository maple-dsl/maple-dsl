package com.mapledsl.nebula;

import com.mapledsl.core.MapleDslDialectPredicateRender;
import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.model.Model;
import com.mapledsl.nebula.model.NebulaModel;
import com.mapledsl.nebula.module.MapleNebulaDslModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class MapleNebulaDslDialectPredicateRender extends MapleDslDialectPredicateRender {

    @Override
    protected String vertex(@NotNull String ref,  @Nullable String label, String column) {
        if (Model.ID.equals(column))    return "id(vertex)";
        if (Model.TAG.equals(column))   return "head(labels(vertex))";

        return (nullLabel(label) ? "properties(vertex)" : label) + DOT + column;
    }

    @Override
    protected String edge(@NotNull String ref, @Nullable String label, String column) {
        if (Model.E.SRC.equals(column)) return "src(edge)";
        if (Model.E.DST.equals(column)) return "dst(edge)";
        if (Model.TAG.equals(column))   return "type(edge)";
        if (NebulaModel.E.RANK.equals(column)) return "rank(edge)";

        return (nullLabel(label) ? "properties(edge)" : label) + DOT + column;
    }

    @Override
    protected String inV(@NotNull String ref, @Nullable String label, String column) {
        if (Model.ID.equals(column))    return "id($^)";
        if (Model.TAG.equals(column))   return "head(labels($^))";

        return (nullLabel(label) ? "properties($^)" : "$^" + DOT + label) + DOT + column;
    }

    @Override
    protected String outV(@NotNull String ref, @Nullable String label, String column) {
        if (Model.ID.equals(column))    return "id($$)";
        if (Model.TAG.equals(column))   return "head(labels($$))";

        return (nullLabel(label) ? "properties($$)" : "$$" + DOT + label) + DOT + column;
    }

    @Override
    protected String op(OP op) {
        if (!operatorRenderMap.containsKey(op)) throw new UnsupportedOperationException("Unsupported OP: " + op.name());
        return operatorRenderMap.get(op);
    }

    @Override
    public String dialect() {
        return MapleNebulaDslModule.DIALECT;
    }

    private boolean nullLabel(String label) {
        return label == null || label.trim().isEmpty();
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
