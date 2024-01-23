package com.mapledsl.core.condition;

import com.mapledsl.core.model.Model;

import java.util.function.Consumer;

/**
 * The TraversalStep interface extends the Traversal interface and provides methods for specifying traversal definitions and filters.
 * It represents a step in the traversal process.
 */
public interface TraversalStep extends Traversal {

    /**
     * Specifies the traversal definitions referenced by the destination vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param step specified traversal definitions or filters.
     * @param alias out vertex alias
     * @return the current ref of Traversal.
     */
    <V extends Model.V<?>> TraversalStep outV(String alias, Consumer<Step<V>> step);
    <V extends Model.V<?>> TraversalStep outV(String alias, Class<V> label, Consumer<Step<V>> step);
    <V extends Model.V<?>> TraversalStep outV(String alias, String label, Consumer<Step<V>> fetch);
    /**
     * Specifies the traversal definitions referenced by the source vertex.
     * @param <V> specifies the vertex type of the model conditions(tokens/predicates).
     * @param step specified traversal definitions or filters.
     * @param alias in vertex alias
     * @return the current ref of Traversal.
     */
    <V extends Model.V<?>> TraversalStep inV(String alias, Consumer<Step<V>> step);
    <V extends Model.V<?>> TraversalStep inV(String alias, Class<V> label, Consumer<Step<V>> step);
    <V extends Model.V<?>> TraversalStep inV(String alias, String label, Consumer<Step<V>> step);
    /**
     * Specifies the traversal definitions referenced by the path of edge.
     * @param <E> specifies the edge type of the model conditions(tokens/predicates).
     * @param step specified traversal definitions or filters.
     * @param alias edge alias
     * @return the current ref of Traversal.
     */
    <E extends Model.E<?,?>> TraversalStep edge(String alias, Consumer<Step<E>> step);
    <E extends Model.E<?,?>> TraversalStep edge(String alias, Class<E> label, Consumer<Step<E>> step);
    <E extends Model.E<?,?>> TraversalStep edge(String alias, String label, Consumer<Step<E>> step);

    TraversalStep limit(int limit);
    TraversalStep limit(int skip, int limit);

    interface Step<M extends Model<?>> extends Condition<M, Step<M>>, Query<M> {
        Query<M> selectAll();
    }
}
