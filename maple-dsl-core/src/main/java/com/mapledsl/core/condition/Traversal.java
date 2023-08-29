package com.mapledsl.core.condition;

import com.mapledsl.core.condition.common.P;
import com.mapledsl.core.condition.common.T;
import com.mapledsl.core.model.Model;

/**
 * @author bofa1ex
 * @since 2023/08/28
 * @param <M>
 */
public interface Traversal<M extends Model<?>> extends Query<M, Traversal<M>> {
    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for outgoing edges.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal<M> inE(int stepM, int stepN, Class<? extends Model.E>[] types);
    Traversal<M> inE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal<M> inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> inE(int stepM, int stepN, String[] types);
    Traversal<M> inE(int stepM, int stepN, String type);
    Traversal<M> inE(int stepM, int stepN, String first, String second);
    Traversal<M> inE(int stepM, int stepN, String first, String second, String third);

    Traversal<M> inE(int step, Class<? extends Model.E>[] types);
    Traversal<M> inE(int step, Class<? extends Model.E> type);
    Traversal<M> inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> inE(int step, String... types);
    Traversal<M> inE(int step, String type);
    Traversal<M> inE(int step, String first, String second);
    Traversal<M> inE(int step, String first, String second, String third);

    Traversal<M> inE(Class<? extends Model.E>[] types);
    Traversal<M> inE(Class<? extends Model.E> type);
    Traversal<M> inE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> inE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> inE(String[] types);
    Traversal<M> inE(String type);
    Traversal<M> inE(String first, String second);
    Traversal<M> inE(String first, String second, String third);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for incoming edges.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal<M> outE(int stepM, int stepN, Class<? extends Model.E>[] types);
    Traversal<M> outE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal<M> outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> outE(int stepM, int stepN, String[] types);
    Traversal<M> outE(int stepM, int stepN, String type);
    Traversal<M> outE(int stepM, int stepN, String first, String second);
    Traversal<M> outE(int stepM, int stepN, String first, String second, String third);

    Traversal<M> outE(int step, Class<? extends Model.E>[] types);
    Traversal<M> outE(int step, Class<? extends Model.E> type);
    Traversal<M> outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> outE(int step, String... types);
    Traversal<M> outE(int step, String type);
    Traversal<M> outE(int step, String first, String second);
    Traversal<M> outE(int step, String first, String second, String third);

    Traversal<M> outE(Class<? extends Model.E>[] types);
    Traversal<M> outE(Class<? extends Model.E> type);
    Traversal<M> outE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> outE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> outE(String[] types);
    Traversal<M> outE(String type);
    Traversal<M> outE(String first, String second);
    Traversal<M> outE(String first, String second, String third);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for edges of both directions.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal<M> bothE(int stepM, int stepN, Class<? extends Model.E>[] types);
    Traversal<M> bothE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal<M> bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> bothE(int stepM, int stepN, String[] types);
    Traversal<M> bothE(int stepM, int stepN, String type);
    Traversal<M> bothE(int stepM, int stepN, String first, String second);
    Traversal<M> bothE(int stepM, int stepN, String first, String second, String third);

    Traversal<M> bothE(int step, Class<? extends Model.E>[] types);
    Traversal<M> bothE(int step, Class<? extends Model.E> type);
    Traversal<M> bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> bothE(int step, String... types);
    Traversal<M> bothE(int step, String type);
    Traversal<M> bothE(int step, String first, String second);
    Traversal<M> bothE(int step, String first, String second, String third);

    Traversal<M> bothE(Class<? extends Model.E>[] types);
    Traversal<M> bothE(Class<? extends Model.E> type);
    Traversal<M> bothE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal<M> bothE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal<M> bothE(String[] types);
    Traversal<M> bothE(String type);
    Traversal<M> bothE(String first, String second);
    Traversal<M> bothE(String first, String second, String third);

    /**
     * Specifies the traversal filters referenced by the source vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param predicates specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see M A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal<M> inV(P<V>[] predicates);
    <V extends Model.V> Traversal<M> inV(P<V> predicate);
    <V extends Model.V> Traversal<M> inV(P<V> first, P<V> second);
    <V extends Model.V> Traversal<M> inV(P<V> first, P<V> second, P<V> third);
    /**
     * Specifies the traversal definitions referenced by the source vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param selections specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal<M> inV(T<V>[] selections);
    <V extends Model.V> Traversal<M> inV(T<V> selection);
    <V extends Model.V> Traversal<M> inV(T<V> first, T<V> second);
    <V extends Model.V> Traversal<M> inV(T<V> first, T<V> second, T<V> third);
    /**
     * Specifies the traversal definitions/filters referenced by the destination vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param predicates specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal<M> outV(P<V>[] predicates);
    <V extends Model.V> Traversal<M> outV(P<V> predicate);
    <V extends Model.V> Traversal<M> outV(P<V> first, P<V> second);
    <V extends Model.V> Traversal<M> outV(P<V> first, P<V> second, P<V> third);
    /**
     * Specifies the traversal definitions referenced by the destination vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param conditions specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal<M> outV(T<V>[] conditions);
    <V extends Model.V> Traversal<M> outV(T<V> selection);
    <V extends Model.V> Traversal<M> outV(T<V> first, T<V> second);
    <V extends Model.V> Traversal<M> outV(T<V> first, T<V> second, T<V> third);
    /**
     * Specifies the traversal filters referenced by the path of edge.
     * @param <E> specifies the edge type of the model conditions(tokens/predicates).
     * @param predicates specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <E extends Model.E> Traversal<M> E(P<E>[] predicates);
    <E extends Model.E> Traversal<M> E(P<E> predicate);
    <E extends Model.E> Traversal<M> E(P<E> first, P<E> second);
    <E extends Model.E> Traversal<M> E(P<E> first, P<E> second, P<E> third);
    /**
     * Specifies the traversal definitions referenced by the path of edge.
     * @param <E> specifies the edge type of the model conditions(tokens/predicates).
     * @param selections specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <E extends Model.E>  Traversal<M> E(T<E>[] selections);
    <E extends Model.E>  Traversal<M> E(T<E> selection);
    <E extends Model.E>  Traversal<M> E(T<E> first, T<E> second);
    <E extends Model.E>  Traversal<M> E(T<E> first, T<E> second, T<E> third);
}
