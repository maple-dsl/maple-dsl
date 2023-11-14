package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Query;
import com.mapledsl.core.condition.Wrapper;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class QueryWrapper<M extends Model<?>> implements Query<M>, Wrapper {
    MapleDslDialectSelection<M> headSelect, tailSelect, headShadowSelect, tailShadowSelect;
    MapleDslDialectFunction<M> headFunc, tailFunc;
    Integer skip, limit;

    private final Set<String> selectionColumnSet = new LinkedHashSet<>();
    private final Set<String> selectionAliaSet = new LinkedHashSet<>();
    private final Consumer<MapleDslDialectBase<M>> dialectBaseConsumer;
    private final Wrapper delegateWrapper;

    QueryWrapper(@NotNull Consumer<MapleDslDialectBase<M>> dialectBaseConsumer, @NotNull Wrapper delegateWrapper) {
        this.dialectBaseConsumer = dialectBaseConsumer;
        this.delegateWrapper = delegateWrapper;
    }

    @Override
    public String render(MapleDslConfiguration context) {
        return delegateWrapper.render(context);
    }

    @Override
    public abstract SortWrapper<M> select(String first, String... columns);

    @Override
    public abstract SortWrapper<M> select(boolean condition, String first, String... columns);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);

    @Override
    @SuppressWarnings("unchecked")
    public abstract <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others);

    @Override
    @SuppressWarnings("unchecked")
    public abstract <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others);

    @Override
    public abstract SortWrapper<M> selectAs(String column, String alias);

    @Override
    public abstract SortWrapper<M> selectAs(SerializableFunction<M, ?> column, String alias);

    @Override
    public abstract SortWrapper<M> count(String alias);

    @Override
    public abstract SortWrapper<M> count(String column, String alias);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> count(SerializableFunction<M, R> column, String alias);

    @Override
    public abstract SortWrapper<M> sum(String column, String alias);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> sum(SerializableFunction<M, R> column, String alias);

    @Override
    public abstract SortWrapper<M> avg(String column, String alias);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> avg(SerializableFunction<M, R> column, String alias);

    @Override
    public abstract SortWrapper<M> min(String column, String alias);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> min(SerializableFunction<M, R> column, String alias);

    @Override
    public abstract SortWrapper<M> max(String column, String alias);

    @Override
    public abstract <R extends Serializable> SortWrapper<M> max(SerializableFunction<M, R> column, String alias);

    protected synchronized void next(@NotNull MapleDslDialectFunction<M> next) {
        dialectBaseConsumer.accept(next);

        // e.g. selectAs("name","person_name").sum("age","sum_age")
        // `age` was missing selected, checking from column_set whether contains.
        // e.g. selectAs("name","person_name").count("person_name","cnt_n")
        // `person_name` should check from alias_set whether contains.
        if (next.column() != null && !selectionColumnSet.contains(next.column()) && !selectionAliaSet.contains(next.column())) {
            shadow(new MapleDslDialectSelection<>(next.column()));
        }

        if (headFunc == null) {
            headFunc = next;
            tailFunc = headFunc;
            return;
        }

        tailFunc.next = next;
        tailFunc = tailFunc.next;
    }

    protected synchronized void next(@NotNull MapleDslDialectSelection<M> next) {
        dialectBaseConsumer.accept(next);

        if (!next.isAllPresent()) {
            Collections.addAll(selectionAliaSet, next.aliases());
            Collections.addAll(selectionColumnSet, next.columns());
        }

        if (headSelect == null) {
            headSelect = next;
            tailSelect = headSelect;
            return;
        }

        tailSelect.next = next;
        tailSelect = tailSelect.next;
    }

    protected synchronized void shadow(@NotNull MapleDslDialectSelection<M> shadow) {
        dialectBaseConsumer.accept(shadow);
        Collections.addAll(selectionAliaSet, shadow.aliases());
        Collections.addAll(selectionColumnSet, shadow.columns());

        if (headShadowSelect == null) {
            headShadowSelect = shadow;
            tailShadowSelect = headShadowSelect;
            return;
        }

        tailShadowSelect.next = shadow;
        tailShadowSelect = tailShadowSelect.next;
    }

    public QueryWrapper<M> limit(int limit) {
        return limit(0, limit);
    }

    public QueryWrapper<M> limit(int skip, int limit) {
        this.skip = skip;
        this.limit = limit;
        return this;
    }
}
