package com.mapledsl.core.condition;

import com.mapledsl.core.condition.common.P;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author bofa1ex
 * @since 2023/08/28
 * @param <T>
 */
public interface Match<T extends Model<?>> extends Query<T, Match<T>> {
    /**
     * It specifies the primary selection in query.
     * <p>
     * [note] return the whole vertex/edge as default.
     *
     * @param col   the primary selection in query.
     * @return the current ref of the fetch condition wrapper.
     */
    Match<T> select(String col);
    Match<T> select(boolean condition, String col);
    Match<T> select(String col1, String col2);
    Match<T> select(boolean condition, String col1, String col2);
    Match<T> select(String col1, String col2, String col3);
    Match<T> select(boolean condition, String col1, String col2, String col3);
    Match<T> select(String[] columns);
    Match<T> select(boolean condition, String[] columns);

    <R extends Serializable> Match<T> select(SerializableFunction<T, R> col);
    <R extends Serializable> Match<T> select(boolean condition, SerializableFunction<T, R> col);
    <R extends Serializable> Match<T> select(SerializableFunction<T, R> col1, SerializableFunction<T, ?> col2);
    <R extends Serializable> Match<T> select(boolean condition, SerializableFunction<T, R> col1, SerializableFunction<T, ?> col2);
    <R extends Serializable> Match<T> select(SerializableFunction<T, R> col1, SerializableFunction<T, ?> col2, SerializableFunction<T, ?> col3);
    <R extends Serializable> Match<T> select(boolean condition, SerializableFunction<T, R> col1, SerializableFunction<T, ?> col2, SerializableFunction<T, ?> col3);
    <R extends Serializable> Match<T> select(SerializableFunction<T, R>[] columns);
    <R extends Serializable> Match<T> select(boolean condition, SerializableFunction<T, R>[] columns);

    /**
     * specifies the according to the query column&alias criteria.
     * <p>
     * [note] If the query columns criteria is not specified, then it will return all columns as default.
     *
     * @param column    the query column criteria.
     * @param alias     the query column alias mapping criteria.
     * @return the current ref of the fetch condition wrapper.
     */
    Match<T> selectAs(String column, String alias);
    Match<T> selectAs(SerializableFunction<T, ?> column, String alias);

    Match<T> filter(P<T> predicate);
    Match<T> filter(boolean condition, P<T> predicate);
    Match<T> filter(P<T> first, P<T> second);
    Match<T> filter(boolean condition, P<T> first, P<T> second);
    Match<T> filter(P<T> first, P<T> second, P<T> third);
    Match<T> filter(boolean condition, P<T> first, P<T> second, P<T> third);

    Match<T> filter(Collection<P<T>> predicates);
    Match<T> filter(boolean condition, Collection<P<T>> predicates);

    <R extends Serializable> Match<T> eq(SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> eq(boolean condition, SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> eq(String column, R value);
    <R extends Serializable> Match<T> eq(boolean condition, String column, R value);

    <R extends Serializable> Match<T> ne(SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> ne(boolean condition, SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> ne(String column, R value);
    <R extends Serializable> Match<T> ne(boolean condition, String column, R value);

    <R extends Serializable> Match<T> gt(SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> gt(boolean condition, SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> gt(String column, R value);
    <R extends Serializable> Match<T> gt(boolean condition, String column, R value);

    <R extends Serializable> Match<T> ge(SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> ge(boolean condition, SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> ge(String column, R value);
    <R extends Serializable> Match<T> ge(boolean condition, String column, R value);

    <R extends Serializable> Match<T> lt(SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> lt(boolean condition, SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> lt(String column, R value);
    <R extends Serializable> Match<T> lt(boolean condition, String column, R value);

    <R extends Serializable> Match<T> le(SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> le(boolean condition, SerializableFunction<T, R> column, R value);
    <R extends Serializable> Match<T> le(String column, R value);
    <R extends Serializable> Match<T> le(boolean condition, String column, R value);

    <R extends Serializable> Match<T> in(SerializableFunction<T, R> column, Collection<R> value);
    <R extends Serializable> Match<T> in(boolean condition, SerializableFunction<T, R> column, Collection<R> value);
    <R extends Serializable> Match<T> in(String column, Collection<R> value);
    <R extends Serializable> Match<T> in(boolean condition, String column, Collection<R> value);

    Match<T> isNull(String column);
    Match<T> isNull(boolean condition, String column);
    <R extends Serializable> Match<T> isNull(SerializableFunction<T, R> column);
    <R extends Serializable> Match<T> isNull(boolean condition, SerializableFunction<T, R> column);

    Match<T> notNull(String column);
    Match<T> notNull(boolean condition, String column);
    <R extends Serializable> Match<T> notNull(SerializableFunction<T, R> column);
    <R extends Serializable> Match<T> notNull(boolean condition, SerializableFunction<T, R> column);

    <V extends Model.V> Traversal<V> traverse();
    <E extends Model.E> Traversal<E> traverseEdge();
}
