package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.FetchWrapper;
import com.mapledsl.core.condition.wrapper.MatchWrapper;
import com.mapledsl.core.condition.wrapper.TraversalStepWrapper;
import com.mapledsl.core.condition.wrapper.TraversalWrapper;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.mapledsl.core.MapleDslDialectRender.*;
import static com.mapledsl.core.MapleDslDialectRenderHelper.COMMA;

public final class G {

    /**
     * A domain specific condition wrapper for traversing a graph using "graph concepts" (e.g. vertices, edges).
     * Represents a directed walk over a graph {@code Graph}.
     *
     * @param vertexId specific the id of vertex {@code Vertex}.
     * @return the basic traversing condition wrapper of the specific id of vertex as origin.
     */
    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(String vertexId) {
        return new TraversalWrapperFacade(vertexId);
    }

    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(Number vertexId) {
        return new TraversalWrapperFacade(vertexId);
    }

    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(String... vertexIds) {
        return new TraversalWrapperFacade(vertexIds);
    }

    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(Number... vertexIds) {
        return new TraversalWrapperFacade(vertexIds);
    }

    @Contract("_ -> new")
    public static TraversalWrapper traverse(MatchWrapper<? extends Model.V> match) {
        return new TraversalWrapperFacade(((MatchVertexWrapper<?>) match));
    }

    /**
     * A domain specific condition wrapper for fetch vertex by the related VID & TAG.
     *
     * @param tag specific the tag of the vertex entity {@code Model.V}.
     * @param <V> specific the type of the vertex entity {@code Model.V}.
     * @return the basic fetching condition wrapper of the specified id of vertex & tag as origin.
     */
    @Contract("_ -> new")
    public static <V extends Model.V> @NotNull MatchWrapper<V> vertex(Class<V> tag) {
        return new MatchVertexWrapper<>(matchV, tag);
    }

    @Contract("_ -> new")
    public static @NotNull MatchWrapper<Model.V> vertex(String tag) {
        return new MatchVertexWrapper<>(matchV, tag);
    }

    @Contract("_ -> new")
    public static <E extends Model.E> @NotNull MatchWrapper<E> edge(Class<E> tag) {
        return new MatchEdgeWrapper<>(matchE, tag);
    }

    @Contract("_ -> new")
    public static <E extends Model.E> @NotNull MatchWrapper<E> edge(String tag) {
        return new MatchEdgeWrapper<>(matchE, tag);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V> vertex(String tag, String... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V> vertex(String tag, Number... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds).mapToLong(Number::longValue));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V> vertex(String tag, LongStream vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .mapToObj(MapleDslDialectRenderHelper::numeric)
                .collect(Collectors.joining(COMMA))
        );
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V> vertex(String tag, Stream<String> vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .map(MapleDslDialectRenderHelper::escaped)
                .collect(Collectors.joining(COMMA))
        );
    }

    /**
     * A domain specific condition wrapper for fetch vertex by the related VID & TAG.
     *
     * @param tag       specific the tag of the vertex entity {@code Model.V}.
     * @param vertexIds specific the id of the vertex {@code Vertex}.
     * @param <V>       specific the type of the vertex entity {@code Model.V}.
     * @return the basic fetching condition wrapper of the specified id of vertex & tag as origin.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <V extends Model.V> @NotNull FetchWrapper<V> vertex(Class<V> tag, String... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds));
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
    public static <V extends Model.V> @NotNull FetchWrapper<V> vertex(Class<V> tag, Number... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds).mapToLong(Number::longValue));
    }

    @Contract("_, _ -> new")
    public static <V extends Model.V> @NotNull FetchWrapper<V> vertex(Class<V> tag, LongStream vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .mapToObj(MapleDslDialectRenderHelper::numeric)
                .collect(Collectors.joining(COMMA))
        );
    }

    @Contract("_, _ -> new")
    public static <V extends Model.V> @NotNull FetchWrapper<V> vertex(Class<V> tag, Stream<String> vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .map(MapleDslDialectRenderHelper::escaped)
                .collect(Collectors.joining(COMMA))
        );
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
    public static <E extends Model.E> @NotNull FetchWrapper<E> edge(Class<E> tag, Model.E... edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
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
    public static <E extends Model.E> @NotNull FetchWrapper<E> edge(Class<E> tag, Collection<Model.E> edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
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
    public static <E extends Model.E> @NotNull FetchWrapper<E> edge(String tag, Model.E... edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
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
    public static <E extends Model.E> @NotNull FetchWrapper<E> edge(String tag, Collection<Model.E> edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
    }

    static final class FetchVertexWrapper<V extends Model.V> extends FetchWrapper<V> {
        <R> FetchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label, R vertices) {
            super(DEFAULT_VERTEX_ALIAS, label, vertices, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS))
            ;
        }

        <R> FetchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<V> labelClazz, R vertices) {
            super(DEFAULT_VERTEX_ALIAS, labelClazz, vertices, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS)
            );
        }
    }

    static final class FetchEdgeWrapper<E extends Model.E> extends FetchWrapper<E> {
        <R> FetchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label, R edges) {
            super(DEFAULT_EDGE_ALIAS, label, edges, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }

        <R> FetchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<E> labelClazz, R edges) {
            super(DEFAULT_EDGE_ALIAS, labelClazz, edges, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }
    }

    static final class MatchVertexWrapper<V extends Model.V> extends MatchWrapper<V> {
        MatchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label) {
            super(DEFAULT_VERTEX_ALIAS, label, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS)
            );
        }

        MatchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<V> labelClazz) {
            super(DEFAULT_VERTEX_ALIAS, labelClazz, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS)
            );
        }

        MatchVertexWrapper<V> asTraversal() {
            super.arguments[TRAVERSE_INDEX] = true;
            return this;
        }

        MatchVertexWrapper<V> asDelete(boolean withEdge) {
            if (withEdge) super.arguments[DETACH_INDEX] = true;
            else super.arguments[DELETE_INDEX] = true;
            return this;
        }
    }

    static final class MatchEdgeWrapper<E extends Model.E> extends MatchWrapper<E> {
        MatchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label) {
            super(DEFAULT_EDGE_ALIAS, label, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }

        MatchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<E> labelClazz) {
            super(DEFAULT_EDGE_ALIAS, labelClazz, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }

        MatchEdgeWrapper<E> asDelete() {
            super.arguments[DELETE_INDEX] = true;
            return this;
        }
    }

    static final class TraversalWrapperFacade extends TraversalStepWrapper {
        MatchVertexWrapper<?> match;

        <R> TraversalWrapperFacade(R from) {
            super(from, traversal);
        }

        TraversalWrapperFacade(MatchVertexWrapper<? extends Model.V> match) {
            super(DEFAULT_VERTEX_ALIAS, traversal);
            this.match = match.asTraversal();
        }

        @Override
        public String render(MapleDslConfiguration configuration) {
            return Objects.isNull(match) ? super.render(configuration)
                    : match.render(configuration) + super.render(configuration);
        }
    }
}
