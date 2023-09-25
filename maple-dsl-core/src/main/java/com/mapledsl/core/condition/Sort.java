package com.mapledsl.core.condition;

import com.mapledsl.core.model.Model;

public interface Sort<M extends Model<?>> extends Query<M, Sort<M>> {
    /**
     * It specifies the according to the order(asc&desc) column criteria.
     * <p>
     * [note] If the order columns is not include query columns, then it will complete in query columns as default.
     *
     * @param first the first query order column criteria.
     * @param others  the other query order columns criteria.
     * @return the current ref of the fetch condition wrapper.
     */
    Query<M, Sort<M>> ascending();
    Query<M, Sort<M>> descending();
}
