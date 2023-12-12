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
import static com.mapledsl.core.MapleDslDialectRenderHelper.BLANK;
import static com.mapledsl.core.MapleDslDialectRenderHelper.COMMA;

public final class G {

    /**
     * Traverses through the given vertex ids and returns a TraversalWrapper object.
     *
     * @param vertexIds the ids of the vertices to traverse
     * @return a TraversalWrapper object representing the traversal through the vertex ids
     */
    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(String... vertexIds) {
        return traverse(Arrays.stream(vertexIds));
    }

    /**
     * Traverses through the given vertex ids and returns a TraversalWrapper object.
     *
     * @param vertexIds the ids of the vertices to traverse
     * @return a TraversalWrapper object representing the traversal through the vertex ids
     */
    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(Number... vertexIds) {
        return traverse(Arrays.stream(vertexIds).mapToLong(Number::longValue));
    }

    /**
     * Traverses through the given vertex ids in the provided stream and returns a TraversalWrapper object.
     *
     * @param vertexIdStream the stream of vertex ids to traverse
     * @return a TraversalWrapper object representing the traversal through the vertex ids
     */
    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(Stream<String> vertexIdStream) {
        return new TraversalWrapperFacade(vertexIdStream
                .map(MapleDslDialectRenderHelper::quote)
                .collect(Collectors.joining(COMMA)));
    }

    /**
     * Traverses through the given vertex ids in the provided stream and returns a TraversalWrapper object.
     *
     * @param vertexIdStream the stream of vertex ids to traverse
     * @return a TraversalWrapper object representing the traversal through the vertex ids
     */
    @Contract("_ -> new")
    public static @NotNull TraversalWrapper traverse(LongStream vertexIdStream) {
        return new TraversalWrapperFacade(vertexIdStream
                .mapToObj(MapleDslDialectRenderHelper::numeric)
                .collect(Collectors.joining(COMMA)));
    }

    /**
     * Traverses through the given MatchWrapper object and returns a TraversalWrapper object.
     *
     * @param match the MatchWrapper object containing the vertices to traverse
     * @return a TraversalWrapper object representing the traversal through the vertices
     */
    @Contract("_ -> new")
    public static TraversalWrapper traverse(MatchWrapper<? extends Model.V<?>> match) {
        return new TraversalWrapperFacade(((MatchVertexWrapper<?>) match));
    }

    /**
     * Returns a MatchWrapper object representing a vertex match with the specified tag.
     *
     * @param tag the class representing the tag of the vertex
     * @param <V> the type of the vertex entity
     * @return a MatchWrapper object representing the vertex match
     */
    @Contract("_ -> new")
    public static <V extends Model.V<?>> @NotNull MatchWrapper<V> vertex(Class<V> tag) {
        return new MatchVertexWrapper<>(matchV, tag);
    }

    /**
     * Creates a MatchWrapper object representing a vertex match with the specified tag.
     *
     * @param tag the class representing the tag of the vertex
     * @return a MatchWrapper object representing the vertex match
     */
    @Contract("_ -> new")
    public static @NotNull MatchWrapper<Model.V<?>> vertex(String tag) {
        return new MatchVertexWrapper<>(matchV, tag);
    }

    /**
     * Returns a MatchWrapper object representing an edge match with the specified tag.
     *
     * @param tag the class representing the tag of the edge
     * @param <E> the type of the edge entity
     * @return a MatchWrapper object representing the edge match
     */
    @Contract("_ -> new")
    public static <E extends Model.E<?>> @NotNull MatchWrapper<E> edge(Class<E> tag) {
        return new MatchEdgeWrapper<>(matchE, tag);
    }

    /**
     * Returns a MatchWrapper object representing an edge match with the specified tag.
     *
     * @param tag the class representing the tag of the edge
     * @param <E> the type of the edge entity
     * @return a MatchWrapper object representing the edge match
     */
    @Contract("_ -> new")
    public static <E extends Model.E<?>> @NotNull MatchWrapper<E> edge(String tag) {
        return new MatchEdgeWrapper<>(matchE, tag);
    }

    /**
     * Creates a FetchWrapper object to fetch vertices with the given tag and vertex IDs.
     *
     * @param tag       the tag representing the vertex entity
     * @param vertexIds the IDs of the vertices to fetch
     * @return a FetchWrapper object representing the fetching condition for the specified vertex IDs and tag
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <V extends Model.V<?>> @NotNull FetchWrapper<V> vertex(String tag, String... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds));
    }

    /**
     * Creates a FetchWrapper object to fetch vertices with the given tag and vertex IDs.
     *
     * @param tag the tag representing the vertex entity
     * @param vertexIds the IDs of the vertices to fetch
     * @return a FetchWrapper object representing the fetching condition for the specified vertex IDs and tag
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V<?>> vertex(String tag, Number... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds).mapToLong(Number::longValue));
    }

    /**
     * Creates a FetchWrapper instance for querying vertex data.
     *
     * @param tag             the tag associated with the vertex
     * @param vertexIdStream  the stream of vertex IDs
     * @return a FetchWrapper instance that wraps the vertex query
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V<?>> vertex(String tag, LongStream vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .mapToObj(MapleDslDialectRenderHelper::numeric)
                .collect(Collectors.joining(COMMA))
        );
    }

    /**
     * Returns a FetchWrapper object for the given vertex.
     *
     * @param tag              the tag associated with the vertex
     * @param vertexIdStream   a stream of vertex IDs
     * @return a FetchWrapper object for the given vertex
     * @throws NullPointerException if either tag or vertexIdStream is null
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <V extends Model.V<?>> @NotNull FetchWrapper<V> vertex(String tag, Stream<String> vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .map(MapleDslDialectRenderHelper::quote)
                .collect(Collectors.joining(COMMA))
        );
    }

    /**
     * Creates a FetchWrapper object representing a vertex in a graph database.
     *
     * @param tag        the tag representing the vertex class type.
     * @param vertexIds  the array of vertex IDs.
     * @param <V>        the type of vertex class.
     * @return a FetchWrapper object representing the vertex.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <V extends Model.V<?>> @NotNull FetchWrapper<V> vertex(Class<V> tag, String... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds));
    }

    /**
     * Creates a FetchWrapper object representing a vertex in a graph database.
     *
     * @param tag        the tag representing the vertex class type.
     * @param vertexIds  the array of vertex IDs.
     * @param <V>        the type of vertex class.
     * @return a FetchWrapper object representing the vertex.
     */
    @Contract("_, _ -> new")
    public static <V extends Model.V<?>> @NotNull FetchWrapper<V> vertex(Class<V> tag, Number... vertexIds) {
        return vertex(tag, Arrays.stream(vertexIds).mapToLong(Number::longValue));
    }

    /**
     * Fetches vertices of the specified class with the given vertex IDs.
     *
     * @param tag The class of vertices to fetch.
     * @param vertexIdStream A stream of vertex IDs to fetch.
     * @return A FetchWrapper instance representing the fetched vertices.
     */
    @Contract("_, _ -> new")
    public static <V extends Model.V<?>> @NotNull FetchWrapper<V> vertex(Class<V> tag, LongStream vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .mapToObj(MapleDslDialectRenderHelper::numeric)
                .collect(Collectors.joining(COMMA))
        );
    }

    /**
     * Creates a new instance of {@link FetchWrapper} with the given class tag and vertex ID stream.
     *
     * @param tag The class tag for the vertex type.
     * @param vertexIdStream The stream of vertex IDs.
     * @param <V> The type of the vertex.
     * @return A new instance of {@link FetchWrapper} for the given vertex type.
     * @throws NullPointerException if any of the arguments are null.
     */
    @Contract("_, _ -> new")
    public static <V extends Model.V<?>> @NotNull FetchWrapper<V> vertex(Class<V> tag, Stream<String> vertexIdStream) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIdStream
                .map(MapleDslDialectRenderHelper::quote)
                .collect(Collectors.joining(COMMA))
        );
    }

    /**
     * Creates an instance of FetchWrapper for fetching edges.
     *
     * @param tag   the class representing the edge
     * @param edges the array of edges
     * @param <E>   the type of the edge
     * @return a new instance of FetchWrapper for fetching edges
     */
    @Contract("_, _ -> new")
    public static <E extends Model.E<?>> @NotNull FetchWrapper<E> edge(Class<E> tag, Model.E<?>... edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
    }

    /**
     * Creates a new {@link FetchWrapper} for the given {@link Model.E} class and collection of edges.
     * This method returns a new instance of {@link FetchEdgeWrapper}.
     *
     * @param tag The {@link Class} object representing the {@link Model.E} class.
     * @param edges The collection of {@link Model.E} edges.
     * @return A new instance of {@link FetchWrapper} wrapping the given {@link Model.E} edges.
     * @throws IllegalArgumentException if tag is null.
     */
    @Contract("_, _ -> new")
    public static <E extends Model.E<?>> @NotNull FetchWrapper<E> edge(Class<E> tag, Collection<Model.E<?>> edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
    }

    /**
     * This method is used to create an instance of FetchWrapper with FetchEdgeWrapper implementation.
     *
     * @param tag   the tag to be passed to FetchEdgeWrapper constructor
     * @param edges the edges to be passed to FetchEdgeWrapper constructor
     * @param <E>   the type of Model.E
     * @return an instance of FetchWrapper with FetchEdgeWrapper implementation
     */
    @Contract("_, _ -> new")
    public static <E extends Model.E<?>> @NotNull FetchWrapper<E> edge(String tag, Model.E<?>... edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
    }

    /**
     * Creates a new FetchWrapper instance with the given tag and collection of edges.
     *
     * @param tag   the tag for the FetchWrapper instance
     * @param edges the collection of edges to be included in the FetchWrapper instance
     * @return a new FetchWrapper instance
     */
    @Contract("_, _ -> new")
    public static <E extends Model.E<?>> @NotNull FetchWrapper<E> edge(String tag, Collection<Model.E<?>> edges) {
        return new FetchEdgeWrapper<>(fetchE, tag, edges);
    }

    /**
     * A wrapper class for fetching vertices in a graph.
     *
     * @param <V> The type of the vertices in the graph.
     */
    static final class FetchVertexWrapper<V extends Model.V<?>> extends FetchWrapper<V> {
        /**
         * Fetches vertices in a graph.
         *
         * @param <R> The type of the vertices in the graph.
         * @param renderFunc The function that defines how to render the vertex.
         * @param label The label of the vertex.
         * @param vertices The vertices to fetch.
         */
        <R> FetchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label, R vertices) {
            super(DEFAULT_VERTEX_ALIAS, label, vertices, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS))
            ;
        }

        /**
         * FetchVertexWrapper is a generic class that represents a wrapper for fetching vertices in a graph.
         *
         * @param <R> The type of the vertices in the graph.
         */
         <R> FetchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<V> labelClazz, R vertices) {
            super(DEFAULT_VERTEX_ALIAS, labelClazz, vertices, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS)
            );
        }
    }

    /**
     * The FetchEdgeWrapper class is a static final class that extends the FetchWrapper class.
     * It represents a wrapper for fetching edges in a graph database.
     *
     * @param <E> The type of the model.
     */
    static final class FetchEdgeWrapper<E extends Model.E<?>> extends FetchWrapper<E> {
        /**
         * FetchEdgeWrapper represents a wrapper for fetching edges in a graph database.
         *
         * @param <R> The type of the edges.
         * @param renderFunc The function used to render the fetch query.
         * @param label The label of the edges.
         * @param edges The edges to fetch.
         */
        <R> FetchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label, R edges) {
            super(DEFAULT_EDGE_ALIAS, label, edges, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }

        /**
         * FetchEdgeWrapper is a class that represents a wrapper for fetching edges in a graph database.
         *
         * @param <R> The type of the edges.
         */
        <R> FetchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<E> labelClazz, R edges) {
            super(DEFAULT_EDGE_ALIAS, labelClazz, edges, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }
    }

    /**
     * A wrapper class that represents a matched vertex in a graph query.
     *
     * @param <V> The type of the vertex model.
     */
    static final class MatchVertexWrapper<V extends Model.V<?>> extends MatchWrapper<V> {
        /**
         * Creates a MatchVertexWrapper object.
         *
         * @param renderFunc A BiFunction that takes a MapleDslConfiguration object and an array of Objects as arguments and returns a String.
         * @param label The label for the vertex.
         */
        MatchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label) {
            super(DEFAULT_VERTEX_ALIAS, label, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS)
            );
        }

        /**
         * Creates a MatchVertexWrapper object.
         *
         * @param renderFunc A BiFunction that takes a MapleDslConfiguration object and an array of Objects as arguments and returns a String.
         * @param labelClazz The class representing the label for the vertex.
         */
        MatchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<V> labelClazz) {
            super(DEFAULT_VERTEX_ALIAS, labelClazz, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS)
            );
        }

        /**
         * Sets the arguments for traversing and returns the updated MatchVertexWrapper object.
         *
         * @return The updated MatchVertexWrapper object.
         */
        MatchVertexWrapper<V> asTraversal() {
            super.arguments[TRAVERSE_INDEX] = true;
            return this;
        }

        /**
         * Sets the delete flag for the vertex and returns the updated MatchVertexWrapper object.
         *
         * @param withEdge true if the delete flag for the vertex should be set along with detach flag for the edge, false otherwise
         * @return The updated MatchVertexWrapper object
         */
        MatchVertexWrapper<V> asDelete(boolean withEdge) {
            if (withEdge) super.arguments[DETACH_INDEX] = true;
            else super.arguments[DELETE_INDEX] = true;
            return this;
        }
    }

    /**
     * A wrapper class for matching edges in a graph. Extends the {@link MatchWrapper} class.
     *
     * @param <E> the type of the edge model
     */
    static final class MatchEdgeWrapper<E extends Model.E<?>> extends MatchWrapper<E> {
        /**
         * Constructs a new MatchEdgeWrapper object.
         *
         * @param renderFunc the bi-function used for rendering the edge
         * @param label      the label of the edge
         */
        MatchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label) {
            super(DEFAULT_EDGE_ALIAS, label, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }

        /**
         * Constructs a new MatchEdgeWrapper object.
         *
         * @param renderFunc the bi-function used for rendering the edge.
         * @param labelClazz the class representing the label of the edge.
         */
        MatchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<E> labelClazz) {
            super(DEFAULT_EDGE_ALIAS, labelClazz, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabelClazz(labelClazz)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }

        /**
         * Sets the 'delete' flag to true for the current edge.
         *
         * @return the updated MatchEdgeWrapper object
         */
        MatchEdgeWrapper<E> asDelete() {
            super.arguments[DELETE_INDEX] = true;
            return this;
        }
    }

    /**
     * This class is a wrapper facade for TraversalStepWrapper. It extends the TraversalStepWrapper class
     * and provides additional functionality for rendering a traversal.
     * <p></p>
     * It has two constructors, one that takes a fromFragment as an argument and another that takes a
     * MatchVertexWrapper object as an argument.
     * <p></p>
     * The render method is overridden to include the rendering of the match if it is not null.
     */
    static final class TraversalWrapperFacade extends TraversalStepWrapper {
        /**
         * This variable represents a matched vertex in a graph query.
         */
        MatchVertexWrapper<?> match;

        /**
         * This class is a wrapper facade for TraversalStepWrapper. It extends the TraversalStepWrapper class
         * and provides additional functionality for rendering a traversal.
         */
        TraversalWrapperFacade(String fromFragment) {
            super(traversal, fromFragment);
        }

        /**
         * This class is a wrapper facade for TraversalStepWrapper. It extends the TraversalStepWrapper class
         * and provides additional functionality for rendering a traversal.
         */
        TraversalWrapperFacade(MatchVertexWrapper<? extends Model.V<?>> match) {
            super(traversal);
            this.match = match.asTraversal();
        }

        @Override
        public String render(MapleDslConfiguration configuration) {
            return Objects.isNull(match) ? super.render(configuration) : match.render(configuration) + BLANK + super.render(configuration);
        }
    }
}
