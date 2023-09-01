package com.mapledsl.core;

import com.mapledsl.core.MapleDslDialectPredicateRender.PredicateRendererModel;
import com.mapledsl.core.MapleDslDialectSelectionRender.SelectionRendererModel;
import com.mapledsl.core.condition.Fetch;
import com.mapledsl.core.condition.Traversal;
import com.mapledsl.core.condition.common.Direction;
import com.mapledsl.core.condition.common.P;
import com.mapledsl.core.condition.common.T;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class GraphTraversals {
    /**
     * A domain specific condition wrapper for traversing a graph using "graph concepts" (e.g. vertices, edges).
     * Represents a directed walk over a graph {@code Graph}.
     *
     * @param vertexId specific the id of vertex {@code Vertex}.
     * @param <T>      specific the type of the vertex entity {@code Model.V}.
     * @return the basic traversing condition wrapper of the specific id of vertex as origin.
     */
    @Contract("_ -> new")
    public static @NotNull Traversal from(String vertexId) {
        return new TW(vertexId);
    }

    @Contract("_ -> new")
    public static  @NotNull Traversal from(Number vertexId) {
        return new TW(vertexId);
    }

    @Contract("_ -> new")
    public static  @NotNull Traversal from(String... vertexIds) {
        return new TW(vertexIds);
    }

    @Contract("_ -> new")
    public static @NotNull Traversal from(Number... vertexIds) {
        return new TW(vertexIds);
    }

    @Contract("_ -> new")
    public static Traversal from(Fetch<Model.V> fetch) {
        return new TW(fetch);
    }

    static class TW implements Traversal {
        static final int LENGTH = 12;
        static final int STEP_M_INDEX = 0;
        static final int STEP_N_INDEX = 1;
        static final int FROM_INDEX = 2;
        static final int OVER_INDEX = 3;
        static final int DIRECTION_INDEX = 4;
        static final int PREDICATE_INDEX = 5;
        static final int SELECTION_INDEX = 6;
        static final int PINNED_INDEX = 7;
        static final int ORDER_ASC_INDEX = 8;
        static final int ORDER_DSC_INDEX = 9;
        static final int SKIP_INDEX = 10;
        static final int LIMIT_INDEX = 11;

        /**
         * Position arguments distributed like below:
         * [0] stepM        [1] stepN
         * [2] from         [3] over  [4] direction
         * [5] where
         * [6] select       [7] pinned_select
         * [8] orderAsc     [9] orderDsc
         * [10] skip        [11] limit
         */
        final LinkedList<Object[]> argumentsList = new LinkedList<>();
        final List<String> pinnedList = new LinkedList<>();
        final List<String> orderAscList = new ArrayList<>();
        final List<String> orderDescList = new ArrayList<>();

        Object[] arguments = init();
        PredicateRendererModel<?>[] inPredicates, outPredicates, ePredicates;
        SelectionRendererModel<?>[] inSelections, outSelections, eSelections;

        /**
         * Traversal the vertices#ID of the graph.
         *
         * @param from {@link Model.V#id()}
         */
        <R> TW(R from) {
            this.arguments[FROM_INDEX] = from;
        }

        Object[] empty() {
            return null;
        }

        Object[] init() {
            return new Object[LENGTH];
        }

        void addAllPredicates(Collection<PredicateRendererModel<?>> container, PredicateRendererModel<?>[] predicates) {
            if (predicates == null) return;
            Collections.addAll(container, predicates);
        }

        void addAllSelection(Collection<SelectionRendererModel<?>> container, SelectionRendererModel<?>[] selections) {
            if (selections == null) return;
            for (SelectionRendererModel<?> selection : selections) {
                if (selection.base.isAscending())   Collections.addAll(orderAscList, selection.base.aliases());
                if (selection.base.isDescending())  Collections.addAll(orderDescList, selection.base.aliases());
                if (selection.base.isPinning())     Collections.addAll(pinnedList, selection.base.aliases());

                container.add(selection);
            }
        }

        void nextTraversal(Supplier<Object[]> argumentSupplier) {
            final List<PredicateRendererModel<?>> predicateList = new ArrayList<>();
            addAllPredicates(predicateList, inPredicates);
            addAllPredicates(predicateList, outPredicates);
            addAllPredicates(predicateList, ePredicates);

            final List<SelectionRendererModel<?>> selectionList = new ArrayList<>();
            addAllSelection(selectionList, inSelections);
            addAllSelection(selectionList, outSelections);
            addAllSelection(selectionList, eSelections);

            arguments[PREDICATE_INDEX]  = predicateList.isEmpty()   ? null : predicateList;
            arguments[SELECTION_INDEX]  = selectionList.isEmpty()   ? null : selectionList;
            arguments[ORDER_ASC_INDEX]  = orderAscList.isEmpty()    ? null : orderAscList;
            arguments[ORDER_DSC_INDEX]  = orderDescList.isEmpty()   ? null : orderDescList;
            arguments[PINNED_INDEX]     = pinnedList.isEmpty()      ? null : pinnedList;

            argumentsList.add(arguments);
            arguments = argumentSupplier.get();
        }

        @Override
        public String render(MapleDslConfiguration configuration) {
            nextTraversal(this::empty);

            final StringBuilder builder = new StringBuilder();
            for (Object[] args : argumentsList) {
                String sql = MapleDslDialectRender.traversal.apply(configuration, args);
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
        public Traversal inE(int stepM, int stepN, Class<? extends Model.E>[] over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.IN;

            return this;
        }

        @Override
        public Traversal inE(int stepM, int stepN, Class<? extends Model.E> over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.IN;

            return this;
        }

        @Override
        public Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = new Class<?>[]{ first, second };
            arguments[DIRECTION_INDEX] = Direction.IN;

            return this;
        }

        @Override
        public Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = new Class<?>[]{ first, second, third };
            arguments[DIRECTION_INDEX] = Direction.IN;

            return this;
        }

        @Override
        public Traversal inE(int stepM, int stepN, String[] over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.IN;

            return this;
        }

        @Override
        public Traversal inE(int step, Class<? extends Model.E>[] over) {
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
        public Traversal inE(Class<? extends Model.E>[] over) {
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
        public Traversal outE(int stepM, int stepN, Class<? extends Model.E>[] over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.OUT;

            return this;
        }

        @Override
        public Traversal outE(int stepM, int stepN, Class<? extends Model.E> over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.OUT;

            return this;
        }

        @Override
        public Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = new Class<?>[]{ first, second };
            arguments[DIRECTION_INDEX] = Direction.OUT;

            return this;
        }

        @Override
        public Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = new Class<?>[]{ first, second, third};
            arguments[DIRECTION_INDEX] = Direction.OUT;

            return this;
        }

        @Override
        public Traversal outE(int stepM, int stepN, String[] over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.OUT;

            return this;
        }

        @Override
        public Traversal outE(int step, Class<? extends Model.E>[] over) {
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
        public Traversal outE(Class<? extends Model.E>[] over) {
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
        public Traversal bothE(int stepM, int stepN, Class<? extends Model.E>[] over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.BOTH;

            return this;
        }

        @Override
        public Traversal bothE(int stepM, int stepN, Class<? extends Model.E> over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.BOTH;

            return this;
        }

        @Override
        public Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = new Class<?>[]{ first, second };
            arguments[DIRECTION_INDEX] = Direction.BOTH;

            return this;
        }

        @Override
        public Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = new Class<?>[]{ first, second, third};
            arguments[DIRECTION_INDEX] = Direction.BOTH;

            return this;
        }

        @Override
        public Traversal bothE(int stepM, int stepN, String[] over) {
            // if over is null, it will render as `*`
            if (arguments[OVER_INDEX] != null) nextTraversal(this::init);

            arguments[STEP_M_INDEX] = stepM;
            arguments[STEP_N_INDEX] = stepN;
            arguments[OVER_INDEX] = over;
            arguments[DIRECTION_INDEX] = Direction.BOTH;

            return this;
        }

        @Override
        public Traversal bothE(int step, Class<? extends Model.E>[] over) {
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
        public Traversal bothE(Class<? extends Model.E>[] over) {
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
        public <V extends Model.V> Traversal inV(P<V>[] predicates) {
            requireNonNull(predicates, "Predicates must not be null.");
            inPredicates = new PredicateRendererModel<?>[predicates.length];
            Arrays.setAll(inPredicates, i -> MapleDslDialectPredicateRender.in(predicates[i]));
            return this;
        }

        @Override
        public <V extends Model.V> Traversal inV(P<V> predicate) {
            requireNonNull(predicate, "Predicate must not be null.");
            inPredicates = new PredicateRendererModel<?>[] { MapleDslDialectPredicateRender.in(predicate) };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal inV(P<V> first, P<Model.V> second) {
            requireNonNull(first, "Predicate must not be null.");
            requireNonNull(second, "Predicate must not be null.");
            inPredicates = new PredicateRendererModel<?>[] {
                    MapleDslDialectPredicateRender.in(first),
                    MapleDslDialectPredicateRender.in(second)
            };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal inV(P<V> first, P<Model.V> second, P<Model.V> third) {
            requireNonNull(first, "Predicate must not be null.");
            requireNonNull(second, "Predicate must not be null.");
            requireNonNull(third, "Predicate must not be null.");
            inPredicates = new PredicateRendererModel<?>[] {
                    MapleDslDialectPredicateRender.in(first),
                    MapleDslDialectPredicateRender.in(second),
                    MapleDslDialectPredicateRender.in(third),
            };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal inV(T<V>[] selections) {
            requireNonNull(selections, "Selections must not be null.");
            inSelections = new SelectionRendererModel<?>[selections.length];
            Arrays.setAll(inSelections, i -> MapleDslDialectSelectionRender.in(selections[i]));
            return this;
        }

        @Override
        public <V extends Model.V> Traversal inV(T<V> selection) {
            requireNonNull(selection, "Selection must not be null.");
            inSelections = new SelectionRendererModel<?>[] { MapleDslDialectSelectionRender.in(selection) };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal inV(T<V> first, T<V> second) {
            requireNonNull(first, "Selection must not be null.");
            requireNonNull(second, "Selection must not be null.");
            inSelections = new SelectionRendererModel<?>[] {
                    MapleDslDialectSelectionRender.in(first),
                    MapleDslDialectSelectionRender.in(second)
            };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal inV(T<V> first, T<V> second, T<V> third) {
            requireNonNull(first, "Selection must not be null.");
            requireNonNull(second, "Selection must not be null.");
            requireNonNull(third, "Selection must not be null.");
            inSelections = new SelectionRendererModel<?>[] {
                    MapleDslDialectSelectionRender.in(first),
                    MapleDslDialectSelectionRender.in(second),
                    MapleDslDialectSelectionRender.in(third)
            };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(P<V>[] predicates) {
            requireNonNull(predicates, "Predicates must not be null.");
            outPredicates = new PredicateRendererModel<?>[predicates.length];
            Arrays.setAll(outPredicates, i -> MapleDslDialectPredicateRender.out(predicates[i]));
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(P<V> predicate) {
            requireNonNull(predicate, "Predicate must not be null.");
            outPredicates = new PredicateRendererModel<?>[] { MapleDslDialectPredicateRender.out(predicate) };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(P<V> first, P<V> second) {
            requireNonNull(first, "Predicate must not be null.");
            requireNonNull(second, "Predicate must not be null.");
            outPredicates = new PredicateRendererModel<?>[] {
                    MapleDslDialectPredicateRender.out(first),
                    MapleDslDialectPredicateRender.out(second)
            };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(P<V> first, P<V> second, P<V> third) {
            requireNonNull(first, "Predicate must not be null.");
            requireNonNull(second, "Predicate must not be null.");
            requireNonNull(third, "Predicate must not be null.");
            outPredicates = new PredicateRendererModel<?>[] {
                    MapleDslDialectPredicateRender.out(first),
                    MapleDslDialectPredicateRender.out(second),
                    MapleDslDialectPredicateRender.out(third),
            };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(T<V>[] selections) {
            requireNonNull(selections, "Selections must not be null.");
            outSelections = new SelectionRendererModel<?>[selections.length];
            Arrays.setAll(outSelections, i -> MapleDslDialectSelectionRender.out(selections[i]));
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(T<V> selection) {
            requireNonNull(selection, "Selection must not be null.");
            outSelections = new SelectionRendererModel<?>[] { MapleDslDialectSelectionRender.out(selection) };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(T<V> first, T<V> second) {
            requireNonNull(first, "Selection must not be null.");
            requireNonNull(second, "Selection must not be null.");
            outSelections = new SelectionRendererModel<?>[] {
                    MapleDslDialectSelectionRender.out(first),
                    MapleDslDialectSelectionRender.out(second)
            };
            return this;
        }

        @Override
        public <V extends Model.V> Traversal outV(T<V> first, T<V> second, T<V> third) {
            requireNonNull(first, "Selection must not be null.");
            requireNonNull(second, "Selection must not be null.");
            requireNonNull(third, "Selection must not be null.");
            outSelections = new SelectionRendererModel<?>[] {
                    MapleDslDialectSelectionRender.out(first),
                    MapleDslDialectSelectionRender.out(second),
                    MapleDslDialectSelectionRender.out(third),
            };
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(P<E>[] predicates) {
            requireNonNull(predicates, "Predicates must not be null.");
            ePredicates = new PredicateRendererModel<?>[predicates.length];
            Arrays.setAll(ePredicates, i -> MapleDslDialectPredicateRender.e(predicates[i]));
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(P<E> predicate) {
            requireNonNull(predicate, "Predicate must not be null.");
            ePredicates = new PredicateRendererModel<?>[] { MapleDslDialectPredicateRender.e(predicate) };
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(P<E> first, P<E> second) {
            requireNonNull(first, "Predicate must not be null.");
            requireNonNull(second, "Predicate must not be null.");
            ePredicates = new PredicateRendererModel<?>[] {
                    MapleDslDialectPredicateRender.e(first),
                    MapleDslDialectPredicateRender.e(second)
            };
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(P<E> first, P<E> second, P<E> third) {
            requireNonNull(first, "Predicate must not be null.");
            requireNonNull(second, "Predicate must not be null.");
            requireNonNull(third, "Predicate must not be null.");
            ePredicates = new PredicateRendererModel<?>[] {
                    MapleDslDialectPredicateRender.e(first),
                    MapleDslDialectPredicateRender.e(second),
                    MapleDslDialectPredicateRender.e(third)
            };
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(T<E>[] selections) {
            requireNonNull(selections, "Selections must not be null.");
            eSelections = new SelectionRendererModel<?>[selections.length];
            Arrays.setAll(eSelections, i -> MapleDslDialectSelectionRender.e(selections[i]));
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(T<E> selection) {
            requireNonNull(selection, "Selection must not be null.");
            eSelections = new SelectionRendererModel<?>[] { MapleDslDialectSelectionRender.e(selection) };
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(T<E> first, T<E> second) {
            requireNonNull(first, "Selection must not be null.");
            requireNonNull(second, "Selection must not be null.");
            eSelections = new SelectionRendererModel<?>[] {
                    MapleDslDialectSelectionRender.e(first),
                    MapleDslDialectSelectionRender.e(second)
            };
            return this;
        }

        @Override
        public <E extends Model.E> Traversal E(T<E> first, T<E> second, T<E> third) {
            requireNonNull(first, "Selection must not be null.");
            requireNonNull(second, "Selection must not be null.");
            requireNonNull(third, "Selection must not be null.");
            eSelections = new SelectionRendererModel<?>[] {
                    MapleDslDialectSelectionRender.e(first),
                    MapleDslDialectSelectionRender.e(second),
                    MapleDslDialectSelectionRender.e(third)
            };
            return this;
        }
    }
}
