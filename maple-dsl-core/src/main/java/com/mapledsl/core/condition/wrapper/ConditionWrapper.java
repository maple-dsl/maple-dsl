package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Condition;
import com.mapledsl.core.condition.Wrapper;
import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class ConditionWrapper<M extends Model<?>> implements Condition.Unary<M>, Wrapper {
    MapleDslDialectPredicate<M> head, tail;
    private final Consumer<MapleDslDialectBase<M>> predicateDecorator;
    private final Wrapper delegateWrapper;
    private final AtomicReference<OP> connection = new AtomicReference<>(OP.AND);

    ConditionWrapper(@NotNull Consumer<MapleDslDialectBase<M>> predicateDecorator, @NotNull Wrapper delegateWrapper) {
        this.predicateDecorator = predicateDecorator;
        this.delegateWrapper = delegateWrapper;
    }

    private synchronized void next(@NotNull MapleDslDialectPredicate<M> next) {
        predicateDecorator.accept(next);
        if (head == null) {
            head = next;
            tail = head;
            return;
        }
        tail.connection = connection.getAndSet(OP.AND);
        tail.next = next;
        tail = tail.next;
    }

    @Override
    public ConditionWrapper<M> or() {
        connection.set(OP.OR);
        return this;
    }

    @Override
    public ConditionWrapper<M> xor() {
        connection.set(OP.XOR);
        return this;
    }

    @Override
    public ConditionWrapper<M> or(Consumer<Unary<M>> operator) {
        requireNonNull(operator);
        connection.set(OP.OR);
        tail.hasPrefix = true;
        operator.accept(this);
        tail.hasSuffix = true;
        return this;
    }

    @Override
    public ConditionWrapper<M> xor(Consumer<Unary<M>> operator) {
        requireNonNull(operator);
        connection.set(OP.XOR);
        tail.hasPrefix = true;
        operator.accept(this);
        tail.hasSuffix = true;
        return this;
    }

    @Override
    public ConditionWrapper<M> and(Consumer<Unary<M>> operator) {
        requireNonNull(operator);
        connection.set(OP.AND);
        tail.hasPrefix = true;
        operator.accept(this);
        tail.hasSuffix = true;
        return this;
    }

    @Override
    public ConditionWrapper<M> eq(SerializableFunction<M, Serializable> column, Serializable value) {
        requireNonNull(column);
        return eq(column.asText(), value);
    }

    @Override
    public ConditionWrapper<M> eq(boolean condition, SerializableFunction<M, Serializable> column, Serializable value) {
        if (!condition) return this;
        return eq(column, value);
    }

    @Override
    public ConditionWrapper<M> eq(boolean condition, String column, Serializable value) {
        if (!condition) return this;
        return eq(column, value);
    }

    @Override
    public ConditionWrapper<M> eq(String column, Serializable value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.EQ, value));
        return this;
    }

    @Override
    public ConditionWrapper<M> ne(SerializableFunction<M, Serializable> column, Serializable value) {
        requireNonNull(column);
        return ne(column.asText(), value);
    }

    @Override
    public ConditionWrapper<M> ne(boolean condition, SerializableFunction<M, Serializable> column, Serializable value) {
        if (!condition) return this;
        return ne(column, value);
    }

    @Override
    public ConditionWrapper<M> ne(String column, Serializable value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.NE, value));
        return this;
    }

    @Override
    public ConditionWrapper<M> ne(boolean condition, String column, Serializable value) {
        if (!condition) return this;
        return ne(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> gt(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return gt(column.asText(), value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> gt(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return gt(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> gt(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.GT, value));
        return this;
    }

    @Override
    public <R extends Number> ConditionWrapper<M> gt(boolean condition, String column, R value) {
        if (!condition) return this;
        return gt(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> ge(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return ge(column.asText(), value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> ge(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return ge(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> ge(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.GE, value));
        return this;
    }

    @Override
    public <R extends Number> ConditionWrapper<M> ge(boolean condition, String column, R value) {
        if (!condition) return this;
        return ge(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> lt(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return lt(column.asText(), value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> lt(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return lt(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> lt(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.LT, value));
        return this;
    }

    @Override
    public <R extends Number> ConditionWrapper<M> lt(boolean condition, String column, R value) {
        if (!condition) return this;
        return lt(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> le(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return le(column.asText(), value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> le(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return le(column, value);
    }

    @Override
    public <R extends Number> ConditionWrapper<M> le(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.LE, value));
        return this;
    }

    @Override
    public <R extends Number> ConditionWrapper<M> le(boolean condition, String column, R value) {
        if (!condition) return this;
        return le(column, value);
    }

    @Override
    public ConditionWrapper<M> in(SerializableFunction<M, Serializable> column, Collection<Serializable> value) {
        requireNonNull(column);
        return in(column.asText(), value);
    }

    @Override
    public ConditionWrapper<M> in(boolean condition, SerializableFunction<M, Serializable> column, Collection<Serializable> value) {
        if (!condition) return this;
        return in(column, value);
    }

    @Override
    public ConditionWrapper<M> in(String column, Collection<Serializable> value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.IN, value));
        return this;
    }

    @Override
    public ConditionWrapper<M> in(boolean condition, String column, Collection<Serializable> value) {
        if (!condition) return this;
        return in(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> contains(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return contains(column.asText(), value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> contains(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return contains(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> contains(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.CONTAINS, value));
        return this;
    }

    @Override
    public <R extends String> ConditionWrapper<M> contains(boolean condition, String column, R value) {
        if (!condition) return this;
        return contains(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> startsWith(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return startsWith(column.asText(), value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> startsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return startsWith(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> startsWith(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.STARTS_WITH, value));
        return this;
    }

    @Override
    public <R extends String> ConditionWrapper<M> startsWith(boolean condition, String column, R value) {
        if (!condition) return this;
        return startsWith(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> notStartsWith(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return notStartsWith(column.asText(), value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> notStartsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return notStartsWith(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> notStartsWith(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.NOT_STARTS_WITH, value));
        return this;
    }

    @Override
    public <R extends String> ConditionWrapper<M> notStartsWith(boolean condition, String column, R value) {
        if (!condition) return this;
        return notStartsWith(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> endsWith(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return endsWith(column.asText(), value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> endsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return endsWith(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> endsWith(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.ENDS_WITH, value));
        return this;
    }

    @Override
    public <R extends String> ConditionWrapper<M> endsWith(boolean condition, String column, R value) {
        if (!condition) return this;
        return endsWith(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> notEndsWith(SerializableFunction<M, R> column, R value) {
        requireNonNull(column);
        return notEndsWith(column.asText(), value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> notEndsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return this;
        return notEndsWith(column, value);
    }

    @Override
    public <R extends String> ConditionWrapper<M> notEndsWith(String column, R value) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.NOT_ENDS_WITH, value));
        return this;
    }

    @Override
    public <R extends String> ConditionWrapper<M> notEndsWith(boolean condition, String column, R value) {
        if (!condition) return this;
        return notEndsWith(column, value);
    }

    @Override
    public ConditionWrapper<M> isNull(String column) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.ISNULL));
        return this;
    }

    @Override
    public ConditionWrapper<M> isNull(boolean condition, String column) {
        if (!condition) return this;
        return isNull(column);
    }

    @Override
    public ConditionWrapper<M> isNull(SerializableFunction<M, ?> column) {
        requireNonNull(column);
        return isNull(column.asText());
    }

    @Override
    public ConditionWrapper<M> isNull(boolean condition, SerializableFunction<M, ?> column) {
        if (!condition) return this;
        return isNull(column);
    }

    @Override
    public ConditionWrapper<M> notNull(String column) {
        requireNonNull(column);
        next(new MapleDslDialectPredicate<>(column, OP.NOT_NULL));
        return this;
    }

    @Override
    public ConditionWrapper<M> notNull(boolean condition, String column) {
        if (!condition) return this;
        return notNull(column);
    }

    @Override
    public ConditionWrapper<M> notNull(SerializableFunction<M, ?> column) {
        requireNonNull(column);
        return notNull(column.asText());
    }

    @Override
    public ConditionWrapper<M> notNull(boolean condition, SerializableFunction<M, ?> column) {
        if (!condition) return this;
        return notNull(column);
    }

    @Override
    public String render(MapleDslConfiguration context) {
        return delegateWrapper.render(context);
    }
}
