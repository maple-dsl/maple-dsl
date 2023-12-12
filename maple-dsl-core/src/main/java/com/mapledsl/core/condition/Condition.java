package com.mapledsl.core.condition;

import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * The Condition interface represents a condition that can be applied to a model.
 *
 * @param <M> the type of the model
 * @param <Children> the type of the child conditions
 */
public interface Condition<M extends Model<?>, Children> {
    /**
     * The `Unary` interface represents a condition that operates on a single `Model` object.
     * It extends the `Condition` interface and specifies that the concrete implementation should
     * provide methods to chain multiple conditions together.
     *
     * @param <M> The type of the `Model` object on which the condition operates.
     */
    interface Unary<M extends Model<?>> extends Condition<M, Unary<M>> {}

    Children or();
    Children xor();

    Children or(Consumer<Condition.Unary<M>> operator);
    Children xor(Consumer<Condition.Unary<M>> operator);
    Children and(Consumer<Condition.Unary<M>> operator);

    Children isNull(String column);
    Children isNull(boolean condition, String column);
    Children isNull(SerializableFunction<M, ?> column);
    Children isNull(boolean condition, SerializableFunction<M, ?> column);

    Children notNull(String column);
    Children notNull(boolean condition, String column);
    Children notNull(SerializableFunction<M, ?> column);
    Children notNull(boolean condition, SerializableFunction<M, ?> column);

    Children eq(SerializableFunction<M, ?> column, Object value);
    Children eq(boolean condition, SerializableFunction<M, ?> column, Object value);
    Children eq(String column, Object value);
    Children eq(boolean condition, String column, Object value);

    Children ne(SerializableFunction<M, ?> column, Object value);
    Children ne(boolean condition, SerializableFunction<M, ?> column, Object value);
    Children ne(String column, Object value);
    Children ne(boolean condition, String column, Object value);

    Children in(SerializableFunction<M, ?> column, Collection<?> value);
    Children in(boolean condition, SerializableFunction<M, ?> column, Collection<?> value);
    Children in(String column, Collection<?> value);
    Children in(boolean condition, String column, Collection<?> value);

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
}
