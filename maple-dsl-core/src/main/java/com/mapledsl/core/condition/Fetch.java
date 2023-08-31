package com.mapledsl.core.condition;

import com.mapledsl.core.condition.common.P;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author bofa1ex
 * @since 2023/08/28
 * @param <M>
 */
public interface Fetch<M extends Model<?>> extends Wrapper<Fetch<M>> {
    /**
     * It specifies the primary selection in query.
     * <p>
     * [note] return the whole vertex/edge as default.
     *
     * @param col   the primary selection in query.
     * @return the current ref of the fetch condition wrapper.
     */
    Fetch<M> select(String col);
    Fetch<M> select(boolean condition, String col);
    Fetch<M> select(String col1, String col2);
    Fetch<M> select(boolean condition, String col1, String col2);
    Fetch<M> select(String col1, String col2, String col3);
    Fetch<M> select(boolean condition, String col1, String col2, String col3);
    Fetch<M> select(String[] columns);
    Fetch<M> select(boolean condition, String[] columns);

    <R extends Serializable> Fetch<M> select(SerializableFunction<M, R> col);
    <R extends Serializable> Fetch<M> select(boolean condition, SerializableFunction<M, R> col);
    <R extends Serializable> Fetch<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);
    <R extends Serializable> Fetch<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);
    <R extends Serializable> Fetch<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    <R extends Serializable> Fetch<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    <R extends Serializable> Fetch<M> select(SerializableFunction<M, R>[] columns);
    <R extends Serializable> Fetch<M> select(boolean condition, SerializableFunction<M, R>[] columns);

    /**
     * specifies the according to the query column&alias criteria.
     * <p>
     * [note] If the query columns criteria is not specified, then it will return all columns as default.
     *
     * @param column    the query column criteria.
     * @param alias     the query column alias mapping criteria.
     * @return the current ref of the fetch condition wrapper.
     */
    Fetch<M> selectAs(String column, String alias);
    Fetch<M> selectAs(SerializableFunction<M, ?> column, String alias);

    Fetch<M> filter(P<M> predicate);
    Fetch<M> filter(boolean condition, P<M> predicate);
    Fetch<M> filter(P<M> first, P<M> second);
    Fetch<M> filter(boolean condition, P<M> first, P<M> second);
    Fetch<M> filter(P<M> first, P<M> second, P<M> third);
    Fetch<M> filter(boolean condition, P<M> first, P<M> second, P<M> third);

    Fetch<M> filter(Collection<P<M>> predicates);
    Fetch<M> filter(boolean condition, Collection<P<M>> predicates);

    <R extends Serializable> Fetch<M> eq(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> eq(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> eq(String column, R value);
    <R extends Serializable> Fetch<M> eq(boolean condition, String column, R value);

    <R extends Serializable> Fetch<M> ne(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> ne(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> ne(String column, R value);
    <R extends Serializable> Fetch<M> ne(boolean condition, String column, R value);

    <R extends Serializable> Fetch<M> gt(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> gt(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> gt(String column, R value);
    <R extends Serializable> Fetch<M> gt(boolean condition, String column, R value);

    <R extends Serializable> Fetch<M> ge(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> ge(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> ge(String column, R value);
    <R extends Serializable> Fetch<M> ge(boolean condition, String column, R value);

    <R extends Serializable> Fetch<M> lt(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> lt(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> lt(String column, R value);
    <R extends Serializable> Fetch<M> lt(boolean condition, String column, R value);

    <R extends Serializable> Fetch<M> le(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> le(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Fetch<M> le(String column, R value);
    <R extends Serializable> Fetch<M> le(boolean condition, String column, R value);

    <R extends Serializable> Fetch<M> in(SerializableFunction<M, R> column, Collection<R> value);
    <R extends Serializable> Fetch<M> in(boolean condition, SerializableFunction<M, R> column, Collection<R> value);
    <R extends Serializable> Fetch<M> in(String column, Collection<R> value);
    <R extends Serializable> Fetch<M> in(boolean condition, String column, Collection<R> value);

    <R extends String> Fetch<M> contains(SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> contains(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> contains(String column, R value);
    <R extends String> Fetch<M> contains(boolean condition, String column, R value);

    <R extends String> Fetch<M> startsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> startsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> startsWith(String column, R value);
    <R extends String> Fetch<M> startsWith(boolean condition, String column, R value);

    <R extends String> Fetch<M> notStartsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> notStartsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> notStartsWith(String column, R value);
    <R extends String> Fetch<M> notStartsWith(boolean condition, String column, R value);

    <R extends String> Fetch<M> endsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> endsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> endsWith(String column, R value);
    <R extends String> Fetch<M> endsWith(boolean condition, String column, R value);

    <R extends String> Fetch<M> notEndsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> notEndsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Fetch<M> notEndsWith(String column, R value);
    <R extends String> Fetch<M> notEndsWith(boolean condition, String column, R value);

    Fetch<M> isNull(String column);
    Fetch<M> isNull(boolean condition, String column);
    <R extends Serializable> Fetch<M> isNull(SerializableFunction<M, R> column);
    <R extends Serializable> Fetch<M> isNull(boolean condition, SerializableFunction<M, R> column);

    Fetch<M> notNull(String column);
    Fetch<M> notNull(boolean condition, String column);
    <R extends Serializable> Fetch<M> notNull(SerializableFunction<M, R> column);
    <R extends Serializable> Fetch<M> notNull(boolean condition, SerializableFunction<M, R> column);

    /**
     * It specifies the according to the order(asc&desc) column criteria.
     * <p>
     * [note] If the order columns is not include query columns, then it will complete in query columns as default.
     *
     * @param col   the query order column criteria.
     * @return the current ref of the fetch condition wrapper.
     */
    Fetch<M> orderByAsc(String col);
    Fetch<M> orderByAsc(String col1, String col2);
    Fetch<M> orderByAsc(String col1, String col2, String col3);
    Fetch<M> orderByAsc(String[] cols);

    Fetch<M> orderByAsc(SerializableFunction<M, ?> col);
    Fetch<M> orderByAsc(SerializableFunction<M, ?> col1, SerializableFunction<M, ?> col2);
    Fetch<M> orderByAsc(SerializableFunction<M, ?> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    Fetch<M> orderByAsc(SerializableFunction<M, ?>[] cols);

    Fetch<M> orderByDesc(String col);
    Fetch<M> orderByDesc(String col1, String col2);
    Fetch<M> orderByDesc(String col1, String col2, String col3);
    Fetch<M> orderByDesc(String[] cols);

    Fetch<M> orderByDesc(SerializableFunction<M, ?> col);
    Fetch<M> orderByDesc(SerializableFunction<M, ?> col1, SerializableFunction<M, ?> col2);
    Fetch<M> orderByDesc(SerializableFunction<M, ?> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    Fetch<M> orderByDesc(SerializableFunction<M, ?>[] cols);
}
