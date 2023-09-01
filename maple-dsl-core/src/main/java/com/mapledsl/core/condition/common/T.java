package com.mapledsl.core.condition.common;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

import java.util.Arrays;
import java.util.Map;

/**
 * @author bofa1ex
 * @since 2023/8/14
 */
public class T<M extends Model<?>> {
    private String[] columns;
    private String[] aliases;

    private String label;
    private Class<M> instantiated;

    private boolean all = false;
    private boolean pinning = false;
    private boolean ascending = false;
    private boolean descending = false;

    public String label(MapleDslConfiguration ctx) {
        if (label != null && !label.trim().isEmpty()) return label;
        if (instantiated == null) return null;
        return ctx.getLabel(instantiated);
    }

    public boolean isAllPresent() {
        return all;
    }

    public boolean isNotPresent() {
        return columns == null;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isDescending() {
        return descending;
    }

    public boolean isPinning() {
        return pinning;
    }

    public String[] columns() {
        return columns;
    }

    public String alias() {
        return aliases[0];
    }

    public String[] aliases() {
        return aliases;
    }

    public T<M> ascending() {
        descending = false;
        ascending = true;
        return this;
    }

    public T<M> descending() {
        ascending = false;
        descending = true;
        return this;
    }

    public T<M> pin() {
        pinning = true;
        return this;
    }

    public static <M extends Model<?>> T<M> it(String label, String alias) {
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final T<M> t = new T<>();
        t.aliases = new String[] { alias };
        t.all = true;
        t.label = label;

        return t;
    }

    public static <M extends Model<?>> T<M> it(Class<M> instantiated, String alias) {
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final T<M> t = new T<>();
        t.aliases = new String[] { alias };
        t.all = true;
        t.instantiated = instantiated;

        return t;
    }

    public static <M extends Model<?>> T<M> it(String alias) {
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final T<M> t = new T<>();
        t.aliases = new String[] { alias };
        t.all = true;

        return t;
    }

    public static <M extends Model<?>> T<M> selectAs(String label, String column, String alias) {
        if (column == null || column.trim().isEmpty()) throw new IllegalArgumentException("select column must not be blank.");
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final T<M> t = new T<>();
        t.columns = new String[] { column };
        t.aliases = new String[] { alias };
        t.label = label;

        return t;
    }

    public static <M extends Model<?>> T<M> selectAs(Class<M> instantiated, String column, String alias) {
        if (column == null || column.trim().isEmpty()) throw new IllegalArgumentException("select column must not be blank.");
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final T<M> t = new T<>();
        t.columns = new String[] { column };
        t.aliases = new String[] { alias };
        t.instantiated = instantiated;

        return t;
    }

    public static <M extends Model<?>> T<M> selectAs(String column, String alias) {
        if (column == null || column.trim().isEmpty()) throw new IllegalArgumentException("select column must not be blank.");
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final T<M> t = new T<>();
        t.columns = new String[] { column };
        t.aliases = new String[] { alias };

        return t;
    }

    public static <M extends Model<?>> T<M> selectAs(SerializableFunction<M, ?> func, String alias) {
        if (func == null) throw new IllegalArgumentException("select column must not be empty.");
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final Map.Entry<Class<M>, String> funcMeta = func.asMeta();
        final T<M> t = new T<>();
        t.columns = new String[] { funcMeta.getValue() };
        t.aliases = new String[] { alias };
        t.instantiated = funcMeta.getKey();
        return t;
    }

    public static <M extends Model<?>> T<M> select(String... columns) {
        if (columns == null || columns.length == 0) throw new IllegalArgumentException("select columns must not be empty.");

        final T<M> t = new T<>();
        t.columns = columns;
        t.aliases = columns;
        return t;
    }


    public static <M extends Model<?>> T<M> select(String label, String... columns) {
        if (columns == null || columns.length == 0) throw new IllegalArgumentException("select columns must not be empty.");

        final T<M> t = new T<>();
        t.columns = columns;
        t.aliases = columns;
        t.label = label;

        return t;
    }


    public static <M extends Model<?>> T<M> select(Class<M> instantiated, String... columns) {
        if (columns == null || columns.length == 0) throw new IllegalArgumentException("select columns must not be empty.");

        final T<M> t = new T<>();
        t.columns = columns;
        t.aliases = columns;
        t.instantiated = instantiated;

        return t;
    }

    @SafeVarargs
    public static <M extends Model<?>> T<M> select(SerializableFunction<M, ?>... func) {
        if (func == null || func.length == 0) throw new IllegalArgumentException("select columns must not be empty.");

        final String[] columns = new String[func.length];
        final T<M> t = new T<>();
        t.instantiated = func[0].asMeta().getKey();

        Arrays.setAll(columns, i -> func[i].asText());
        t.columns = columns;
        t.aliases = columns;

        return t;
    }

    public static <M extends Model<?>> T<M> pin(String label, String... columns) {
        return T.<M>select(label, columns).pin();
    }

    public static <M extends Model<?>> T<M> pin(Class<M> instantiated, String... columns) {
        return T.select(instantiated, columns).pin();
    }

    public static <M extends Model<?>> T<M> pin(String... columns) {
        return T.<M>select(columns).pin();
    }

    @SafeVarargs
    public static <M extends Model<?>> T<M> pin(SerializableFunction<M, ?>... func) {
        return select(func).pin();
    }

    public static <M extends Model<?>> T<M> ascending(String label, String... columns) {
        return T.<M>select(label, columns).ascending();
    }

    public static <M extends Model<?>> T<M> ascending(Class<M> instantiated, String... columns) {
        return T.select(instantiated, columns).ascending();
    }

    public static <M extends Model<?>> T<M> ascending(String... columns) {
        return T.<M>select(columns).ascending();
    }

    @SafeVarargs
    public static <M extends Model<?>> T<M> ascending(SerializableFunction<M, ?>... func) {
        return select(func).ascending();
    }

    public static <M extends Model<?>> T<M> descending(String label, String... columns) {
        return T.<M>select(label, columns).descending();
    }

    public static <M extends Model<?>> T<M> descending(Class<M> instantiated, String... columns) {
        return T.select(instantiated, columns).descending();
    }

    public static <M extends Model<?>> T<M> descending(String... columns) {
        return T.<M>select(columns).descending();
    }

    @SafeVarargs
    public static <M extends Model<?>> T<M> descending(SerializableFunction<M, ?>... func) {
        return select(func).descending();
    }
}
