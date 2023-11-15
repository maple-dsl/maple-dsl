package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Wrapper;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FetchWrapper<M extends Model<?>> extends DuplexWrapper<M, FetchWrapper<M>> implements Wrapper {
    static final int REF_INDEX = 0;
    static final int LABEL_INDEX = 1;
    static final int FROM_INDEX = 2;
    static final int SELECTION_INDEX = 3;
    static final int SHADOW_SELECTION_INDEX = 4;
    static final int FUNCTION_INDEX = 5;
    static final int ORDER_ASC_INDEX = 6;
    static final int ORDER_DESC_INDEX = 7;
    static final int SKIP_INDEX = 8;
    static final int LIMIT_INDEX = 9;
    /**
     * Arguments position:
     * <pre>
     * [0] ref         [1] label
     * [2] from
     * [3] selection   [4] shadow_selection
     * [5] function
     * [6] orderAsc    [7] orderDsc
     * [8] skip        [9] limit
     * </pre>
     */
    final Object[] arguments = new Object[10];
    final BiFunction<MapleDslConfiguration, Object[], String> renderFunc;

    protected <R> FetchWrapper(@NotNull String reference, @NotNull String label, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(renderModelDecorator);
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = label;
        this.arguments[FROM_INDEX] = vertices;
    }

    protected <R> FetchWrapper(@NotNull String reference, @NotNull Class<M> labelClazz, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(renderModelDecorator);
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = labelClazz;
        this.arguments[FROM_INDEX] = vertices;
    }

    @Override
    protected FetchWrapper<M> instance() {
        return this;
    }

    @Override
    public String render(MapleDslConfiguration context) {
        if (selection.headSelect == null && selection.headShadowSelect == null) {
            super.selection.next(new MapleDslDialectSelection<>(true));
        }

        this.arguments[SELECTION_INDEX] = selection.headSelect;
        this.arguments[SHADOW_SELECTION_INDEX] = selection.headShadowSelect;
        this.arguments[FUNCTION_INDEX] = selection.headFunc;
        this.arguments[ORDER_ASC_INDEX] = selection.orderAscSet.isEmpty() ? null : selection.orderAscSet;
        this.arguments[ORDER_DESC_INDEX] = selection.orderDescSet.isEmpty() ? null : selection.orderDescSet;
        this.arguments[SKIP_INDEX] = selection.skip;
        this.arguments[LIMIT_INDEX] = selection.limit;

        return renderFunc.apply(context, arguments);
    }
}
