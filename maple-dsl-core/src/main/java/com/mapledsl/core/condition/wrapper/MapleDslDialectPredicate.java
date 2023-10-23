package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.model.Model;

import static java.util.Optional.ofNullable;

public final class MapleDslDialectPredicate<M extends Model<?>> extends MapleDslDialectBase<M> {
    private final String column;
    private final Object value;
    private final OP op;

    public OP connection;
    public MapleDslDialectPredicate<M> next;

    public boolean hasPrefix, hasSuffix;

    MapleDslDialectPredicate(String column, OP op) {
        this(column, op, null);
    }

    MapleDslDialectPredicate(String column, OP op, Object value) {
        this.column = column;
        this.op = op;
        this.value = value;
    }

    public boolean hasNext() {
        return next != null;
    }

    public String value(MapleDslConfiguration ctx) {
        if (value == null) return null;
        if (ctx == null) throw new IllegalArgumentException();
        if (instantiatedLabelClazz != null) return ctx.beanDefinition(instantiatedLabelClazz).parameterized(column, value);

        return ofNullable(ctx.parameterHandler(value.getClass()))
                .map(it -> it.apply(value, ctx))
                .orElse(null);
    }

    public String column() {
        return column;
    }

    public Object value() {
        return value;
    }

    public OP op() {
        return op;
    }
}
