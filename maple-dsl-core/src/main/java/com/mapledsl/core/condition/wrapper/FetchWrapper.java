package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Fetch;
import com.mapledsl.core.condition.Sort;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FetchWrapper<M extends Model<?>> extends QueryDuplexWrapper<M, Fetch<M>> implements Fetch<M>, Sort<M> {
    static final int REF_INDEX = 0;
    static final int LABEL_INDEX = 1;
    static final int FROM_INDEX = 2;
    static final int SELECTION_INDEX = 3;
    static final int FUNCTION_INDEX = 4;
    static final int ORDER_ASC_INDEX = 5;
    static final int ORDER_DESC_INDEX = 6;
    static final int SKIP_INDEX = 7;
    static final int LIMIT_INDEX = 8;
    /**
     * Arguments position:
     * <pre>
     * [0] ref         [1] label
     * [2] from
     * [3] selection
     * [4] function
     * [5] orderAsc    [6] orderDsc
     * [7] skip        [8] limit
     * </pre>
     */
    final Object[] arguments = new Object[9];
    final BiFunction<MapleDslConfiguration, Object[], String> renderFunc;

    protected <R> FetchWrapper(@NotNull String reference, @NotNull String label, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(new QueryWrapper<>(renderModelDecorator), new ConditionWrapper<>(renderModelDecorator));
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = label;
        this.arguments[FROM_INDEX] = vertices;
    }

    protected <R> FetchWrapper(@NotNull String reference, @NotNull Class<M> labelClazz, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(new QueryWrapper<>(renderModelDecorator), new ConditionWrapper<>(renderModelDecorator));
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = labelClazz;
        this.arguments[FROM_INDEX] = vertices;
    }

    @Override
    protected Fetch<M> instance() {
        return this;
    }

    @Override
    public String render(MapleDslConfiguration context) {
        if (selection.orderAscSet.isEmpty()) arguments[ORDER_ASC_INDEX] = null;
        else arguments[ORDER_ASC_INDEX] = selection.orderAscSet;

        if (selection.orderDescSet.isEmpty()) arguments[ORDER_DESC_INDEX] = null;
        else arguments[ORDER_DESC_INDEX] = selection.orderDescSet;

        if (selection.headSelect == null) selection.headSelect = new MapleDslDialectSelection<>(true);
        this.arguments[SELECTION_INDEX] = selection.headSelect;
        this.arguments[FUNCTION_INDEX] = selection.headFunc;

        return renderFunc.apply(context, arguments);
    }

    @Override
    public Fetch<M> limit(int limit) {
        return limit(0, limit);
    }

    @Override
    public Fetch<M> limit(int skip, int limit) {
        this.arguments[SKIP_INDEX] = skip;
        this.arguments[LIMIT_INDEX] = limit;
        return this;
    }
}
