package com.mapledsl.core.condition;

import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;

/**
 * @author bofa1ex
 * @since 2023/08/28
 * @param <T>
 * @param <Children>
 */
public interface Query<T extends Model<?>, Children extends Query<T, Children>> extends Wrapper {
    /**
     * It specifies the according to the order(asc&desc) column criteria.
     * <p>
     * [note] If the order columns is not include query columns, then it will complete in query columns as default.
     *
     * @param col   the query order column criteria.
     * @return the current ref of the fetch condition wrapper.
     */
    Children orderByAsc(String col);
    Children orderByAsc(String col1, String col2);
    Children orderByAsc(String col1, String col2, String col3);
    Children orderByAsc(String[] cols);

    Children orderByAsc(SerializableFunction<T, ?> col);
    Children orderByAsc(SerializableFunction<T, ?> col1, SerializableFunction<T, ?> col2);
    Children orderByAsc(SerializableFunction<T, ?> col1, SerializableFunction<T, ?> col2, SerializableFunction<T, ?> col3);
    Children orderByAsc(SerializableFunction<T, ?>[] cols);

    Children orderByDesc(String col);
    Children orderByDesc(String col1, String col2);
    Children orderByDesc(String col1, String col2, String col3);
    Children orderByDesc(String[] cols);

    Children orderByDesc(SerializableFunction<T, ?> col);
    Children orderByDesc(SerializableFunction<T, ?> col1, SerializableFunction<T, ?> col2);
    Children orderByDesc(SerializableFunction<T, ?> col1, SerializableFunction<T, ?> col2, SerializableFunction<T, ?> col3);
    Children orderByDesc(SerializableFunction<T, ?>[] cols);

    Children limit(int limit);
    Children limit(int skip, int limit);
}
