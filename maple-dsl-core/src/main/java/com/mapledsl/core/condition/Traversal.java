package com.mapledsl.core.condition;

import com.mapledsl.core.model.Model;

import java.util.Collection;

/**
 * @author bofa1ex
 * @since 2023/08/28
 */
public interface Traversal {
    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for outgoing edges.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    TraversalStep inE(int stepM, int stepN, Collection<Class<? extends Model.E>> types);
    TraversalStep inE(int stepM, int stepN, Class<? extends Model.E> type);
    TraversalStep inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep inE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep inE(int step, Collection<Class<? extends Model.E>> types);
    TraversalStep inE(int step, Class<? extends Model.E> type);
    TraversalStep inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep inE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep inE(Collection<Class<? extends Model.E>> types);
    TraversalStep inE(Class<? extends Model.E> type);
    TraversalStep inE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep inE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep inE(int stepM, int stepN, String... types);
    TraversalStep inE(int step, String... types);
    TraversalStep inE(String... types);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for incoming edges.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    TraversalStep outE(int stepM, int stepN, Collection<Class<? extends Model.E>> types);
    TraversalStep outE(int stepM, int stepN, Class<? extends Model.E> type);
    TraversalStep outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep outE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep outE(int step, Collection<Class<? extends Model.E>> types);
    TraversalStep outE(int step, Class<? extends Model.E> type);
    TraversalStep outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep outE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep outE(Collection<Class<? extends Model.E>> types);
    TraversalStep outE(Class<? extends Model.E> type);
    TraversalStep outE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep outE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep outE(int stepM, int stepN, String... types);
    TraversalStep outE(int step, String... types);
    TraversalStep outE(String... types);

    /**
     * Specifies the conditions of stepM, stepN, edge_types to search for edges of both directions.
     * @param stepM from step number, specifies the hop number.
     * @param stepN to step number, specifies the hop number.
     * @param types represents a list of edge types which the traversal can go through.
     * @return the current ref of Traversal.
     */
    TraversalStep bothE(int stepM, int stepN, Collection<Class<? extends Model.E>> types);
    TraversalStep bothE(int stepM, int stepN, Class<? extends Model.E> type);
    TraversalStep bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep bothE(int stepM, int stepN, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep bothE(int step, Collection<Class<? extends Model.E>> types);
    TraversalStep bothE(int step, Class<? extends Model.E> type);
    TraversalStep bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep bothE(int step, Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep bothE(Collection<Class<? extends Model.E>> types);
    TraversalStep bothE(Class<? extends Model.E> type);
    TraversalStep bothE(Class<? extends Model.E> first, Class<? extends Model.E> second);
    TraversalStep bothE(Class<? extends Model.E> first, Class<? extends Model.E> second, Class<? extends Model.E> third);

    TraversalStep bothE(int stepM, int stepN, String... types);
    TraversalStep bothE(int step, String... types);
    TraversalStep bothE(String... types);
}
