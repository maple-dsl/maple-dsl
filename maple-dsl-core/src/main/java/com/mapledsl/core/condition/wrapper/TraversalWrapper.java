package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Sort;
import com.mapledsl.core.condition.Traversal;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("DuplicatedCode")
public class TraversalWrapper implements Traversal {
    static final int LENGTH = 25;
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
    static final int FUNCTION_INDEX = 14;
    static final int COMPANION_INDEX = 15;
    static final int ORDER_ASC_INDEX = 16;
    static final int ORDER_DSC_INDEX = 17;
    static final int SKIP_INDEX = 18;
    static final int LIMIT_INDEX = 19;
    static final int HAS_NEXT_INDEX = 20;
    static final int NEXT_INDEX = 21;
    static final int DELETE_VERTEX_INDEX = 22;
    static final int DETACH_VERTEX_INDEX = 23;
    static final int DELETE_EDGE_INDEX = 24;

    /**
     * Position arguments distributed like below:
     * <pre>
     * [0] stepM        [1] stepN
     * [2] from         [3] from_match      [4] from_prev
     * [5] over         [6] direction_in    [7] direction_out [8] direction_both
     * [9] in_alias     [10] out_alias      [11] edge_alias
     * [12] where
     * [13] select      [14] function       [15] companion
     * [16] order_asc    [17] order_desc
     * [18] skip        [19 limit
     * [20] has_next    [21] next
     * [22] delete_vertex [23] detach_vertex [24] delete_edge
     * </pre>
     */
    final LinkedList<Object[]> argumentsList = new LinkedList<>();
    final List<String> orderAscList = new LinkedList<>();
    final List<String> orderDescList = new LinkedList<>();
    final Set<String> nextTraversalCompanionSet = new LinkedHashSet<>();
    final BiFunction<MapleDslConfiguration, Object[], String> renderFunc;

    Object[] arguments = new Object[LENGTH];
    String nextTraversalFrom;

    List<MapleDslDialectPredicate<?>> predicateList = new LinkedList<>();
    List<MapleDslDialectSelection<?>> selectionList = new LinkedList<>();
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

    private synchronized void nextTraversal(boolean terminate) {
        if (arguments[DIRECTION_IN_INDEX] == null && arguments[DIRECTION_OUT_INDEX] == null && arguments[DIRECTION_BOTH_INDEX] == null) {
            throw new MapleDslExecutionException("Missing direction_clause value, use `inE`, `outE`, `bothE`.");
        }

        if (arguments[IN_ALIAS_INDEX] == null)      arguments[IN_ALIAS_INDEX] = DEFAULT_IN_ALIAS;
        if (arguments[OUT_ALIAS_INDEX] == null)     arguments[OUT_ALIAS_INDEX] = DEFAULT_OUT_ALIAS;
        if (arguments[EDGE_ALIAS_INDEX] == null)    arguments[EDGE_ALIAS_INDEX] = DEFAULT_EDGE_ALIAS;

        // missing select will append `outV(it -> it.selectAs(Model.ID, "_dst")` automatically.
        if (nextTraversalFrom == null) {
            nextTraversalFrom = DEFAULT_NEXT_TRAVERSAL_FROM_ALIAS;
            selectionList.add(new MapleDslDialectSelection<Model.V>(Model.ID, nextTraversalFrom).setOut(true));
        }

        if (!predicateList.isEmpty()) {
            arguments[PREDICATE_INDEX] = predicateList;
            predicateList = new LinkedList<>();
        }
        if (!selectionList.isEmpty()) {
            arguments[SELECTION_INDEX] = selectionList;
            selectionList = new LinkedList<>();
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

        if (terminate) return;
        else arguments[HAS_NEXT_INDEX] = true;

        arguments = new Object[LENGTH];
        // specific for nebula, e.g. | GO FROM $-._dst
        arguments[FROM_PREV_INDEX] = nextTraversalFrom;
        // $-._dst or the others(next_traversal_var) should be removed from the companion set
        nextTraversalCompanionSet.remove(nextTraversalFrom);
        // clear _dst var for next filling.
        nextTraversalFrom = null;
        arguments[COMPANION_INDEX] = nextTraversalCompanionSet.isEmpty() ? null : nextTraversalCompanionSet;
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
    public Traversal limit(int limit) {
        return limit(0, limit);
    }

    @Override
    public Traversal limit(int skip, int limit) {
        arguments[SKIP_INDEX] = skip;
        arguments[LIMIT_INDEX] = limit;
        return this;
    }

    @Override
    public Traversal inE(int stepM, int stepN, Collection<Class<? extends Model.E>> over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over == null || over.isEmpty() ? null : over;
        arguments[DIRECTION_IN_INDEX] = true;

        return this;
    }

    @Override
    public Traversal inE(int stepM, int stepN, Class<? extends Model.E> over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over;
        arguments[DIRECTION_IN_INDEX] = true;

        return this;
    }

    @Override
    public Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        if (first == null) throw new MapleDslExecutionException("edgeType `first` must not be null.");
        if (second == null) throw new MapleDslExecutionException("edgeType `second` must not be null.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = new Class<?>[]{ first, second };
        arguments[DIRECTION_IN_INDEX] = true;

        return this;
    }

    @Override
    public Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        if (first == null) throw new MapleDslExecutionException("edgeType `first` must not be null.");
        if (second == null) throw new MapleDslExecutionException("edgeType `second` must not be null.");
        if (third == null) throw new MapleDslExecutionException("edgeType `third` must not be null.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = new Class<?>[]{ first, second, third };
        arguments[DIRECTION_IN_INDEX] = true;

        return this;
    }

    @Override
    public Traversal inE(int stepM, int stepN, @NotNull String[] over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over == null || over.length == 0 ? null : over;
        arguments[DIRECTION_IN_INDEX] = true;

        return this;
    }

    @Override
    public Traversal inE(int step, Collection<Class<? extends Model.E>> over) {
        return inE(0, step, over);
    }

    @Override
    public Traversal inE(int step, Class<? extends Model.E> over) {
        return inE(0, step, over);
    }

    @Override
    public Traversal inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return inE(0, step, first, second);
    }

    @Override
    public Traversal inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return inE(0, step, first, second, third);
    }

    @Override
    public Traversal inE(int step, String... over) {
        return inE(0, step, over);
    }

    @Override
    public Traversal inE(Collection<Class<? extends Model.E>> over) {
        return inE(1, over);
    }

    @Override
    public Traversal inE(Class<? extends Model.E> over) {
        return inE(1, over);
    }

    @Override
    public Traversal inE(Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return inE(1, first, second);
    }

    @Override
    public Traversal inE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return inE(1, first, second, third);
    }

    @Override
    public Traversal inE(String[] over) {
        return inE(1, over);
    }

    @Override
    public Traversal outE(int stepM, int stepN, Collection<Class<? extends Model.E>> over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over == null || over.isEmpty() ? null : over;
        arguments[DIRECTION_OUT_INDEX] = true;

        return this;
    }

    @Override
    public Traversal outE(int stepM, int stepN, Class<? extends Model.E> over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over;
        arguments[DIRECTION_OUT_INDEX] = true;

        return this;
    }

    @Override
    public Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        if (first == null) throw new MapleDslExecutionException("edgeType `first` must not be null.");
        if (second == null) throw new MapleDslExecutionException("edgeType `second` must not be null.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = new Class<?>[]{ first, second };
        arguments[DIRECTION_OUT_INDEX] = true;

        return this;
    }

    @Override
    public Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        if (first == null) throw new MapleDslExecutionException("edgeType `first` must not be null.");
        if (second == null) throw new MapleDslExecutionException("edgeType `second` must not be null.");
        if (third == null) throw new MapleDslExecutionException("edgeType `third` must not be null.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = new Class<?>[]{ first, second, third };
        arguments[DIRECTION_OUT_INDEX] = true;

        return this;
    }

    @Override
    public Traversal outE(int stepM, int stepN, String[] over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over == null || over.length == 0 ? null : over;
        arguments[DIRECTION_OUT_INDEX] = true;

        return this;
    }

    @Override
    public Traversal outE(int step, Collection<Class<? extends Model.E>> over) {
        return outE(0, step, over);
    }

    @Override
    public Traversal outE(int step, Class<? extends Model.E> over) {
        return outE(0, step, over);
    }

    @Override
    public Traversal outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return outE(0, step, first, second);
    }

    @Override
    public Traversal outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return outE(0, step, first, second, third);
    }

    @Override
    public Traversal outE(int step, String[] over) {
        return outE(0, step, over);
    }

    @Override
    public Traversal outE(Collection<Class<? extends Model.E>> over) {
        return outE(1, over);
    }

    @Override
    public Traversal outE(Class<? extends Model.E> over) {
        return outE(1, over);
    }

    @Override
    public Traversal outE(Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return outE(1, first, second);
    }

    @Override
    public Traversal outE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return outE(1, first, second, third);
    }

    @Override
    public Traversal outE(String[] over) {
        return outE(1, over);
    }

    @Override
    public Traversal bothE(int stepM, int stepN, Collection<Class<? extends Model.E>> over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over == null || over.isEmpty() ? null : over;
        arguments[DIRECTION_BOTH_INDEX] = true;

        return this;
    }

    @Override
    public Traversal bothE(int stepM, int stepN, Class<? extends Model.E> over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over;
        arguments[DIRECTION_BOTH_INDEX] = true;

        return this;
    }

    @Override
    public Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        if (first == null) throw new MapleDslExecutionException("edgeType `first` must not be null.");
        if (second == null) throw new MapleDslExecutionException("edgeType `second` must not be null.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = new Class<?>[]{ first, second };
        arguments[DIRECTION_BOTH_INDEX] = true;

        return this;
    }

    @Override
    public Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0.");
        if (stepN < 1) throw new MapleDslExecutionException("stepN must not >= 1.");
        if (first == null) throw new MapleDslExecutionException("edgeType `first` must not be null.");
        if (second == null) throw new MapleDslExecutionException("edgeType `second` must not be null.");
        if (third == null) throw new MapleDslExecutionException("edgeType `third` must not be null.");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = new Class<?>[]{ first, second, third};
        arguments[DIRECTION_BOTH_INDEX] = true;

        return this;
    }

    @Override
    public Traversal bothE(int stepM, int stepN, String[] over) {
        if (stepM < 0) throw new MapleDslExecutionException("stepM must not >= 0");
        if (stepN < 1) throw new MapleDslExecutionException("stepM must not >= 1");
        // if over is null, it will render as `*`
        if (arguments[OVER_INDEX] != null) nextTraversal(false);

        arguments[STEP_M_INDEX] = stepM;
        arguments[STEP_N_INDEX] = stepN;
        arguments[OVER_INDEX] = over == null || over.length == 0 ? null : over;
        arguments[DIRECTION_BOTH_INDEX] = true;

        return this;
    }

    @Override
    public Traversal bothE(int step, Collection<Class<? extends Model.E>> over) {
        return bothE(0, step, over);
    }

    @Override
    public Traversal bothE(int step, Class<? extends Model.E> over) {
        return bothE(0, step, over);
    }

    @Override
    public Traversal bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return bothE(0, step, first, second);
    }

    @Override
    public Traversal bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return bothE(0, step, first, second, third);
    }

    @Override
    public Traversal bothE(int step, String[] over) {
        return bothE(0, step, over);
    }

    @Override
    public Traversal bothE(Collection<Class<? extends Model.E>> over) {
        return bothE(1, over);
    }

    @Override
    public Traversal bothE(Class<? extends Model.E> over) {
        return bothE(1, over);
    }

    @Override
    public Traversal bothE(Class<? extends Model.E> first, Class<? extends Model.E> second) {
        return bothE(1, first, second);
    }

    @Override
    public Traversal bothE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
        return bothE(1, first, second, third);
    }

    @Override
    public Traversal bothE(String[] over) {
        return bothE(1, over);
    }

    @Override
    public <V extends Model.V> Traversal outV(String alias, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[OUT_ALIAS_INDEX])) throw new IllegalArgumentException("outV alias has been defined.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("outV alias must not be empty.");
        this.arguments[OUT_ALIAS_INDEX] = alias;

        final StepWrapper<V> outStep = new StepWrapper<>(it -> it.setOut(true).setInstantiatedAlias(alias));
        step.accept(outStep);
        outStep.rebaseNextTraversal();
        outStep.sink();
        return this;
    }

    @Override
    public <V extends Model.V> Traversal outV(String alias, Class<V> label, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[OUT_ALIAS_INDEX])) throw new IllegalArgumentException("outV alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("outV label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("outV alias must not be empty.");
        this.arguments[OUT_ALIAS_INDEX] = alias;

        final StepWrapper<V> outStep = new StepWrapper<>(it -> it.setOut(true).setInstantiatedLabelClazz(label).setInstantiatedAlias(alias));
        step.accept(outStep);
        outStep.rebaseNextTraversal();
        outStep.sink();
        return this;
    }

    @Override
    public <V extends Model.V> Traversal outV(String alias, String label, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[OUT_ALIAS_INDEX])) throw new IllegalArgumentException("outV alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("outV label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("outV alias must not be empty.");
        this.arguments[OUT_ALIAS_INDEX] = alias;

        final StepWrapper<V> outStep = new StepWrapper<>(it -> it.setOut(true).setInstantiatedLabel(label).setInstantiatedAlias(alias));
        step.accept(outStep);
        outStep.rebaseNextTraversal();
        outStep.sink();
        return this;
    }

    @Override
    public <V extends Model.V> Traversal inV(String alias, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[IN_ALIAS_INDEX])) throw new IllegalArgumentException("inV alias has been defined.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("inV alias must not be empty.");
        this.arguments[IN_ALIAS_INDEX] = alias;

        final StepWrapper<V> inStep = new StepWrapper<>(it -> it.setIn(true).setInstantiatedAlias(alias));
        step.accept(inStep);
        inStep.sink();
        return this;
    }

    @Override
    public <V extends Model.V> Traversal inV(String alias, Class<V> label, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[IN_ALIAS_INDEX])) throw new IllegalArgumentException("inV alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("inV label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("inV alias must not be empty.");
        this.arguments[IN_ALIAS_INDEX] = alias;

        final StepWrapper<V> inStep = new StepWrapper<>(it -> it.setIn(true).setInstantiatedLabelClazz(label));
        step.accept(inStep);
        inStep.sink();
        return this;
    }

    @Override
    public <V extends Model.V> Traversal inV(String alias, String label, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[IN_ALIAS_INDEX])) throw new IllegalArgumentException("inV alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("inV label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("inV alias must not be empty.");
        this.arguments[IN_ALIAS_INDEX] = alias;

        final StepWrapper<V> inStep = new StepWrapper<>(it -> it.setIn(true).setInstantiatedLabel(label).setInstantiatedAlias(alias));
        step.accept(inStep);
        inStep.sink();
        return this;
    }

    @Override
    public <E extends Model.E> Traversal edge(String alias, Class<E> label, Consumer<Step<E>> step) {
        if (Objects.nonNull(arguments[EDGE_ALIAS_INDEX])) throw new IllegalArgumentException("edge alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("edge label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("edge alias must not be empty.");
        this.arguments[EDGE_ALIAS_INDEX] = alias;

        final StepWrapper<E> edgeStep = new StepWrapper<>(it -> it.setE(true).setInstantiatedLabelClazz(label).setInstantiatedAlias(alias));
        step.accept(edgeStep);
        edgeStep.sink();
        return this;
    }

    @Override
    public <E extends Model.E> Traversal edge(String alias, Consumer<Step<E>> step) {
        if (Objects.nonNull(arguments[EDGE_ALIAS_INDEX])) throw new IllegalArgumentException("edge alias has been defined.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("edge alias must not be empty.");
        this.arguments[EDGE_ALIAS_INDEX] = alias;

        final StepWrapper<E> edgeStep = new StepWrapper<>(it -> it.setE(true).setInstantiatedAlias(alias));
        step.accept(edgeStep);
        edgeStep.sink();
        return this;
    }

    @Override
    public <E extends Model.E> Traversal edge(String alias, String label, Consumer<Step<E>> stepConsumer) {
        if (Objects.nonNull(arguments[EDGE_ALIAS_INDEX])) throw new IllegalArgumentException("edge alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("edge label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("edge alias must not be empty.");
        this.arguments[EDGE_ALIAS_INDEX] = alias;

        final StepWrapper<E> edgeStep = new StepWrapper<>(it -> it.setE(true).setInstantiatedLabel(label).setInstantiatedAlias(alias));
        stepConsumer.accept(edgeStep);
        edgeStep.sink();
        return this;
    }

    class StepWrapper<M extends Model<?>> extends QueryDuplexWrapper<M, Step<M>> implements Step<M> {
        private final Consumer<MapleDslDialectBase<M>> decorator;

        StepWrapper(Consumer<MapleDslDialectBase<M>> decorator) {
            super(new QueryWrapper<>(decorator), new ConditionWrapper<>(decorator));
            this.decorator = decorator;
        }

        @Override
        public Sort<M> select(String first, String... columns) {
            super.select(first, columns);
            nextTraversalCompanionSet.add(first);

            if (columns.length == 0) return this;
            Collections.addAll(nextTraversalCompanionSet, columns);
            return this;
        }

        @Override
        public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col) {
            requireNonNull(col);
            return select(col.asText());
        }

        @Override
        public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
            requireNonNull(col1);
            requireNonNull(col2);
            return select(col1.asText(), col2.asText());
        }

        @Override
        public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
            requireNonNull(col1);
            requireNonNull(col2);
            requireNonNull(col3);
            return select(col1.asText(), col2.asText(), col3.asText());
        }

        @Override
        public Sort<M> selectAs(String column, String alias) {
            super.selectAs(column, alias);
            nextTraversalCompanionSet.add(alias);
            return this;
        }

        @Override
        public Sort<M> selectAs(SerializableFunction<M, ?> column, String alias) {
            requireNonNull(column);
            return selectAs(column.asText(), alias);
        }

        @Override
        public void noneSelect() {
            selection.headFunc = null;
            selection.headSelect = null;
        }

        @Override
        public void allSelect() {
            if (selection.headSelect != null) selection.headSelect = null;
            selection.headSelect = new MapleDslDialectSelection<>(true);
            decorator.accept(selection.headSelect);
        }

        @Override
        protected Step<M> instance() {
            return this;
        }

        private void rebaseNextTraversal() {
            // quickly-check selection whether for out vertex.
            if (!selection.headSelect.out()) return;
            // check selection chains whether alias contains "ID".

            for (MapleDslDialectSelection<M> cur = selection.headSelect;; cur = cur.next) {
                int index = Arrays.binarySearch(cur.columns(), Model.ID,
                        Comparator.comparing(Function.identity(), String::compareToIgnoreCase)
                );
                if (index != -1) {
                    nextTraversalFrom = cur.aliases()[index];
                    return;
                }

                if (!cur.hasNext()) break;
            }

            selection.selectAs(Model.ID, DEFAULT_NEXT_TRAVERSAL_FROM_ALIAS);
            nextTraversalFrom = DEFAULT_NEXT_TRAVERSAL_FROM_ALIAS;
        }

        private void sink() {
            if (selection.headSelect != null)          selectionList.add(selection.headSelect);
            if (selection.headFunc != null)            functionList.add(selection.headFunc);
            if (predicate.head != null)                predicateList.add(predicate.head);
            if (!selection.orderAscSet.isEmpty())      orderAscList.addAll(selection.orderAscSet);
            if (!selection.orderDescSet.isEmpty())     orderDescList.addAll(selection.orderDescSet);
        }
    }
}
