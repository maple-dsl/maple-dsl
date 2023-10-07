package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Match;
import com.mapledsl.core.condition.Query;
import com.mapledsl.core.condition.Sort;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class MatchWrapper<M extends Model<?>> extends QueryDuplexWrapper<M, Match<M>> implements Match<M>, Sort<M> {
    static final int LABEL_INDEX = 0;
    static final int SELECTION_INDEX = 1;
    static final int PREDICATE_INDEX = 2;
    static final int FUNCTION_INDEX = 3;
    static final int ORDER_ASC_INDEX = 4;
    static final int ORDER_DESC_INDEX = 5;
    static final int SKIP_INDEX = 6;
    static final int LIMIT_INDEX = 7;
    protected static final int DELETE_INDEX = 8;
    protected static final int DETACH_INDEX = 9;
    protected static final int TRAVERSE_INDEX = 10;
    /**
     * Arguments position:
     * <pre>
     * [0] label
     * [1] selection
     * [2] predicate
     * [3] function
     * [4] orderAsc    [5] orderDesc
     * [6] skip        [7] limit
     * [8] delete      [9] detach(only:vertex)
     * [10] traverse(only:vertex)
     * </pre>
     */
    protected final Object[] arguments = new Object[11];
    final BiFunction<MapleDslConfiguration, Object[], String> renderFunc;

    protected MatchWrapper(String label, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(new QueryWrapper<>(renderModelDecorator), new ConditionWrapper<>(renderModelDecorator));
        this.renderFunc = renderFunc;
        this.arguments[LABEL_INDEX] = label;
    }

    protected MatchWrapper(Class<M> labelClazz, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(new QueryWrapper<>(renderModelDecorator), new ConditionWrapper<>(renderModelDecorator));
        this.renderFunc = renderFunc;
        this.arguments[LABEL_INDEX] = labelClazz;
    }

    @Override
    public String render(MapleDslConfiguration context) {
        if (selection.orderAscSet.isEmpty()) arguments[ORDER_ASC_INDEX] = null;
        else arguments[ORDER_ASC_INDEX] = selection.orderAscSet;

        if (selection.orderDescSet.isEmpty()) arguments[ORDER_DESC_INDEX] = null;
        else arguments[ORDER_DESC_INDEX] = selection.orderDescSet;

        this.arguments[PREDICATE_INDEX] = predicate.head;
        this.arguments[SELECTION_INDEX] = selection.headSelect;
        this.arguments[FUNCTION_INDEX] = selection.headFunc;

        return renderFunc.apply(context, arguments);
    }

    @Override
    public Match<M> limit(int limit) {
        return limit(0, limit);
    }

    @Override
    public Match<M> limit(int skip, int limit) {
        this.arguments[SKIP_INDEX] = skip;
        this.arguments[LIMIT_INDEX] = limit;
        return this;
    }

    @Override
    protected Match<M> instance() {
        return this;
    }
}
