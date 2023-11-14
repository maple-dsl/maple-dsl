package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Traversal;
import com.mapledsl.core.condition.Wrapper;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;

public abstract class TraversalWrapper implements Traversal, Wrapper {
    static final int LENGTH = 26;
    static final int STEP_M_INDEX = 0;
    static final int STEP_N_INDEX = 1;
    static final int FROM_INDEX = 2;
    static final int FROM_MATCH_INDEX = 3;
    static final int FROM_PREV_INDEX = 4;
    static final int OVER_INDEX = 5;
    static final int DIRECTION_IN_INDEX = 6;
    static final int DIRECTION_OUT_INDEX = 7;
    static final int DIRECTION_BOTH_INDEX = 8;
    static final int IN_ALIAS_INDEX = 9;
    static final int OUT_ALIAS_INDEX = 10;
    static final int EDGE_ALIAS_INDEX = 11;
    static final int PREDICATE_INDEX = 12;
    static final int SELECTION_INDEX = 13;
    static final int SHADOW_SELECTION_INDEX = 14;
    static final int FUNCTION_INDEX = 15;
    static final int COMPANION_INDEX = 16;
    static final int ORDER_ASC_INDEX = 17;
    static final int ORDER_DSC_INDEX = 18;
    static final int SKIP_INDEX = 19;
    static final int LIMIT_INDEX = 20;
    static final int HAS_NEXT_INDEX = 21;
    static final int NEXT_INDEX = 22;
    static final int DELETE_VERTEX_INDEX = 23;
    static final int DETACH_VERTEX_INDEX = 24;
    static final int DELETE_EDGE_INDEX = 25;

    /**
     * Position arguments distributed like below:
     * <pre>
     * [0] stepM            [1] stepN
     * [2] from             [3] from_match          [4] from_prev
     * [5] over
     * [6] direction_in     [7] direction_out       [8] direction_both
     * [9] in_alias         [10] out_alias          [11] edge_alias
     * [12] where
     * [13] selection       [14] shadow_selection
     * [15] function        [16] companion
     * [17] order_asc       [18] order_desc
     * [19] skip            [20] limit
     * [21] has_next        [22] next
     * [23] delete_vertex   [24] detach_vertex      [25] delete_edge
     * </pre>
     */
    final LinkedList<Object[]> argumentsList = new LinkedList<>();
    final List<String> orderAscList = new LinkedList<>();
    final List<String> orderDescList = new LinkedList<>();

    final Set<String> curTraversalCompanionSet = new LinkedHashSet<>();
    final Set<String> nextTraversalCompanionSet = new LinkedHashSet<>();
    final BiFunction<MapleDslConfiguration, Object[], String> renderFunc;

    Object[] arguments = new Object[LENGTH];
    String nextTraversalFrom;

    List<MapleDslDialectPredicate<?>> predicateList = new LinkedList<>();
    List<MapleDslDialectSelection<?>> selectionList = new LinkedList<>();
    List<MapleDslDialectSelection<?>> shadowSelectionList = new LinkedList<>();
    List<MapleDslDialectFunction<?>> functionList = new LinkedList<>();

    /**
     * Traversal the vertices#ID of the graph.
     *
     * @param from {@link Model.V#id()}
     */
    protected <R> TraversalWrapper(R from, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
        this.renderFunc = renderFunc;
        this.arguments[FROM_INDEX] = from;
    }

    protected TraversalWrapper(String fromMatchReference, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
        this.renderFunc = renderFunc;
        this.arguments[FROM_MATCH_INDEX] = fromMatchReference;
    }

    @Override
    public String render(MapleDslConfiguration configuration) {
        nextTraversal(true);

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < argumentsList.size(); i++) {
            final Object[] arguments = argumentsList.get(i);
            if (i == argumentsList.size() - 1) {
                arguments[ORDER_ASC_INDEX]  = orderAscList.isEmpty()    ? null : orderAscList;
                arguments[ORDER_DSC_INDEX]  = orderDescList.isEmpty()   ? null : orderDescList;
            }

            final String sql = renderFunc.apply(configuration, arguments);
            builder.append(sql);
        }

        return builder.toString();
    }

    @Override
    public abstract TraversalStepWrapper inE(int stepM, int stepN, Collection<Class<? extends Model.E>> over);

    @Override
    public abstract TraversalStepWrapper inE(int stepM, int stepN, Class<? extends Model.E> over);

    @Override
    public abstract TraversalStepWrapper inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);

    @Override
    public abstract TraversalStepWrapper inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    @Override
    public abstract TraversalStepWrapper inE(int stepM, int stepN, @NotNull String... over);

    @Override
    public TraversalStepWrapper inE(int step, Collection<Class<? extends Model.E>> over) {
        return inE(0, step, over);
    }

    @Override
    public TraversalStepWrapper inE(int step, Class<? extends Model.E> over) {
        return inE(0, step, over);
    }

    @Override
    public TraversalStepWrapper inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return inE(0, step, first, second);
    }

    @Override
    public TraversalStepWrapper inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return inE(0, step, first, second, third);
    }

    @Override
    public TraversalStepWrapper inE(int step, String... over) {
        return inE(0, step, over);
    }

    @Override
    public TraversalStepWrapper inE(Collection<Class<? extends Model.E>> over) {
        return inE(1, over);
    }

    @Override
    public TraversalStepWrapper inE(Class<? extends Model.E> over) {
        return inE(1, over);
    }

    @Override
    public TraversalStepWrapper inE(Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return inE(1, first, second);
    }

    @Override
    public TraversalStepWrapper inE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return inE(1, first, second, third);
    }

    @Override
    public TraversalStepWrapper inE(String... over) {
        return inE(1, over);
    }

    @Override
    public abstract TraversalStepWrapper outE(int stepM, int stepN, Collection<Class<? extends Model.E>> over);

    @Override
    public abstract TraversalStepWrapper outE(int stepM, int stepN, Class<? extends Model.E> over);

    @Override
    public abstract TraversalStepWrapper outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);

    @Override
    public abstract TraversalStepWrapper outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    @Override
    public abstract TraversalStepWrapper outE(int stepM, int stepN, String... over);

    @Override
    public TraversalStepWrapper outE(int step, Collection<Class<? extends Model.E>> over) {
        return outE(0, step, over);
    }

    @Override
    public TraversalStepWrapper outE(int step, Class<? extends Model.E> over) {
        return outE(0, step, over);
    }

    @Override
    public TraversalStepWrapper outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return outE(0, step, first, second);
    }

    @Override
    public TraversalStepWrapper outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return outE(0, step, first, second, third);
    }

    @Override
    public TraversalStepWrapper outE(int step, String... over) {
        return outE(0, step, over);
    }

    @Override
    public TraversalStepWrapper outE(Collection<Class<? extends Model.E>> over) {
        return outE(1, over);
    }

    @Override
    public TraversalStepWrapper outE(Class<? extends Model.E> over) {
        return outE(1, over);
    }

    @Override
    public TraversalStepWrapper outE(Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return outE(1, first, second);
    }

    @Override
    public TraversalStepWrapper outE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return outE(1, first, second, third);
    }

    @Override
    public TraversalStepWrapper outE(String... over) {
        return outE(1, over);
    }

    @Override
    public abstract TraversalStepWrapper bothE(int stepM, int stepN, Collection<Class<? extends Model.E>> over);

    @Override
    public abstract TraversalStepWrapper bothE(int stepM, int stepN, Class<? extends Model.E> over);

    @Override
    public abstract TraversalStepWrapper bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);

    @Override
    public abstract TraversalStepWrapper bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    @Override
    public abstract TraversalStepWrapper bothE(int stepM, int stepN, String... over);

    @Override
    public TraversalStepWrapper bothE(int step, Collection<Class<? extends Model.E>> over) {
        return bothE(0, step, over);
    }

    @Override
    public TraversalStepWrapper bothE(int step, Class<? extends Model.E> over) {
        return bothE(0, step, over);
    }

    @Override
    public TraversalStepWrapper bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return bothE(0, step, first, second);
    }

    @Override
    public TraversalStepWrapper bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return bothE(0, step, first, second, third);
    }

    @Override
    public TraversalStepWrapper bothE(int step, String... over) {
        return bothE(0, step, over);
    }

    @Override
    public TraversalStepWrapper bothE(Collection<Class<? extends Model.E>> over) {
        return bothE(1, over);
    }

    @Override
    public TraversalStepWrapper bothE(Class<? extends Model.E> over) {
        return bothE(1, over);
    }

    @Override
    public TraversalStepWrapper bothE(Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return bothE(1, first, second);
    }

    @Override
    public TraversalStepWrapper bothE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return bothE(1, first, second, third);
    }

    @Override
    public TraversalStepWrapper bothE(String... over) {
        return bothE(1, over);
    }

    protected final synchronized void nextTraversal(boolean terminate) {
        if (arguments[DIRECTION_IN_INDEX] == null && arguments[DIRECTION_OUT_INDEX] == null && arguments[DIRECTION_BOTH_INDEX] == null) {
            throw new MapleDslExecutionException("Missing direction_clause value, use `inE`, `outE`, `bothE`.");
        }

        if (arguments[IN_ALIAS_INDEX] == null)      arguments[IN_ALIAS_INDEX] = DEFAULT_IN_ALIAS;
        if (arguments[OUT_ALIAS_INDEX] == null)     arguments[OUT_ALIAS_INDEX] = DEFAULT_OUT_ALIAS;
        if (arguments[EDGE_ALIAS_INDEX] == null)    arguments[EDGE_ALIAS_INDEX] = DEFAULT_EDGE_ALIAS;

        // if outV#step missing Model.ID selection by `nextTraversalFrom` is null,
        // or missing outV#step by check `nextTraversalCompanionSet` whether empty.
        // :/ then append `outV(it -> it.selectAs(Model.ID, "dst_id")` automatically.
        if (nextTraversalFrom == null) {
            if (terminate && curTraversalCompanionSet.isEmpty()) {
                nextTraversalFrom = DEFAULT_NEXT_TRAVERSAL_FROM_ALIAS;
                selectionList.add(new MapleDslDialectSelection<Model.V>(Model.ID, nextTraversalFrom)
                        .setOut(true)
                        .setInstantiatedAlias(DEFAULT_OUT_ALIAS)
                );
            }
            if (!terminate) {
                nextTraversalFrom = DEFAULT_NEXT_TRAVERSAL_FROM_ALIAS;
                selectionList.add(new MapleDslDialectSelection<Model.V>(Model.ID, nextTraversalFrom)
                        .setOut(true)
                        .setInstantiatedAlias(DEFAULT_OUT_ALIAS)
                );
            }
        }

        if (!predicateList.isEmpty()) {
            arguments[PREDICATE_INDEX] = predicateList;
            predicateList = new LinkedList<>();
        }
        if (!selectionList.isEmpty()) {
            arguments[SELECTION_INDEX] = selectionList;
            selectionList = new LinkedList<>();
        }
        if (!shadowSelectionList.isEmpty()){
            arguments[SHADOW_SELECTION_INDEX] = shadowSelectionList;
            shadowSelectionList = new LinkedList<>();
        }
        if (!functionList.isEmpty()) {
            arguments[FUNCTION_INDEX] = functionList;
            functionList = new LinkedList<>();
        }

        // e.g. MATCH (n) - [:follow] - (m)
        // WITH m, ...
        // MATCH (new_m)
        // `WITH m` should be modified as `WITH m as new_m`
        if (!argumentsList.isEmpty()) argumentsList.peekLast()[NEXT_INDEX] = arguments[IN_ALIAS_INDEX];

        argumentsList.add(arguments);
        // check next_traversal should be terminated or mark it `has_next`.
        if (terminate) return;
        else arguments[HAS_NEXT_INDEX] = true;

        arguments = new Object[LENGTH];
        // specific for nebula, e.g. | GO FROM $-.dst_id
        arguments[FROM_PREV_INDEX] = nextTraversalFrom;
        // $-.dst_id or the others(next_traversal_variable) should be removed from the companion set
        curTraversalCompanionSet.remove(nextTraversalFrom);
        nextTraversalCompanionSet.addAll(curTraversalCompanionSet);
        curTraversalCompanionSet.clear();
        // clear dst_id var for next filling.
        nextTraversalFrom = null;
        arguments[COMPANION_INDEX] = nextTraversalCompanionSet.isEmpty() ? null : new LinkedHashSet<>(nextTraversalCompanionSet);
    }
}
