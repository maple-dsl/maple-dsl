package com.mapledsl.core.condition.common;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

/**
 * @author bofa1ex
 * @since 2023/8/14
 */
public class T<M extends Model<?>> {
    private String[] columns;
    private String[] aliases;
    private Class<M> instantiated;
    private boolean all = false;

    public String label(MapleDslConfiguration ctx) {
        if (instantiated == null) return null;
        return ctx.getLabel(instantiated);
    }

    public boolean isAllPresent() {
        return all;
    }

    public boolean isNotPresent() {
        return columns == null;
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

    public static <M extends Model<?>> T<M> it(String alias) {
        if (alias == null || alias.trim().isEmpty()) throw new IllegalArgumentException("select alias must not be blank.");

        final T<M> t = new T<>();
        t.aliases = new String[] { alias };
        t.all = true;

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

    public static <M extends Model<?>> T<M> select(@NotNull String... columns) {
        if (columns == null || columns.length == 0) throw new IllegalArgumentException("select columns must not be empty.");

        final T<M> t = new T<>();
        t.columns = columns;
        t.aliases = columns;
        return t;
    }

    @SafeVarargs
    public static <M extends Model<?>> T<M> select(@NotNull SerializableFunction<M, ?>... func) {
        if (func == null || func.length == 0) throw new IllegalArgumentException("select columns must not be empty.");

        final String[] columns = new String[func.length];
        final T<M> t = new T<>();
        t.instantiated = func[0].asMeta().getKey();

        Arrays.setAll(columns, i -> func[i].asText());
        t.columns = columns;
        t.aliases = columns;

        return t;
    }
}
