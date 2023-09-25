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
enum MapleDslDialectRender implements BiFunction<MapleDslConfiguration, Object[], String> {
    fetchV {
        @Override
        ST fill(ST fmt, Object[] args) {
            fmt.add("tag", args[0]);
            fmt.add("from", args[1]);
            fmt.add("selection", args[2]);
            fmt.add("function", args[3]);
            fmt.add("order_asc", args[4]);
            fmt.add("order_desc", args[5]);
            fmt.add("skip", args[6]);
            fmt.add("limit", args[7]);
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
            fmt.add("tag", args[0]);
            fmt.add("selection", args[1]);
            fmt.add("where", args[3]);
            fmt.add("function", args[4]);
            fmt.add("order_asc", args[5]);
            fmt.add("order_desc", args[6]);
            fmt.add("skip", args[7]);
            fmt.add("limit", args[8]);
            return fmt;
        }
    },
    matchE {
        @Override
        ST fill(ST fmt, Object[] args) {
            return matchV.fill(fmt, args);
        }
    },
    traversal {
        @Override
        ST fill(ST fmt, Object[] args) {
            fmt.add("step_m", args[0] == null ? 0 : args[0]);
            fmt.add("step_n", args[1] == null ? 1 : args[1]);
            fmt.add("from", args[2]);
            fmt.add("over", args[3]);
            fmt.add("direction_in", args[4]);
            fmt.add("direction_out", args[5]);
            fmt.add("direction_both", args[6]);
            fmt.add("in", args[7]);
            fmt.add("out", args[8]);
            fmt.add("edge", args[9]);
            fmt.add("where", args[10]);
            fmt.add("selection", args[11]);
            fmt.add("function", args[12]);
            fmt.add("companion", args[13]);
            fmt.add("order_asc", args[14]);
            fmt.add("order_desc", args[15]);
            fmt.add("skip", args[16]);
            fmt.add("limit", args[17]);
            fmt.add("has_prev", args[18]);
            fmt.add("has_next", args[19]);
            fmt.add("next", args[20]);
            return fmt;
        }
    };

    abstract ST fill(ST fmt, Object[] args);

    @Override
    public final String apply(MapleDslConfiguration context, Object[] args) {
        final @NotNull String templateName = name();
        final @NotNull ST fmt = context.templateRegistry.borrowTemplate(templateName);
        try {
            return fill(fmt, args).render();
        } finally {
            context.templateRegistry.returnTemplate(fmt);
        }
    }
}