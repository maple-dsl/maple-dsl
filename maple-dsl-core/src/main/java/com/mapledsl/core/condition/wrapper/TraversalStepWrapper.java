package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.TraversalStep;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class TraversalStepWrapper extends TraversalWrapper implements TraversalStep {
    protected TraversalStepWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
        super(renderFunc);
    }

    public TraversalStepWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String fromFragment) {
        super(renderFunc, fromFragment);
    }

    @Override
    public TraversalStepWrapper inE(int stepM, int stepN, Collection<Class<? extends Model.E<?,?>>> over) {
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
    public TraversalStepWrapper inE(int stepM, int stepN, Class<? extends Model.E<?,?>> over) {
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
    public TraversalStepWrapper inE(int stepM, int stepN, Class<? extends Model.E<?,?>> first, Class<? extends Model.E<?,?>> second) {
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
    public TraversalStepWrapper inE(int stepM, int stepN, Class<? extends Model.E<?,?>> first, Class<? extends Model.E<?,?>> second, Class<? extends Model.E<?,?>> third) {
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
    public TraversalStepWrapper inE(int stepM, int stepN, @NotNull String[] over) {
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
    public TraversalStepWrapper outE(int stepM, int stepN, Collection<Class<? extends Model.E<?,?>>> over) {
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
    public TraversalStepWrapper outE(int stepM, int stepN, Class<? extends Model.E<?,?>> over) {
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
    public TraversalStepWrapper outE(int stepM, int stepN, Class<? extends Model.E<?,?>> first, Class<? extends Model.E<?,?>> second) {
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
    public TraversalStepWrapper outE(int stepM, int stepN, Class<? extends Model.E<?,?>> first, Class<? extends Model.E<?,?>> second, Class<? extends Model.E<?,?>> third) {
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
    public TraversalStepWrapper outE(int stepM, int stepN, String[] over) {
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
    public TraversalStepWrapper bothE(int stepM, int stepN, Collection<Class<? extends Model.E<?,?>>> over) {
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
    public TraversalStepWrapper bothE(int stepM, int stepN, Class<? extends Model.E<?,?>> over) {
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
    public TraversalStepWrapper bothE(int stepM, int stepN, Class<? extends Model.E<?,?>> first, Class<? extends Model.E<?,?>> second) {
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
    public TraversalStepWrapper bothE(int stepM, int stepN, Class<? extends Model.E<?,?>> first, Class<? extends Model.E<?,?>> second, Class<? extends Model.E<?,?>> third) {
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
    public TraversalStepWrapper bothE(int stepM, int stepN, String[] over) {
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
    public <V extends Model.V<?>> TraversalStepWrapper outV(String alias, Consumer<Step<V>> step) {
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
    public <V extends Model.V<?>> TraversalStepWrapper outV(String alias, Class<V> label, Consumer<Step<V>> step) {
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
    public <V extends Model.V<?>> TraversalStepWrapper outV(String alias, String label, Consumer<Step<V>> step) {
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
    public <V extends Model.V<?>> TraversalStepWrapper inV(String alias, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[IN_ALIAS_INDEX])) throw new IllegalArgumentException("inV alias has been defined.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("inV alias must not be empty.");
        this.arguments[IN_ALIAS_INDEX] = alias;

        final StepWrapper<V> inStep = new StepWrapper<>(it -> it.setIn(true).setInstantiatedAlias(alias));
        step.accept(inStep);
        inStep.sink();
        return this;
    }

    @Override
    public <V extends Model.V<?>> TraversalStepWrapper inV(String alias, Class<V> label, Consumer<Step<V>> step) {
        if (Objects.nonNull(arguments[IN_ALIAS_INDEX])) throw new IllegalArgumentException("inV alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("inV label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("inV alias must not be empty.");
        this.arguments[IN_ALIAS_INDEX] = alias;

        final StepWrapper<V> inStep = new StepWrapper<>(it -> it.setIn(true).setInstantiatedLabelClazz(label).setInstantiatedAlias(alias));
        step.accept(inStep);
        inStep.sink();
        return this;
    }

    @Override
    public <V extends Model.V<?>> TraversalStepWrapper inV(String alias, String label, Consumer<Step<V>> step) {
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
    public <E extends Model.E<?,?>> TraversalStepWrapper edge(String alias, Class<E> label, Consumer<Step<E>> step) {
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
    public <E extends Model.E<?,?>> TraversalStepWrapper edge(String alias, Consumer<Step<E>> step) {
        if (Objects.nonNull(arguments[EDGE_ALIAS_INDEX])) throw new IllegalArgumentException("edge alias has been defined.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("edge alias must not be empty.");
        this.arguments[EDGE_ALIAS_INDEX] = alias;

        final StepWrapper<E> edgeStep = new StepWrapper<>(it -> it.setE(true).setInstantiatedAlias(alias));
        step.accept(edgeStep);
        edgeStep.sink();
        return this;
    }

    @Override
    public <E extends Model.E<?,?>> TraversalStepWrapper edge(String alias, String label, Consumer<Step<E>> stepConsumer) {
        if (Objects.nonNull(arguments[EDGE_ALIAS_INDEX])) throw new IllegalArgumentException("edge alias has been defined.");
        if (Objects.isNull(label)) throw new IllegalArgumentException("edge label must not be null.");
        if (Objects.isNull(alias) || alias.trim().isEmpty()) throw new IllegalArgumentException("edge alias must not be empty.");
        this.arguments[EDGE_ALIAS_INDEX] = alias;

        final StepWrapper<E> edgeStep = new StepWrapper<>(it -> it.setE(true).setInstantiatedLabel(label).setInstantiatedAlias(alias));
        stepConsumer.accept(edgeStep);
        edgeStep.sink();
        return this;
    }

    @Override
    public TraversalStepWrapper limit(int limit) {
        return limit(0, limit);
    }

    @Override
    public TraversalStepWrapper limit(int skip, int limit) {
        arguments[SKIP_INDEX] = skip;
        arguments[LIMIT_INDEX] = limit;
        return this;
    }

    class StepWrapper<M extends Model<?>> extends DuplexWrapper<M, Step<M>> implements Step<M>, UnsupportedWrapper<M> {

        public StepWrapper(Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
            super(renderModelDecorator,
                    // java.lang.VerifyError: Bad type on operand stack
                    // Lambda expression does not ref `this.curTraversalCompanionSet`, bcuz the `curTraversalCompanionSet` is outer this member.
                    // It should be referenced the outer `this`
                    selection -> {
                        if (selection.isAllPresent()) return;
                        Collections.addAll(TraversalStepWrapper.this.curTraversalCompanionSet, selection.aliases());
                    },
                    function -> TraversalStepWrapper.this.curTraversalCompanionSet.add(function.alias())
            );
        }

        @Override
        public QueryWrapper<M> selectAll() {
            selection.next(new MapleDslDialectSelection<>(true));
            return selection;
        }

        @Override
        protected StepWrapper<M> instance() {
            return this;
        }

        private void rebaseNextTraversal() {
            // quickly-check selection whether for out vertex.
            if (selection.headSelect == null) return;
            if (!selection.headSelect.out()) return;
            if (nextTraversalFrom != null) return;
            // check selection chains whether alias contains "ID".
            for (MapleDslDialectSelection<M> cur = selection.headSelect;;cur = cur.next) {
                if (cur.isAllPresent()) continue;

                int index = Arrays.binarySearch(cur.columns(), Model.ID,
                        Comparator.comparing(Function.identity(), String::compareToIgnoreCase)
                );
                if (index > -1) {
                    nextTraversalFrom = cur.aliases()[index];
                    return;
                }

                if (!cur.hasNext()) break;
            }
        }

        private void sink() {
            if (selection.headSelect != null)          selectionList.add(selection.headSelect);
            if (selection.headShadowSelect != null)    shadowSelectionList.add(selection.headShadowSelect);
            if (selection.headFunc != null)            functionList.add(selection.headFunc);
            if (predicate.head != null)                predicateList.add(predicate.head);
            if (!selection.orderAscSet.isEmpty())      orderAscList.addAll(selection.orderAscSet);
            if (!selection.orderDescSet.isEmpty())     orderDescList.addAll(selection.orderDescSet);
        }
    }
}
