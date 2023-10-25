package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.condition.Condition;
import com.mapledsl.core.condition.Query;
import com.mapledsl.core.condition.Sort;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

abstract class QueryDuplexWrapper<M extends Model<?>, Children> implements Sort<M>, Condition<M, Children> {
    protected final QueryWrapper<M> selection;
    protected final ConditionWrapper<M> predicate;

    protected QueryDuplexWrapper(QueryWrapper<M> selection, ConditionWrapper<M> predicate) {
        this.selection = selection;
        this.predicate = predicate;
    }

    protected abstract Children instance();

    @Override
    public Children or() {
        this.predicate.or();
        return instance();
    }

    @Override
    public Children xor() {
        this.predicate.xor();
        return instance();
    }

    @Override
    public Children or(Consumer<Unary<M>> operator) {
        this.predicate.or(operator);
        return instance();
    }

    @Override
    public Children xor(Consumer<Unary<M>> operator) {
        this.predicate.xor(operator);
        return instance();
    }

    @Override
    public Children and(Consumer<Unary<M>> operator) {
        this.predicate.and(operator);
        return instance();
    }

    @Override
    public <R extends Serializable> Children eq(SerializableFunction<M, R> column, R value) {
        this.predicate.eq(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children eq(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return eq(column, value);
    }

    @Override
    public <R extends Serializable> Children eq(String column, R value) {
        this.predicate.eq(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children eq(boolean condition, String column, R value) {
        if (!condition) return instance();
        return eq(column, value);
    }

    @Override
    public <R extends Serializable> Children ne(SerializableFunction<M, R> column, R value) {
        this.predicate.ne(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children ne(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return ne(column, value);
    }

    @Override
    public <R extends Serializable> Children ne(String column, R value) {
        this.predicate.ne(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children ne(boolean condition, String column, R value) {
        if (!condition) return instance();
        return ne(column, value);
    }

    @Override
    public <R extends Serializable> Children gt(SerializableFunction<M, R> column, R value) {
        this.predicate.gt(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children gt(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return gt(column, value);
    }

    @Override
    public <R extends Serializable> Children gt(String column, R value) {
        this.predicate.gt(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children gt(boolean condition, String column, R value) {
        if (!condition) return instance();
        return gt(column, value);
    }

    @Override
    public <R extends Serializable> Children ge(SerializableFunction<M, R> column, R value) {
        this.predicate.ge(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children ge(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return ge(column, value);
    }

    @Override
    public <R extends Serializable> Children ge(String column, R value) {
        this.predicate.ge(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children ge(boolean condition, String column, R value) {
        if (!condition) return instance();
        return ge(column, value);
    }

    @Override
    public <R extends Serializable> Children lt(SerializableFunction<M, R> column, R value) {
        this.predicate.lt(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children lt(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return lt(column, value);
    }

    @Override
    public <R extends Serializable> Children lt(String column, R value) {
        this.predicate.lt(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children lt(boolean condition, String column, R value) {
        if (!condition) return instance();
        return lt(column, value);
    }

    @Override
    public <R extends Serializable> Children le(SerializableFunction<M, R> column, R value) {
        this.predicate.le(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children le(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return le(column,value);
    }

    @Override
    public <R extends Serializable> Children le(String column, R value) {
        this.predicate.le(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children le(boolean condition, String column, R value) {
        if (!condition) return instance();
        return le(column, value);
    }

    @Override
    public <R extends Serializable> Children in(SerializableFunction<M, R> column, Collection<R> value) {
        this.predicate.in(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children in(boolean condition, SerializableFunction<M, R> column, Collection<R> value) {
        if (!condition) return instance();
        return in(column, value);
    }

    @Override
    public <R extends Serializable> Children in(String column, Collection<R> value) {
        this.predicate.in(column, value);
        return instance();
    }

    @Override
    public <R extends Serializable> Children in(boolean condition, String column, Collection<R> value) {
        if (!condition) return instance();
        return in(column, value);
    }

    @Override
    public <R extends String> Children contains(SerializableFunction<M, R> column, R value) {
        this.predicate.contains(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children contains(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return contains(column, value);
    }

    @Override
    public <R extends String> Children contains(String column, R value) {
        this.predicate.contains(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children contains(boolean condition, String column, R value) {
        if (!condition) return instance();
        return contains(column, value);
    }

    @Override
    public <R extends String> Children startsWith(SerializableFunction<M, R> column, R value) {
        this.predicate.startsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children startsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return startsWith(column, value);
    }

    @Override
    public <R extends String> Children startsWith(String column, R value) {
        this.predicate.startsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children startsWith(boolean condition, String column, R value) {
        if (!condition) return instance();
        return startsWith(column, value);
    }

    @Override
    public <R extends String> Children notStartsWith(SerializableFunction<M, R> column, R value) {
        this.predicate.notStartsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children notStartsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return notStartsWith(column, value);
    }

    @Override
    public <R extends String> Children notStartsWith(String column, R value) {
        this.predicate.notStartsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children notStartsWith(boolean condition, String column, R value) {
        if (!condition) return instance();
        return notStartsWith(column, value);
    }

    @Override
    public <R extends String> Children endsWith(SerializableFunction<M, R> column, R value) {
        this.predicate.endsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children endsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return endsWith(column, value);
    }

    @Override
    public <R extends String> Children endsWith(String column, R value) {
        this.predicate.endsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children endsWith(boolean condition, String column, R value) {
        if (!condition) return instance();
        return endsWith(column, value);
    }

    @Override
    public <R extends String> Children notEndsWith(SerializableFunction<M, R> column, R value) {
        this.predicate.notEndsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children notEndsWith(boolean condition, SerializableFunction<M, R> column, R value) {
        if (!condition) return instance();
        return notEndsWith(column, value);
    }

    @Override
    public <R extends String> Children notEndsWith(String column, R value) {
        this.predicate.notEndsWith(column, value);
        return instance();
    }

    @Override
    public <R extends String> Children notEndsWith(boolean condition, String column, R value) {
        if (!condition) return instance();
        return notEndsWith(column, value);
    }

    @Override
    public Children isNull(String column) {
        this.predicate.isNull(column);
        return instance();
    }

    @Override
    public Children isNull(boolean condition, String column) {
        if (!condition) return instance();
        return isNull(column);
    }

    @Override
    public <R extends Serializable> Children isNull(SerializableFunction<M, R> column) {
        this.predicate.isNull(column);
        return instance();
    }

    @Override
    public <R extends Serializable> Children isNull(boolean condition, SerializableFunction<M, R> column) {
        if (!condition) return instance();
        return isNull(column);
    }

    @Override
    public Children notNull(String column) {
        this.predicate.notNull(column);
        return instance();
    }

    @Override
    public Children notNull(boolean condition, String column) {
        if (!condition) return instance();
        return notNull(column);
    }

    @Override
    public <R extends Serializable> Children notNull(SerializableFunction<M, R> column) {
        this.predicate.notNull(column);
        return instance();
    }

    @Override
    public <R extends Serializable> Children notNull(boolean condition, SerializableFunction<M, R> column) {
        if (!condition) return instance();
        return notNull(column);
    }

    @Override
    public Sort<M> select(String first, String... columns) {
        this.selection.select(first, columns);
        return this;
    }

    @Override
    public Sort<M> select(boolean condition, String first, String... columns) {
        if (!condition) return this;
        return select(first, columns);
    }

    @Override
    public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col) {
        this.selection.select(col);
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col) {
        if (!condition) return this;
        return select(col);
    }

    @Override
    public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        this.selection.select(col1, col2);
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2) {
        if (!condition) return this;
        return select(col1, col2);
    }

    @Override
    public <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3) {
        this.selection.select(col1, col2, col3);
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
        this.selection.select(col1, col2, col3, others);
        return this;
    }

    @SafeVarargs
    @Override
    public final <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others) {
        if (!condition) return this;
        return select(col1, col2, col3, others);
    }

    @Override
    public Sort<M> selectAs(String column, String alias) {
        this.selection.selectAs(column, alias);
        return this;
    }

    @Override
    public Sort<M> selectAs(SerializableFunction<M, ?> column, String alias) {
        this.selection.selectAs(column, alias);
        return this;
    }

    @Override
    public Query<M, Sort<M>> ascending() {
        selection.ascending();
        return this;
    }

    @Override
    public Query<M, Sort<M>> descending() {
        selection.descending();
        return this;
    }

    @Override
    public Sort<M> count(String alias) {
        requireNonNull(alias);
        this.selection.count(alias);
        return this;
    }

    @Override
    public Sort<M> count(String column, String alias) {
        requireNonNull(column);
        requireNonNull(alias);
        this.selection.count(column, alias);
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
        this.selection.sum(column, alias);
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
        this.selection.avg(column, alias);
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
        this.selection.min(column, alias);
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
        this.selection.max(column, alias);
        return this;
    }

    @Override
    public <R extends Serializable> Sort<M> max(SerializableFunction<M, R> column, String alias) {
        requireNonNull(column);
        return max(column.asText(), alias);
    }
}
