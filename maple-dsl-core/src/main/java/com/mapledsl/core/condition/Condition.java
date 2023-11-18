package com.mapledsl.core.condition;

import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

public interface Condition<M extends Model<?>, Children> {
    interface Unary<M extends Model<?>> extends Condition<M, Unary<M>> {}

    Children or();
    Children xor();

    Children or(Consumer<Condition.Unary<M>> operator);
    Children xor(Consumer<Condition.Unary<M>> operator);
    Children and(Consumer<Condition.Unary<M>> operator);

    <R extends Serializable> Children eq(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Children eq(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Children eq(String column, R value);
    <R extends Serializable> Children eq(boolean condition, String column, R value);

    <R extends Serializable> Children ne(SerializableFunction<M, R> column, R value);
    <R extends Serializable> Children ne(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Serializable> Children ne(String column, R value);
    <R extends Serializable> Children ne(boolean condition, String column, R value);

    <R extends Number> Children gt(SerializableFunction<M, R> column, R value);
    <R extends Number> Children gt(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Number> Children gt(String column, R value);
    <R extends Number> Children gt(boolean condition, String column, R value);

    <R extends Number> Children ge(SerializableFunction<M, R> column, R value);
    <R extends Number> Children ge(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Number> Children ge(String column, R value);
    <R extends Number> Children ge(boolean condition, String column, R value);

    <R extends Number> Children lt(SerializableFunction<M, R> column, R value);
    <R extends Number> Children lt(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Number> Children lt(String column, R value);
    <R extends Number> Children lt(boolean condition, String column, R value);

    <R extends Number> Children le(SerializableFunction<M, R> column, R value);
    <R extends Number> Children le(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends Number> Children le(String column, R value);
    <R extends Number> Children le(boolean condition, String column, R value);

    <R extends Serializable> Children in(SerializableFunction<M, R> column, Collection<R> value);
    <R extends Serializable> Children in(boolean condition, SerializableFunction<M, R> column, Collection<R> value);
    <R extends Serializable> Children in(String column, Collection<R> value);
    <R extends Serializable> Children in(boolean condition, String column, Collection<R> value);

    <R extends String> Children contains(SerializableFunction<M, R> column, R value);
    <R extends String> Children contains(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Children contains(String column, R value);
    <R extends String> Children contains(boolean condition, String column, R value);

    <R extends String> Children startsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Children startsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Children startsWith(String column, R value);
    <R extends String> Children startsWith(boolean condition, String column, R value);

    <R extends String> Children notStartsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Children notStartsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Children notStartsWith(String column, R value);
    <R extends String> Children notStartsWith(boolean condition, String column, R value);

    <R extends String> Children endsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Children endsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Children endsWith(String column, R value);
    <R extends String> Children endsWith(boolean condition, String column, R value);

    <R extends String> Children notEndsWith(SerializableFunction<M, R> column, R value);
    <R extends String> Children notEndsWith(boolean condition, SerializableFunction<M, R> column, R value);
    <R extends String> Children notEndsWith(String column, R value);
    <R extends String> Children notEndsWith(boolean condition, String column, R value);

    Children isNull(String column);
    Children isNull(boolean condition, String column);
    <R extends Serializable> Children isNull(SerializableFunction<M, R> column);
    <R extends Serializable> Children isNull(boolean condition, SerializableFunction<M, R> column);

    Children notNull(String column);
    Children notNull(boolean condition, String column);
    <R extends Serializable> Children notNull(SerializableFunction<M, R> column);
    <R extends Serializable> Children notNull(boolean condition, SerializableFunction<M, R> column);
}
