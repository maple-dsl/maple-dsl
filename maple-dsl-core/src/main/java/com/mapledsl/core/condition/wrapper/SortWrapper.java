package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.condition.Query;
import com.mapledsl.core.condition.Wrapper;
import com.mapledsl.core.condition.common.Func;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class SortWrapper<M extends Model<?>> extends QueryWrapper<M> implements Query.Sort<M> {
    protected final Set<String> orderAscSet = new LinkedHashSet<>();
    protected final Set<String> orderDescSet = new LinkedHashSet<>();
    private final Set<String> orderCandidateSet = new LinkedHashSet<>();

    SortWrapper(@NotNull Consumer<MapleDslDialectBase<M>> mapleDslDialectBaseConsumer, @NotNull Wrapper delegateWrapper) {
        super(mapleDslDialectBaseConsumer, delegateWrapper);
    }

    @Override
    protected synchronized void next(@NotNull MapleDslDialectFunction<M> next) {
        super.next(next);
        orderCandidateSet.add(next.alias());
    }

    @Override
    protected synchronized void next(@NotNull MapleDslDialectSelection<M> next) {
        super.next(next);
        if (next.isAllPresent()) return;
        Collections.addAll(orderCandidateSet, next.aliases());
    }

    @Override
    public SortWrapper<M> select(String first, String... columns) {
        requireNonNull(first);
        requireNonNull(columns);

        next(new MapleDslDialectSelection<>(first));

        if (columns.length == 0) return this;
        next(new MapleDslDialectSelection<>(columns));
        return this;
    }

    @Override
    public SortWrapper<M> select(boolean condition, String first, String... columns) {
        if (!condition) return this;
        return select(first, columns);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col) {
        requireNonNull(col);
        next(new MapleDslDialectSelection<>(col.asText()));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col) {
        if (!condition) return this;
        return select(col);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        requireNonNull(col1);
        requireNonNull(col2);
        next(new MapleDslDialectSelection<>(new String[] { col1.asText(), col2.asText() }));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        if(!condition) return this;
        return select(col1, col2);
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
        requireNonNull(col1);
        requireNonNull(col2);
        requireNonNull(col3);
        next(new MapleDslDialectSelection<>(new String[] { col1.asText(), col2.asText(), col3.asText() }));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
        if (!condition) return this;
        return select(col1, col2, col3);
    }

    @SafeVarargs
    @Override
    public final <R extends Serializable> SortWrapper<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others) {
        requireNonNull(col1);
        requireNonNull(col2);
        requireNonNull(col3);
        requireNonNull(others);

        next(new MapleDslDialectSelection<>(new String[]{ col1.asText(), col2.asText(), col3.asText() }));
        if (others.length == 0) return this;
        final String[] columns = new String[others.length];
        Arrays.setAll(columns, i -> others[i].asText());
        next(new MapleDslDialectSelection<>(columns));
        return this;
    }

    @Override
    @SafeVarargs
    public final <R extends Serializable> SortWrapper<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others) {
        if (!condition) return this;
        return select(col1, col2, col3, others);
    }

    @Override
    public SortWrapper<M> selectAs(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        next(new MapleDslDialectSelection<>(column, alias));
        return this;
    }

    @Override
    public SortWrapper<M> selectAs(SerializableFunction<M, ?> column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        next(new MapleDslDialectSelection<>(column.asText(), alias));
        return this;
    }

    @Override
    public SortWrapper<M> count(String alias) {
        requireNonNull(alias);
        next(new MapleDslDialectFunction<>(Func.CNT, alias));
        return this;
    }

    @Override
    public SortWrapper<M> count(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);

        // COUNT('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction<>(column, Func.CNT, alias));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> count(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return count(column.asText(), alias);
    }

    @Override
    public SortWrapper<M> sum(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // SUM('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction<>(column, Func.SUM, alias));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> sum(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return sum(column.asText(), alias);
    }

    @Override
    public SortWrapper<M> avg(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // AVG('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction<>(column, Func.AVG, alias));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> avg(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return avg(column.asText(), alias);
    }

    @Override
    public SortWrapper<M> min(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // MIN('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction<>(column, Func.MIN, alias));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> min(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return min(column.asText(), alias);
    }

    @Override
    public SortWrapper<M> max(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // MAX('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction<>(column, Func.MAX, alias));
        return this;
    }

    @Override
    public <R extends Serializable> SortWrapper<M> max(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return max(column.asText(), alias);
    }

    @Override
    public synchronized QueryWrapper<M> ascending() {
        if (orderCandidateSet.isEmpty()) return this;
        orderAscSet.addAll(orderCandidateSet);
        orderCandidateSet.clear();
        return this;
    }

    @Override
    public synchronized QueryWrapper<M> descending() {
        if (orderCandidateSet.isEmpty()) return this;
        orderDescSet.addAll(orderCandidateSet);
        orderCandidateSet.clear();
        return this;
    }
}