package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.condition.Query;
import com.mapledsl.core.condition.Sort;
import com.mapledsl.core.condition.common.Func;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public final class QueryWrapper<M extends Model<?>> implements Sort<M> {
    MapleDslDialectSelection<M> headSelect, tailSelect;
    MapleDslDialectFunction headFunc, tailFunc;

    final Set<String> orderAscSet = new LinkedHashSet<>();
    final Set<String> orderDescSet = new LinkedHashSet<>();
    final Set<String> selectionColumnSet = new HashSet<>();

    private String[] orderCandidates;
    private final Consumer<MapleDslDialectBase<M>> selectionDecorator;

    QueryWrapper(Consumer<MapleDslDialectBase<M>> selectionDecorator) {
        this.selectionDecorator = selectionDecorator;
    }

    private synchronized void next(@NotNull MapleDslDialectFunction next) {
        orderCandidates = new String[] { next.alias() };

        if (next.column() != null && selectionColumnSet.contains(next.column())) {
            next(new MapleDslDialectSelection<>(next.column()));
        }

        if (headFunc == null) {
            headFunc = next;
            tailFunc = headFunc;
            return;
        }

        tailFunc.next = next;
        tailFunc = tailFunc.next;
    }

    private synchronized void next(@NotNull MapleDslDialectSelection<M> next) {
        selectionDecorator.accept(next);
        orderCandidates = next.aliases();
        Collections.addAll(selectionColumnSet, next.columns());

        if (headSelect == null) {
            headSelect = next;
            tailSelect = headSelect;
            return;
        }

        tailSelect.next = next;
        tailSelect = tailSelect.next;
    }

    @Override
    public Sort<M> select(String first, String... columns) {
        requireNonNull(first);
        requireNonNull(columns);

        next(new MapleDslDialectSelection<>(first));

        if (columns.length == 0) return this;
        next(new MapleDslDialectSelection<>(columns));
        return this;
    }

    @Override
    public Sort<M> select(boolean condition, String first, String... columns) {
        if (!condition) return this;
        return select(first, columns);
    }

    @Override
    public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col) {
        requireNonNull(col);
        next(new MapleDslDialectSelection<>(col.asText()));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col) {
        if (!condition) return this;
        return select(col);
    }

    @Override
    public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        requireNonNull(col1);
        requireNonNull(col2);
        next(new MapleDslDialectSelection<>(col1.asText(), col2.asText()));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        if(!condition) return this;
        return select(col1, col2);
    }

    @Override
    public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
        requireNonNull(col1);
        requireNonNull(col2);
        requireNonNull(col3);
        next(new MapleDslDialectSelection<>(new String[] { col1.asText(), col2.asText(), col3.asText() }));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
        if (!condition) return this;
        return select(col1, col2, col3);
    }

    @SafeVarargs
    @Override
    public final <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others) {
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
    public final <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others) {
        if (!condition) return this;
        return select(col1, col2, col3, others);
    }

    @Override
    public Sort<M> selectAs(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        next(new MapleDslDialectSelection<>(column, alias));
        return this;
    }

    @Override
    public Sort<M> selectAs(SerializableFunction<M, ?> column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        next(new MapleDslDialectSelection<>(column.asText(), alias));
        return this;
    }

    @Override
    public Sort<M> count(String alias) {
        requireNonNull(alias);
        next(new MapleDslDialectFunction(Func.CNT, alias));
        return this;
    }

    @Override
    public Sort<M> count(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);

        // COUNT('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction(column, Func.CNT, alias));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> count(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return count(column.asText(), alias);
    }

    @Override
    public Sort<M> sum(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // SUM('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction(column, Func.SUM, alias));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> sum(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return sum(column.asText(), alias);
    }

    @Override
    public Sort<M> avg(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // AVG('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction(column, Func.AVG, alias));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> avg(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return avg(column.asText(), alias);
    }

    @Override
    public Sort<M> min(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // MIN('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction(column, Func.MIN, alias));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> min(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return min(column.asText(), alias);
    }

    @Override
    public Sort<M> max(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        // MAX('age') missing select `age` as premiss.
        next(new MapleDslDialectFunction(column, Func.MAX, alias));
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> max(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return max(column.asText(), alias);
    }

    @Override
    public synchronized Query<M, Sort<M>> ascending() {
        if (orderCandidates == null || orderCandidates.length == 0) return this;
        Collections.addAll(orderAscSet, orderCandidates);
        orderCandidates = null;
        return this;
    }

    @Override
    public synchronized Query<M, Sort<M>> descending() {
        if (orderCandidates == null || orderCandidates.length == 0) return this;
        Collections.addAll(orderDescSet, orderCandidates);
        orderCandidates = null;
        return this;
    }
}