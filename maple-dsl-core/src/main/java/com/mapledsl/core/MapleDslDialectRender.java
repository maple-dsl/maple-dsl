package com.mapledsl.core;

import org.jetbrains.annotations.NotNull;
import org.stringtemplate.v4.ST;

import java.util.function.BiFunction;

/**
 * A collection represents a group of graph (basic(CURD) & traverse & path & subgraph) dialect renderers.
 *
 * @author bofa1ex
 * @since 2023/08/23
 */
@SuppressWarnings("DuplicatedCode")
enum MapleDslDialectRender implements BiFunction<MapleDslConfiguration, Object[], String> {
    fetchV {
        @Override
        ST fill(ST fmt, Object[] args) {
            fmt.add("ref", args[0]);
            fmt.add("tag", args[1]);
            fmt.add("selection", args[2]);
            fmt.add("shadow_selection", args[3]);
            fmt.add("where", args[4]);
            fmt.add("function", args[5]);
            fmt.add("order_asc", args[6]);
            fmt.add("order_desc", args[7]);
            fmt.add("offset", args[8]);
            fmt.add("limit", args[9]);
            return fmt;
        }
    },
    fetchE {
        @Override
        ST fill(ST fmt, Object[] args) {
            return fetchV.fill(fmt, args);
        }
    },
    matchV {
        @Override
        ST fill(ST fmt, Object[] args) {
            fmt.add("ref", args[0]);
            fmt.add("tag", args[1]);
            fmt.add("selection", args[2]);
            fmt.add("shadow_selection", args[3]);
            fmt.add("where", args[4]);
            fmt.add("function", args[5]);
            fmt.add("order_asc", args[6]);
            fmt.add("order_desc", args[7]);
            fmt.add("offset", args[8]);
            fmt.add("limit", args[9]);
            fmt.add("delete", args[10]);
            fmt.add("detach", args[11]);
            fmt.add("traverse", args[12]);
            return fmt;
        }
    },
    matchE {
        @Override
        ST fill(ST fmt, Object[] args) {
            fmt.add("ref", args[0]);
            fmt.add("tag", args[1]);
            fmt.add("selection", args[2]);
            fmt.add("shadow_selection", args[3]);
            fmt.add("where", args[4]);
            fmt.add("function", args[5]);
            fmt.add("order_asc", args[6]);
            fmt.add("order_desc", args[7]);
            fmt.add("offset", args[8]);
            fmt.add("limit", args[9]);
            fmt.add("delete", args[10]);
            return fmt;
        }
    },
    traversal {
        @Override
        ST fill(ST fmt, Object[] args) {
            fmt.add("step_m", args[0] == null ? 0 : args[0]);
            fmt.add("step_n", args[1] == null ? 1 : args[1]);
            fmt.add("from", args[2]);
            fmt.add("from_match", args[3]);
            fmt.add("from_prev", args[4]);
            fmt.add("over", args[5]);
            fmt.add("direction_in", args[6]);
            fmt.add("direction_out", args[7]);
            fmt.add("direction_both", args[8]);
            fmt.add("in", args[9]);
            fmt.add("out", args[10]);
            fmt.add("edge", args[11]);
            fmt.add("where", args[12]);
            fmt.add("selection", args[13]);
            fmt.add("shadow_selection", args[14]);
            fmt.add("function", args[15]);
            fmt.add("companion", args[16]);
            fmt.add("order_asc", args[17]);
            fmt.add("order_desc", args[18]);
            fmt.add("offset", args[19]);
            fmt.add("limit", args[20]);
            fmt.add("has_next", args[21]);
            fmt.add("next", args[22]);
            fmt.add("delete_vertex", args[23]);
            fmt.add("detach_vertex", args[24]);
            fmt.add("delete_edge", args[25]);
            return fmt;
        }
    };

    abstract ST fill(ST fmt, Object[] args);

    @Override
    public final String apply(MapleDslConfiguration context, Object[] args) {
        final @NotNull String templateName = name();
        final @NotNull ST fmt = context.templateRegistry.borrowTemplate(templateName);
        try {
            final String statement = fill(fmt, args).render();
            return context.templateRegistry.prettyPrint ? statement.trim().replaceAll("\\s{2,}", " ").replaceAll("\\s?,\\s?", ",") : statement;
        } finally {
            context.templateRegistry.returnTemplate(fmt);
        }
    }
}