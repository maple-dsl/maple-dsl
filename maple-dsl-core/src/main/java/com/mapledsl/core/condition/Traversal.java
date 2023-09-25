package com.mapledsl.core.condition;

import com.mapledsl.core.model.Model;

import java.util.Collection;
import java.util.function.Consumer;

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
    Traversal inE(int stepM, int stepN, Collection<Class<? extends Model.E>> types);
    Traversal inE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal inE(int step, Collection<Class<? extends Model.E>> types);
    Traversal inE(int step, Class<? extends Model.E> type);
    Traversal inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal inE(Collection<Class<? extends Model.E>> types);
    Traversal inE(Class<? extends Model.E> type);
    Traversal inE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal inE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal inE(int stepM, int stepN, String... types);
    Traversal inE(int step, String... types);
    Traversal inE(String... types);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for incoming edges.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal outE(int stepM, int stepN, Collection<Class<? extends Model.E>> types);
    Traversal outE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal outE(int step, Collection<Class<? extends Model.E>> types);
    Traversal outE(int step, Class<? extends Model.E> type);
    Traversal outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal outE(Collection<Class<? extends Model.E>> types);
    Traversal outE(Class<? extends Model.E> type);
    Traversal outE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal outE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal outE(int stepM, int stepN, String... types);
    Traversal outE(int step, String... types);
    Traversal outE(String... types);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for edges of both directions.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    Traversal bothE(int stepM, int stepN, Collection<Class<? extends Model.E>> types);
    Traversal bothE(int stepM, int stepN, Class<? extends Model.E> type);
    Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal bothE(int step, Collection<Class<? extends Model.E>> types);
    Traversal bothE(int step, Class<? extends Model.E> type);
    Traversal bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal bothE(Collection<Class<? extends Model.E>> types);
    Traversal bothE(Class<? extends Model.E> type);
    Traversal bothE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    Traversal bothE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    Traversal bothE(int stepM, int stepN, String... types);
    Traversal bothE(int step, String... types);
    Traversal bothE(String... types);
    /**
     * Specifies the traversal definitions referenced by the destination vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param step specified traversal definitions or filters.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal outV(String alias, Consumer<Step<V>> step);
    <V extends Model.V> Traversal outV(String alias, Class<V> label, Consumer<Step<V>> step);
    <V extends Model.V> Traversal outV(String alias, String label, Consumer<Step<V>> fetch);
    /**
     * Specifies the traversal definitions referenced by the source vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param step specified traversal definitions or filters.
     * @return the current ref of Traversal.
     */
    <V extends Model.V> Traversal inV(String alias, Consumer<Step<V>> step);
    <V extends Model.V> Traversal inV(String alias, Class<V> label, Consumer<Step<V>> step);
    <V extends Model.V> Traversal inV(String alias, String label, Consumer<Step<V>> step);
    /**
     * Specifies the traversal definitions referenced by the path of edge.
     * @param <E> specifies the edge type of the model conditions(tokens/predicates).
     * @param step specified traversal definitions or filters.
     * @return the current ref of Traversal.
     */
    <E extends Model.E> Traversal edge(String alias, Consumer<Step<E>> step);
    <E extends Model.E> Traversal edge(String alias, Class<E> label, Consumer<Step<E>> step);
    <E extends Model.E> Traversal edge(String alias, String label, Consumer<Step<E>> step);

    interface Step<M extends Model<?>> extends Condition<M, Step<M>>, Query<M, Sort<M>> {
        void noneSelect();
        void allSelect();
    }
}
