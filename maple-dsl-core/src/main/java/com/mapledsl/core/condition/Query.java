package com.mapledsl.core.condition;

import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.io.Serializable;

public interface Query<M extends Model<?>, Children> {
    /**
     * It specifies the primary selection in query.
     * <p>
     * [note] return the whole vertex/edge as default.
     *
     * @param columns the primary selection in query.
     * @return the current ref of the fetch condition wrapper.
     */
    Children select(String first, String... columns);
    Children select(boolean condition, String first, String... columns);

    <R extends Serializable> Children select(SerializableFunction<M, R> col);
    <R extends Serializable> Children select(boolean condition, SerializableFunction<M, R> col);
    <R extends Serializable> Children select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);
    <R extends Serializable> Children select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2);
    <R extends Serializable> Children select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    <R extends Serializable> Children select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3);
    @SuppressWarnings("unchecked")
    <R extends Serializable> Children select(SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others);
    @SuppressWarnings("unchecked")
    <R extends Serializable> Children select(boolean condition, SerializableFunction<M, R> col1, SerializableFunction<M, ?> col2, SerializableFunction<M, ?> col3, SerializableFunction<M, ?>... others);

    /**
     * specifies the according to the query column&alias criteria.
     * <p>
     * [note] If the query columns criteria is not specified, then it will return all columns as default.
     *
     * @return the current ref of the fetch condition wrapper.
     */
    Children selectAs(String column, String alias);
    Children selectAs(SerializableFunction<M, ?> column, String alias);

    Children count(String alias);
    Children count(String column, String alias);
    <R extends Serializable> Children count(SerializableFunction<M, R> column, String alias);
    Children sum(String column, String alias);
    <R extends Serializable> Children sum(SerializableFunction<M, R> column, String alias);
    Children avg(String column, String alias);
    <R extends Serializable> Children avg(SerializableFunction<M, R> column, String alias);
    Children min(String column, String alias);
    <R extends Serializable> Children min(SerializableFunction<M, R> column, String alias);
    Children max(String column, String alias);
    <R extends Serializable> Children max(SerializableFunction<M, R> column, String alias);
}
