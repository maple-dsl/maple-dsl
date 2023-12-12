package com.mapledsl.core.condition;

import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.io.Serializable;

/**
 * The Query interface represents a query for a model object.
 *
 * @param <M> the type of the model object
 */
public interface Query<M extends Model<?>> {
    /**
     * The Sort interface represents a sorting criteria for a query on a model object.
     *
     * @param <M> the type of the model object
     */
    interface Sort<M extends Model<?>> extends Query<M> {
        Query<M> ascending();
        Query<M> descending();
    }

    /**
     * It specifies the primary selection in query.
     * <p>
     * [note] return the whole vertex/edge as default.
     *
     * @param first the first selection in query.
     * @param columns the primary selection in query.
     * @return the current ref of the fetch condition wrapper.
     */
    Sort<M> select(String first, String... columns);
    Sort<M> select(boolean condition, String first, String... columns);

    <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col);
    <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col);
    <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);
    <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);
    <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    @SuppressWarnings("unchecked")
    <R extends Serializable> Sort<M> select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others);
    @SuppressWarnings("unchecked")
    <R extends Serializable> Sort<M> select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others);

    /**
     * specifies the according to the query column and alias criteria.
     * <p>
     * [note] If the query columns criteria is not specified, then it will return all columns as default.
     *
     * @param column column
     * @param alias alias
     * @return the current ref of the fetch condition wrapper.
     */
    Sort<M> selectAs(String column, String alias);
    Sort<M> selectAs(SerializableFunction<M, ?> column, String alias);

    Sort<M> count(String alias);
    Sort<M> count(String column, String alias);
    <R extends Serializable> Sort<M> count(SerializableFunction<M, R> column, String alias);
    Sort<M> sum(String column, String alias);
    <R extends Serializable> Sort<M> sum(SerializableFunction<M, R> column, String alias);
    Sort<M> avg(String column, String alias);
    <R extends Serializable> Sort<M> avg(SerializableFunction<M, R> column, String alias);
    Sort<M> min(String column, String alias);
    <R extends Serializable> Sort<M> min(SerializableFunction<M, R> column, String alias);
    Sort<M> max(String column, String alias);
    <R extends Serializable> Sort<M> max(SerializableFunction<M, R> column, String alias);
}
