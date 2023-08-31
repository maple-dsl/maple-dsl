package com.mapledsl.core.condition;

import com.mapledsl.core.condition.common.P;
import com.mapledsl.core.condition.common.T;
import com.mapledsl.core.model.Model;

/**
 * @author bofa1ex
 * @since 2023/08/28
 * @param <>
 */
public interface Traversal extends Wrapper<Traversal> {
    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for outgoing edges.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal inE(int stepM, int stepN, Class<? extends Model.E>[] types);
    Traversal inE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal inE(int stepM, int stepN, String[] types);
    Traversal inE(int stepM, int stepN, String type);
    Traversal inE(int stepM, int stepN, String first, String second);
    Traversal inE(int stepM, int stepN, String first, String second, String third);

    Traversal inE(int step, Class<? extends Model.E>[] types);
    Traversal inE(int step, Class<? extends Model.E> type);
    Traversal inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal inE(int step, String... types);
    Traversal inE(int step, String type);
    Traversal inE(int step, String first, String second);
    Traversal inE(int step, String first, String second, String third);

    Traversal inE(Class<? extends Model.E>[] types);
    Traversal inE(Class<? extends Model.E> type);
    Traversal inE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal inE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal inE(String[] types);
    Traversal inE(String type);
    Traversal inE(String first, String second);
    Traversal inE(String first, String second, String third);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for incoming edges.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal outE(int stepM, int stepN, Class<? extends Model.E>[] types);
    Traversal outE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal outE(int stepM, int stepN, String[] types);
    Traversal outE(int stepM, int stepN, String type);
    Traversal outE(int stepM, int stepN, String first, String second);
    Traversal outE(int stepM, int stepN, String first, String second, String third);

    Traversal outE(int step, Class<? extends Model.E>[] types);
    Traversal outE(int step, Class<? extends Model.E> type);
    Traversal outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal outE(int step, String... types);
    Traversal outE(int step, String type);
    Traversal outE(int step, String first, String second);
    Traversal outE(int step, String first, String second, String third);

    Traversal outE(Class<? extends Model.E>[] types);
    Traversal outE(Class<? extends Model.E> type);
    Traversal outE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal outE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal outE(String[] types);
    Traversal outE(String type);
    Traversal outE(String first, String second);
    Traversal outE(String first, String second, String third);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for edges of both directions.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal bothE(int stepM, int stepN, Class<? extends Model.E>[] types);
    Traversal bothE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal bothE(int stepM, int stepN, String[] types);
    Traversal bothE(int stepM, int stepN, String type);
    Traversal bothE(int stepM, int stepN, String first, String second);
    Traversal bothE(int stepM, int stepN, String first, String second, String third);

    Traversal bothE(int step, Class<? extends Model.E>[] types);
    Traversal bothE(int step, Class<? extends Model.E> type);
    Traversal bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal bothE(int step, String... types);
    Traversal bothE(int step, String type);
    Traversal bothE(int step, String first, String second);
    Traversal bothE(int step, String first, String second, String third);

    Traversal bothE(Class<? extends Model.E>[] types);
    Traversal bothE(Class<? extends Model.E> type);
    Traversal bothE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal bothE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal bothE(String[] types);
    Traversal bothE(String type);
    Traversal bothE(String first, String second);
    Traversal bothE(String first, String second, String third);

    /**
     * Specifies the traversal filters referenced by the source vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param predicates specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal inV(P<V>[] predicates);
    <V extends Model.V> Traversal inV(P<V> predicate);
    <V extends Model.V> Traversal inV(P<V> first, P<Model.V> second);
    <V extends Model.V> Traversal inV(P<V> first, P<Model.V> second, P<Model.V> third);
    /**
     * Specifies the traversal definitions referenced by the source vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param selections specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal inV(T<V>[] selections);
    <V extends Model.V> Traversal inV(T<V> selection);
    <V extends Model.V> Traversal inV(T<V> first, T<V> second);
    <V extends Model.V> Traversal inV(T<V> first, T<V> second, T<V> third);
    /**
     * Specifies the traversal definitions/filters referenced by the destination vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param predicates specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal outV(P<V>[] predicates);
    <V extends Model.V> Traversal outV(P<V> predicate);
    <V extends Model.V> Traversal outV(P<V> first, P<V> second);
    <V extends Model.V> Traversal outV(P<V> first, P<V> second, P<V> third);
    /**
     * Specifies the traversal definitions referenced by the destination vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param selections specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal outV(T<V>[] selections);
    <V extends Model.V> Traversal outV(T<V> selection);
    <V extends Model.V> Traversal outV(T<V> first, T<V> second);
    <V extends Model.V> Traversal outV(T<V> first, T<V> second, T<V> third);
    /**
     * Specifies the traversal filters referenced by the path of edge.
     * @param <E> specifies the edge type of the model conditions(tokens/predicates).
     * @param predicates specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <E extends Model.E> Traversal E(P<E>[] predicates);
    <E extends Model.E> Traversal E(P<E> predicate);
    <E extends Model.E> Traversal E(P<E> first, P<E> second);
    <E extends Model.E> Traversal E(P<E> first, P<E> second, P<E> third);
    /**
     * Specifies the traversal definitions referenced by the path of edge.
     * @param <E> specifies the edge type of the model conditions(tokens/predicates).
     * @param selections specified traversal definitions or filters.
     * @see P Predefined {@code Predicate} values that can be used with.
     * @see T A collection of (T)okens which allows for more concise Traversal definitions.
     * @return the current ref of Traversal.
     */
    <E extends Model.E>  Traversal E(T<E>[] selections);
    <E extends Model.E>  Traversal E(T<E> selection);
    <E extends Model.E>  Traversal E(T<E> first, T<E> second);
    <E extends Model.E>  Traversal E(T<E> first, T<E> second, T<E> third);
}
