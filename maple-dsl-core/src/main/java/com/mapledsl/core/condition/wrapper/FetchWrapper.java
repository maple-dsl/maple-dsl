package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Query;
import com.mapledsl.core.condition.Wrapper;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class FetchWrapper<M extends Model<?>> implements Wrapper<M>, Query<M> {
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
     * [6] orderAsc    [7] orderDesc
     * [8] skip        [9] limit
     * </pre>
     */
    final Object[] arguments = new Object[10];
    final SortWrapper<M> selection;
    final BiFunction<MapleDslConfiguration, Object[], String> renderFunc;

    protected <R> FetchWrapper(@NotNull String reference, @NotNull String label, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        this.selection = new SortWrapper<>(renderModelDecorator, this);
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = label;
        this.arguments[FROM_INDEX] = vertices;
    }

    protected <R> FetchWrapper(@NotNull String reference, @NotNull Class<M> labelClazz, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Consumer<MapleDslDialectBase<M>> renderModelDecorator) {
        this.selection = new SortWrapper<>(renderModelDecorator, this);
        this.renderFunc = renderFunc;
        this.arguments[REF_INDEX] = reference;
        this.arguments[LABEL_INDEX] = labelClazz;
        this.arguments[FROM_INDEX] = vertices;
    }

    @Override
    public String render(MapleDslConfiguration context) {
        if (selection.headSelect == null && selection.headShadowSelect == null) {
            selection.shadow(new MapleDslDialectSelection<>(true));
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

    public Wrapper<M> limit(int limit) {
        return limit(0, limit);
    }

    public Wrapper<M> limit(int skip, int limit) {
        selection.skip = skip;
        selection.limit = limit;
        return this;
    }

    @Override
    public SortWrapper<M> select(String first, String... columns) {
        return selection.select(first, columns);
    }

    @Override
    public SortWrapper<M> select(boolean condition, String first, String... columns) {
        return selection.select(condition, first, columns);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col) {
        return selection.select(col);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col) {
        return selection.select(condition, col);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        return selection.select(col1, col2);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        return selection.select(condition, col1, col2);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
        return selection.select(col1, col2, col3);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
        return selection.select(condition, col1, col2, col3);
    }

    @Override
    @SafeVarargs
    public final <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others) {
        return selection.select(col1, col2, col3, others);
    }

    @Override
    @SafeVarargs
    public final <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others) {
        return selection.select(condition, col1, col2, col3, others);
    }

    @Override
    public SortWrapper<M> selectAs(String column, String alias) {
        return selection.selectAs(column, alias);
    }

    @Override
    public SortWrapper<M> selectAs(SerializableFunction<M, ?> column, String alias) {
        return selection.selectAs(column, alias);
    }

    @Override
    public SortWrapper<M> count(String alias) {
        return selection.count(alias);
    }

    @Override
    public SortWrapper<M> count(String column, String alias) {
        return selection.count(column, alias);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> count(SerializableFunction<M, R> column, String alias) {
        return selection.count(column, alias);
    }

    @Override
    public SortWrapper<M> sum(String column, String alias) {
        return selection.sum(column, alias);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> sum(SerializableFunction<M, R> column, String alias) {
        return selection.sum(column, alias);
    }

    @Override
    public SortWrapper<M> avg(String column, String alias) {
        return selection.avg(column, alias);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> avg(SerializableFunction<M, R> column, String alias) {
        return selection.avg(column, alias);
    }

    @Override
    public SortWrapper<M> min(String column, String alias) {
        return selection.min(column, alias);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> min(SerializableFunction<M, R> column, String alias) {
        return selection.min(column, alias);
    }

    @Override
    public SortWrapper<M> max(String column, String alias) {
        return selection.max(column, alias);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> max(SerializableFunction<M, R> column, String alias) {
        return selection.max(column, alias);
    }
}
