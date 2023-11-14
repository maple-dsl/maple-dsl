package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.model.Model;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class MatchWrapper<M extends Model<?>> extends DuplexWrapper<M, MatchWrapper<M>> {
    static final int REF_INDEX = 0;
    static final int LABEL_INDEX = 1;
    static final int SELECTION_INDEX = 2;
    static final int SHADOW_SELECTION_INDEX = 3;
    static final int PREDICATE_INDEX = 4;
    static final int FUNCTION_INDEX = 5;
    static final int ORDER_ASC_INDEX = 6;
    static final int ORDER_DESC_INDEX = 7;
    static final int SKIP_INDEX = 8;
    static final int LIMIT_INDEX = 9;
    protected static final int DELETE_INDEX = 10;
    protected static final int DETACH_INDEX = 11;
    protected static final int TRAVERSE_INDEX = 12;
    /**
     * Arguments position:
     * <pre>
     * [0] ref         [1] tag
     * [2] selection   [3] shadow_selection
     * [4] predicate
     * [5] function
     * [6] orderAsc    [7] orderDesc
     * [8] skip        [9] limit
     * [10] delete     [11] detach(only:vertex)
     * [12] traverse(only:vertex)
     * </pre>
     */
    protected final Object[] arguments = new Object[13];
    private final BiFunction<MapleDslConfiguration, Object[], String> renderFunc;

    protected MatchWrapper(String reference, String label, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(renderModelDecorator);
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = label;
    }

    protected MatchWrapper(String reference, Class<M> labelClazz, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        super(renderModelDecorator);
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = labelClazz;
    }

    @Override
    public String render(MapleDslConfiguration context) {
        if (selection.headSelect == null) {
            super.selection.next(new MapleDslDialectSelection<>(true));
        }

        this.arguments[PREDICATE_INDEX] = predicate.head;
        this.arguments[SELECTION_INDEX] = selection.headSelect;
        this.arguments[SHADOW_SELECTION_INDEX] = selection.headShadowSelect;
        this.arguments[FUNCTION_INDEX] = selection.headFunc;
        this.arguments[ORDER_ASC_INDEX] = selection.orderAscSet.isEmpty() ? null : selection.orderAscSet;
        this.arguments[ORDER_DESC_INDEX] = selection.orderDescSet.isEmpty() ? null : selection.orderDescSet;
        this.arguments[SKIP_INDEX] = selection.skip;
        this.arguments[LIMIT_INDEX] = selection.limit;

        return renderFunc.apply(context, arguments);
    }

    @Override
    protected MatchWrapper<M> instance() {
        return this;
    }
}
