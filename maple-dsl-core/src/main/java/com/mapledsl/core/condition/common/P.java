package com.mapledsl.core.condition.common;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * @author bofa1ex
 * @since 2023/8/14
 */
@SuppressWarnings("unused")
public class P<M extends Model<?>> {
    private final String column;
    private final Object value;
    private final OP op;

    private String label;
    private Class<M> instantiated;

    private P<M> next;
    private OP connection;

    public String column() {
        return column;
    }

    public OP op() {
        return op;
    }

    public Object value() {
        return value;
    }

    public boolean hasNext() {
        return next != null;
    }

    public P<M> next() {
        return next;
    }

    public OP connection() {
        return connection;
    }

    public String value(MapleDslConfiguration ctx) {
        if (value == null) return null;
        if (ctx == null) throw new IllegalArgumentException();
        if (instantiated != null) return ctx.getBeanDefinition(instantiated).parameterized(column, value);

        return ofNullable(ctx.getParameterHandler(value.getClass()))
                .map(it -> it.apply(value, ctx))
                .orElse(null);
    }

    public String label(MapleDslConfiguration ctx) {
        if (label != null && !label.trim().isEmpty()) return label;
        if (instantiated == null) return null;
        return ctx.getLabel(instantiated);
    }

    P(SerializableFunction<M, ?> column, OP op) {
        this(column, op, null);
    }

    P(String column, OP op, String label) {
        this(column, op, null, label);
    }

    P(String column, OP op, Class<M> instantiated) {
        this(column, op, null, instantiated);
    }

    P(String column, OP op) {
        this.column = column;
        this.op = op;
        this.value = null;
    }

    P(SerializableFunction<M, ?> column, OP op, Object value) {
        final Map.Entry<Class<M>, String> meta = column.asMeta();
        this.instantiated = meta.getKey();
        this.column = meta.getValue();
        this.op = op;
        this.value = value;
    }

    P(String column, OP op, Object value) {
        this.column = column;
        this.op = op;
        this.value = value;
    }

    P(String column, OP op, Object value, String label) {
        this(column, op, value);
        this.label = label;
    }

    P(String column, OP op, Object value, Class<M> instantiated) {
        this(column, op, value);
        this.instantiated = instantiated;
    }

    public P<M> or(P<M> or) {
        this.next = or;
        this.connection = OP.OR;
        return this;
    }

    public P<M> xor(P<M> xor) {
        this.next = xor;
        this.connection = OP.XOR;
        return this;
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> eq(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.EQ, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> @NotNull P<M> eq(SerializableFunction<M, R> func, R value) {
        return new P<>(func, OP.EQ, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> eq(boolean condition, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.EQ, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> eq(boolean condition, String label, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.EQ, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> eq(boolean condition, Class<M> instantiated, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.EQ, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> eq(String col, R value) {
        return eq(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> eq(String label, String col, R value) {
        return eq(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> eq(Class<M> instantiated, String col, R value) {
        return eq(true, instantiated, col, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> lt(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.LT, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> lt(SerializableFunction<M, R> func, R value) {
        return lt(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> lt(boolean condition, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.LT, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> lt(boolean condition, String label, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.LT, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> lt(boolean condition, Class<M> instantiated, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.LT, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> lt(String col, R value) {
        return lt(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> lt(String label, String col, R value) {
        return lt(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> lt(Class<M> instantiated, String col, R value) {
        return lt(true, instantiated, col, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> le(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.LE, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> le(SerializableFunction<M, R> func, R value) {
        return le(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> le(boolean condition, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.LE, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> le(boolean condition, String label, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.LE, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> le(boolean condition, Class<M> instantiated, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.LE, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> le(String col, R value) {
        return le(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> le(String label, String col, R value) {
        return le(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> le(Class<M> instantiated, String col, R value) {
        return le(true, instantiated, col, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> gt(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.GT, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> gt(SerializableFunction<M, R> func, R value) {
        return gt(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> gt(boolean condition, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.GT, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> gt(boolean condition, String label, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.GT, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> gt(boolean condition, Class<M> instantiated, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.GT, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> gt(String col, R value) {
        return gt(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> gt(String label, String col, R value) {
        return gt(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> gt(Class<M> instantiated, String col, R value) {
        return gt(true, instantiated, col, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> ge(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.GE, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> ge(SerializableFunction<M, R> func, R value) {
        return ge(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ge(boolean condition, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.GE, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ge(boolean condition, String label, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.GE, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ge(boolean condition, Class<M> instantiated, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.GE, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ge(String col, R value) {
        return ge(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ge(String label, String col, R value) {
        return ge(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ge(Class<M> instantiated, String col, R value) {
        return ge(true, instantiated, col, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> ne(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.NE, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> ne(SerializableFunction<M, R> func, R value) {
        return ne(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ne(boolean condition, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.NE, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ne(boolean condition, String label, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.NE, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ne(boolean condition, Class<M> instantiated, String col, R value) {
        if (!condition) return null;
        return new P<>(col, OP.NE, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ne(String col, R value) {
        return ne(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ne(String label, String col, R value) {
        return ne(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> ne(Class<M> instantiated, String col, R value) {
        return ne(true, instantiated, col, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> in(boolean condition, SerializableFunction<M, R> func, Collection<R> value) {
        if (!condition) return null;
        return new P<>(func, OP.IN, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> in(SerializableFunction<M, R> func, Collection<R> value) {
        return in(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> in(boolean condition, String col, Collection<R> value) {
        if (!condition) return null;
        return new P<>(col, OP.IN, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> in(boolean condition, String label, String col, Collection<R> value) {
        if (!condition) return null;
        return new P<>(col, OP.IN, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> in(boolean condition, Class<M> instantiated, String col, Collection<R> value) {
        if (!condition) return null;
        return new P<>(col, OP.IN, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> in(String col, Collection<R> value) {
        return in(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> in(String label, String col, Collection<R> value) {
        return in(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> in(Class<M> instantiated, String col, Collection<R> value) {
        return in(true, instantiated, col, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(boolean condition, SerializableFunction<M, R> func, Collection<R> value) {
        if (!condition) return null;
        return new P<>(func, OP.NOT_IN, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(SerializableFunction<M, R> func, Collection<R> value) {
        return notIn(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(boolean condition, String col, Collection<R> value) {
        if (!condition) return null;
        return new P<>(col, OP.NOT_IN, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(boolean condition, String label, String col, Collection<R> value) {
        if (!condition) return null;
        return new P<>(col, OP.NOT_IN, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(boolean condition, Class<M> instantiated, String col, Collection<R> value) {
        if (!condition) return null;
        return new P<>(col, OP.NOT_IN, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(String col, Collection<R> value) {
        return notIn(true, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(String label, String col, Collection<R> value) {
        return notIn(true, label, col, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notIn(Class<M> instantiated, String col, Collection<R> value) {
        return notIn(true, instantiated, col, value);
    }

    @Contract("false, _ -> null; true, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(boolean condition, SerializableFunction<M, R> func) {
        if (!condition) return null;
        return new P<>(func, OP.ISNULL);
    }

    @Contract("_ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(SerializableFunction<M, R> func) {
        return isNull(true, func);
    }

    @Contract(value = "false, _ -> null; true, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(boolean condition, String col) {
        if (!condition) return null;
        return new P<>(col, OP.ISNULL);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(boolean condition, String label, String col) {
        if (!condition) return null;
        return new P<>(col, OP.ISNULL, label);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(boolean condition, Class<M> instantiated, String col) {
        if (!condition) return null;
        return new P<>(col, OP.ISNULL, instantiated);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(String col) {
        return isNull(true, col);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(String label, String col) {
        return isNull(true, label, col);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> isNull(Class<M> instantiated, String col) {
        return isNull(true, instantiated, col);
    }

    @Contract("false, _ -> null; true, _ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(boolean condition, SerializableFunction<M, R> func) {
        if (!condition) return null;
        return new P<>(func, OP.NOT_NULL);
    }

    @Contract("_ -> new")
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(SerializableFunction<M, R> func) {
        return notNull(true, func);
    }

    @Contract(value = "false, _ -> null; true, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(boolean condition, String col) {
        if (!condition) return null;
        return new P<>(col, OP.NOT_NULL);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(boolean condition, String label, String col) {
        if (!condition) return null;
        return new P<>(col, OP.NOT_NULL, label);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(boolean condition, Class<M> instantiated, String col) {
        if (!condition) return null;
        return new P<>(col, OP.NOT_NULL, instantiated);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(String col) {
        return notNull(true, col);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(String label, String col) {
        return notNull(true, label, col);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends Serializable> P<M> notNull(String label, Class<M> instantiated) {
        return notNull(true, instantiated, label);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> contains(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.CONTAINS, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> contains(SerializableFunction<M, R> func, R value) {
        return contains(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> contains(boolean condition, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.CONTAINS, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> contains(boolean condition, String label, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.CONTAINS, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> contains(boolean condition, Class<M> instantiated, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.CONTAINS, value, instantiated);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> contains(String label, String column, R value) {
        return contains(true, label, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> contains(Class<M> instantiated, String column, R value) {
        return contains(true, instantiated, column, value);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> contains(String column, R value) {
        return contains(true, column, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> startsWith(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.STARTS_WITH, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> startsWith(SerializableFunction<M, R> func, R value) {
        return startsWith(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> startsWith(boolean condition, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.STARTS_WITH, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> startsWith(boolean condition, String label, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.STARTS_WITH, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> startsWith(boolean condition, Class<M> instantiated, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.STARTS_WITH, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> startsWith(String column, R value) {
        return startsWith(true, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> startsWith(String label, String column, R value) {
        return startsWith(true, label, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> startsWith(Class<M> instantiated, String column, R value) {
        return startsWith(true, instantiated, column, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.NOT_STARTS_WITH, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(SerializableFunction<M, R> func, R value) {
        return notStartsWith(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(boolean condition, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.NOT_STARTS_WITH, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(boolean condition, String label, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.NOT_STARTS_WITH, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(boolean condition, Class<M> instantiated, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.NOT_STARTS_WITH, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(String column, R value) {
        return notStartsWith(true, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(String label, String column, R value) {
        return notStartsWith(true, label, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notStartsWith(Class<M> instantiated, String column, R value) {
        return notStartsWith(true, instantiated, column, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> endsWith(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.ENDS_WITH, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> endsWith(SerializableFunction<M, R> func, R value) {
        return endsWith(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> endsWith(boolean condition, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.ENDS_WITH, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> endsWith(boolean condition, String label, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.ENDS_WITH, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> endsWith(boolean condition, Class<M> instantiated, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.ENDS_WITH, value, instantiated);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> endsWith(String column, R value) {
        return endsWith(true, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> endsWith(String label, String column, R value) {
        return endsWith(true, label, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> endsWith(Class<M> instantiated, String column, R value) {
        return endsWith(true, instantiated, column, value);
    }

    @Contract("false, _, _ -> null; true, _, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(boolean condition, SerializableFunction<M, R> func, R value) {
        if (!condition) return null;
        return new P<>(func, OP.NOT_ENDS_WITH, value);
    }

    @Contract("_, _ -> new")
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(SerializableFunction<M, R> func, R value) {
        return notEndsWith(true, func, value);
    }

    @Contract(value = "false, _, _ -> null; true, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(boolean condition, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.NOT_ENDS_WITH, value);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(boolean condition, String label, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.NOT_ENDS_WITH, value, label);
    }

    @Contract(value = "false, _, _, _ -> null; true, _, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(boolean condition, Class<M> instantiated, String column, R value) {
        if (!condition) return null;
        return new P<>(column, OP.NOT_ENDS_WITH, value, instantiated);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(String label, String column, R value) {
        return notEndsWith(true, label, column, value);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(Class<M> instantiated, String column, R value) {
        return notEndsWith(true, instantiated, column, value);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <M extends Model<?>, R extends String> P<M> notEndsWith(String column, R value) {
        return notEndsWith(true, column, value);
    }
}
