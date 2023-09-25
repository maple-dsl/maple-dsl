package com.mapledsl.core;

import com.mapledsl.core.condition.Fetch;
import com.mapledsl.core.condition.Match;
import com.mapledsl.core.condition.Traversal;
import com.mapledsl.core.condition.wrapper.FetchWrapper;
import com.mapledsl.core.condition.wrapper.MatchWrapper;
import com.mapledsl.core.condition.wrapper.TraversalWrapper;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;

import static com.mapledsl.core.MapleDslDialectRender.fetchE;
import static com.mapledsl.core.MapleDslDialectRender.fetchV;
import static com.mapledsl.core.MapleDslDialectRender.matchE;
import static com.mapledsl.core.MapleDslDialectRender.matchV;
import static com.mapledsl.core.MapleDslDialectRender.traversal;

public final class G {
    /**
     * A domain specific condition wrapper for traversing a graph using "graph concepts" (e.g. vertices, edges).
     * Represents a directed walk over a graph {@code Graph}.
     *
     * @param vertexId specific the id of vertex {@code Vertex}.
     * @return the basic traversing condition wrapper of the specific id of vertex as origin.
     */
    @Contract("_ -> new")
    public static @NotNull Traversal traverse(String vertexId) {
        return new TraversalWrapperFacade(vertexId);
    }

    @Contract("_ -> new")
    public static @NotNull Traversal traverse(Number vertexId) {
        return new TraversalWrapperFacade(vertexId);
    }

    @Contract("_ -> new")
    public static @NotNull Traversal traverse(String... vertexIds) {
        return new TraversalWrapperFacade(vertexIds);
    }

    @Contract("_ -> new")
    public static @NotNull Traversal traverse(Number... vertexIds) {
        return new TraversalWrapperFacade(vertexIds);
    }

    @Contract("_ -> new")
    public static Traversal traverse(Match<Model.V> match) {
        return new TraversalWrapperFacade(match);
    }

    /**
     * A domain specific condition wrapper for fetch vertex by the related VID & TAG.
     *
     * @param tag      specific the tag of the vertex entity {@code Model.V}.
     * @param <V>      specific the type of the vertex entity {@code Model.V}.
     * @return the basic fetching condition wrapper of the specified id of vertex & tag as origin.
     */
    @Contract("_ -> new")
    public static <V extends Model.V> @NotNull Match<V> vertex(Class<V> tag) {
        return new MatchVertexWrapper<>(tag, matchV);
    }

    @Contract("_ -> new")
    public static @NotNull Match<Model.V> vertex(String tag) {
        return new MatchVertexWrapper<>(tag, matchV);
    }

    @Contract("_ -> new")
    public static <E extends Model.E> @NotNull Match<E> edge(Class<E> tag) {
        return new MatchEdgeWrapper<>(tag, matchE);
    }

    @Contract("_ -> new")
    public static <E extends Model.E> @NotNull Match<E> edge(String tag) {
        return new MatchEdgeWrapper<>(tag, matchE);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Fetch<Model.V> vertex(String tag, String... vertexIds) {
        return new FetchVertexWrapper<>(tag, vertexIds, fetchV);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Fetch<Model.V> vertex(String tag, Number... vertexIds) {
        return new FetchVertexWrapper<>(tag, vertexIds, fetchV);
    }

    /**
     * A domain specific condition wrapper for fetch vertex by the related VID & TAG.
     *
     * @param tag      specific the tag of the vertex entity {@code Model.V}.
     * @param vertexIds specific the id of the vertex {@code Vertex}.
     * @param <V>      specific the type of the vertex entity {@code Model.V}.
     * @return the basic fetching condition wrapper of the specified id of vertex & tag as origin.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <V extends Model.V> @NotNull Fetch<V> vertex(Class<V> tag, String... vertexIds) {
        return new FetchVertexWrapper<>(tag, vertexIds, fetchV);
    }

    /**
     * A domain specific condition wrapper for fetch vertex by the related VID & TAG.
     *
     * @param tag       specific the tag of the vertex entity {@code Model.V}.
     * @param vertexIds specific the id of vertices {@code Vertex}.
     * @param <V>       specific the type of the vertex entity {@code Model.V}.
     * @return the basic fetching condition wrapper of the specified id of vertices & tag as origin.
     */
    @Contract("_, _ -> new")
    public static <V extends Model.V> @NotNull Fetch<V> vertex(Class<V> tag, Number... vertexIds) {
        return new FetchVertexWrapper<>(tag, vertexIds, fetchV);
    }

    /**
     * A domain specific condition wrapper for fetch edge by the related ELEMENTS & TAG.
     *
     * @param tag   specific the type of the edge entity {@code Model.E}.
     * @param edges specific the edges entity{@code Edge#src, Edge#dst, Edge#id, Edge#type}.
     * @param <E>   specific the type of the edge entity {@code Model.E}.
     * @return the basic fetching condition wrapper of the specified edges basic props & tag as origin.
     */
    @Contract("_, _ -> new")
    @SafeVarargs
    public static <E extends Model.E> @NotNull Fetch<E> edge(Class<E> tag, E... edges) {
        return new FetchEdgeWrapper<>(tag, edges, fetchE);
    }

    /**
     * A domain specific condition wrapper for fetch edge by the related ELEMENTS & TAG.
     *
     * @param tag   specific the type of the edge entity {@code Model.E}.
     * @param edges specific the edges entity{@code Edge#src, Edge#dst, Edge#id, Edge#type}.
     * @param <E>   specific the type of the edge entity {@code Model.E}.
     * @return the basic fetching condition wrapper of the specified edges basic props & tag as origin.
     */
    @Contract("_, _ -> new")
    public static <E extends Model.E> @NotNull Fetch<E> edge(Class<E> tag, Collection<E> edges) {
        return new FetchEdgeWrapper<>(tag, edges, fetchE);
    }

    /**
     * A domain specific condition wrapper for fetch edge by the related ELEMENTS & TAG.
     *
     * @param tag   specific the type of the edge entity {@code Model.E}.
     * @param edges specific the edges entity{@code Edge#src, Edge#dst, Edge#id, Edge#type}.
     * @param <E>   specific the type of the edge entity {@code Model.E}.
     * @return the basic fetching condition wrapper of the specified edges basic props & tag as origin.
     */
    @Contract("_, _ -> new")
    @SafeVarargs
    public static <E extends Model.E> @NotNull Fetch<E> edge(String tag, E... edges) {
        return new FetchEdgeWrapper<>(tag, edges, fetchE);
    }

    /**
     * A domain specific condition wrapper for fetch edge by the related ELEMENTS & TAG.
     *
     * @param tag   specific the type of the edge entity {@code Model.E}.
     * @param edges specific the edges entity{@code Edge#src, Edge#dst, Edge#id, Edge#type}.
     * @param <E>   specific the type of the edge entity {@code Model.E}.
     * @return the basic fetching condition wrapper of the specified edges basic props & tag as origin.
     */
    @Contract("_, _ -> new")
    public static <E extends Model.E> @NotNull Fetch<E> edge(String tag, Collection<E> edges) {
        return new FetchEdgeWrapper<>(tag, edges, fetchE);
    }

    static final class FetchVertexWrapper<V extends Model.V> extends FetchWrapper<V> {
        <R> FetchVertexWrapper(String label, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(label, vertices, renderFunc, it -> it.setV(true).setInstantiatedLabel(label));
        }

        <R> FetchVertexWrapper(Class<V> labelClazz, R vertices, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(labelClazz, vertices, renderFunc, it -> it.setV(true).setInstantiatedLabelClazz(labelClazz));
        }
    }
    static final class FetchEdgeWrapper<E extends Model.E> extends FetchWrapper<E> {
        <R> FetchEdgeWrapper(String label, R edges, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(label, edges, renderFunc, it -> it.setE(true).setInstantiatedLabel(label));
        }

        <R> FetchEdgeWrapper(Class<E> labelClazz, R edges, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(labelClazz, edges, renderFunc, it -> it.setE(true).setInstantiatedLabelClazz(labelClazz));
        }
    }
    static final class MatchVertexWrapper<V extends Model.V> extends MatchWrapper<V> {
        MatchVertexWrapper(String label, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(label, renderFunc, it -> it.setV(true).setInstantiatedLabel(label));
        }
        MatchVertexWrapper(Class<V> labelClazz, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(labelClazz, renderFunc, it -> it.setV(true).setInstantiatedLabelClazz(labelClazz));
        }
    }
    static final class MatchEdgeWrapper<E extends Model.E> extends MatchWrapper<E> {
        MatchEdgeWrapper(String label, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(label, renderFunc, it -> it.setE(true).setInstantiatedLabel(label));
        }

        MatchEdgeWrapper(Class<E> labelClazz, BiFunction<MapleDslConfiguration, Object[], String> renderFunc) {
            super(labelClazz, renderFunc, it -> it.setE(true).setInstantiatedLabelClazz(labelClazz));
        }
    }
    static final class TraversalWrapperFacade extends TraversalWrapper {
        /**
         * Traversal the vertices#ID of the graph.
         *
         * @param from       {@link Model.V#id()}
         */
        <R> TraversalWrapperFacade(R from) {
            super(from, traversal);
        }
    }
}