package com.mapledsl.core.condition.wrapper;

import com.mapledsl.core.condition.common.OP;
import com.mapledsl.core.model.Model;
import org.apiguardian.api.API;

@API(status = API.Status.INTERNAL)
public final class MapleDslDialectPredicate<M extends Model<?>> extends MapleDslDialectBase<M> {
    private final String column;
    private final OP op;
    private final Object value;

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
