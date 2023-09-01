package com.mapledsl.core;

import org.jetbrains.annotations.NotNull;
import org.stringtemplate.v4.ST;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * A collection represents a group of graph (basic(CURD) & traverse & path & subgraph) dialect renderers.
 *
 * @author bofa1ex
 * @since 2023/08/23
 */
enum MapleDslDialectRender implements BiFunction<MapleDslConfiguration, Object[], String> {
    saveV {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            requireNonNull(args[0], "tag must not be null.");
            requireNonNull(args[1], "vertex/vertices must not be null.");
            return render(name(), context, fmt -> fmt.add("tag", args[0]).add("v", args[1]));
        }
    },
    saveE {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            requireNonNull(args[0], "tag must not be null.");
            requireNonNull(args[1], "edge/edges must not be null.");
            return render(name(), context, fmt -> fmt.add("tag", args[0]).add("e", args[1]));
        }
    },
    deleteV {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            requireNonNull(args[0], "tag must not be null");
            return render(name(), context, fmt -> fmt.add("tag", args[0]).add("from", args[1]).add("withEdge", args[2]));
        }
    },
    deleteE {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            requireNonNull(args[0], "tag must not be null");
            return render(name(), context, fmt -> fmt.add("tag", args[0]).add("from", args[1]));
        }
    },
    selectV {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            requireNonNull(args[0], "tag must not be null");
            return render(name(), context, fmt -> fmt.add("tag", args[0]).add("from", args[1]));
        }
    },
    selectE {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            requireNonNull(args[0], "tag must not be null");
            return render(name(), context, fmt -> fmt.add("tag", args[0]).add("from", args[1]));
        }
    },
    fetchV {
        @Override
        public String apply(MapleDslConfiguration context, Object... args) {
            return render(name(), context, fmt -> fmt
                    .add("tag", args[0])
                    .add("from", args[1])
                    .add("primary_selection", args[2])
                    .add("secondary_selection", null)
                    .add("group", args[3])
                    .add("count", args[5]).add("count_alias", args[6])
                    .add("sum", args[7]).add("sum_alias", args[8])
                    .add("avg", args[6]).add("avg_alias", args[6])
                    .add("max", args[7]).add("max_alias", args[7])
                    .add("min", args[8]).add("min_alias", args[8])
                    .add("order_asc", args[9])
                    .add("order_desc", args[10])
                    .add("skip", args[11])
                    .add("limit", args[12])
            );
        }
    },
    fetchE {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            return render(name(), context, fmt -> fmt
                    .add("tag", args[0])
                    .add("from", args[1])
                    .add("yield", args[2])
                    .add("groupBy", args[3])
                    .add("count", args[4])
                    .add("sum", args[5])
                    .add("avg", args[6])
                    .add("max", args[7])
                    .add("min", args[8])
                    .add("orderAsc", args[9])
                    .add("orderDsc", args[10])
                    .add("skip", args[11])
                    .add("limit", args[12])
            );
        }
    },
    matchV {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            return render(name(), context, UnaryOperator.identity());
        }
    },
    matchE {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            return render(name(), context, UnaryOperator.identity());
        }
    },
    traversal {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            return render(name(), context, fmt -> fmt
                    .add("stepM", args[0] == null ? 0 : args[0])
                    .add("stepN", args[1] == null ? 1 : args[1])
                    .add("from", args[2])
                    .add("over", args[3])
                    .add("direction", args[4])
                    .add("where", args[5])
                    .add("yield", args[6])
                    .add("groupBy", args[7])
                    .add("count", args[8])
                    .add("sum", args[9])
                    .add("avg", args[10])
                    .add("max", args[11])
                    .add("min", args[12])
                    .add("orderAsc", args[13])
                    .add("orderDsc", args[14])
                    .add("skip", args[15])
                    .add("limit", args[16])
            );
        }
    },
    path {
        @Override
        public String apply(@NotNull MapleDslConfiguration context, Object... args) {
            return render(name(), context, fmt -> fmt
                    .add("from", args[0])
                    .add("to", args[1])
                    .add("over", args[2])
                    .add("where", args[3])
                    .add("orderBy", args[4])
                    .add("limit", args[5])
            );
        }
    };

    private static String render(String template, MapleDslConfiguration context, UnaryOperator<ST> fmtFunc) {
        final @NotNull ST fmt = context.templateRegistry.borrowTemplate(template);
        try {
            return fmtFunc.apply(fmt).render();
        } finally {
            context.templateRegistry.returnTemplate(fmt);
        }
    }
}