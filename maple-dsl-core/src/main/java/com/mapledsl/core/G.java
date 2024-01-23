package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.FetchWrapper;
import com.mapledsl.core.condition.wrapper.MatchWrapper;
import com.mapledsl.core.condition.wrapper.TraversalStepWrapper;
import com.mapledsl.core.condition.wrapper.TraversalWrapper;
import com.mapledsl.core.model.ID;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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
    @SafeVarargs
    @Contract("_ -> new")
    public static <ID> @NotNull TraversalWrapper traverse(ID... vertexIds) {
        return new TraversalWrapperFacade(Arrays.stream(vertexIds)
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA)));
    }

    /**
     * Traverses through the given vertex ids and returns a TraversalWrapper object.
     *
     * @param vertexIds the ids of the vertices to traverse
     * @return a TraversalWrapper object representing the traversal through the vertex ids
     */
    @Contract("_ -> new")
    public static <ID> @NotNull TraversalWrapper traverse(Collection<ID> vertexIds) {
        return new TraversalWrapperFacade(vertexIds.stream()
                .map(MapleDslDialectRenderHelper::identify)
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
    public static <E extends Model.E<?,?>> @NotNull MatchWrapper<E> edge(Class<E> tag) {
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
    public static <E extends Model.E<?,?>> @NotNull MatchWrapper<E> edge(String tag) {
        return new MatchEdgeWrapper<>(matchE, tag);
    }

    /**
     * Returns a FetchWrapper object for the given vertex.
     *
     * @param tag              the tag associated with the vertex
     * @param vertexIds        the array of vertex IDs.
     * @return a FetchWrapper  object for the given vertex
     * @throws NullPointerException if either tag or vertexIdStream is null
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V<String>> vertex(String tag, String... vertexIds) {
        return new FetchVertexWrapper<>(fetchV, tag, Arrays.stream(vertexIds)
                .map(MapleDslDialectRenderHelper::quote)
                .collect(Collectors.joining(COMMA)));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V<Number>> vertex(String tag, Number... vertexIds) {
        return new FetchVertexWrapper<>(fetchV, tag, Arrays.stream(vertexIds)
                .map(MapleDslDialectRenderHelper::numeric)
                .collect(Collectors.joining(COMMA)));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull FetchWrapper<Model.V<String>> vertex(String tag, ID... vertexIds) {
        return new FetchVertexWrapper<>(fetchV, tag, Arrays.stream(vertexIds)
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA)));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <ID> @NotNull FetchWrapper<Model.V<ID>> vertex(String tag, Collection<ID> vertexIds) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIds.stream()
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA)));
    }

    /**
     * Creates a FetchWrapper object representing a vertex in a graph database.
     *
     * @param tag        the tag representing the vertex class type.
     * @param vertexIds  the array of vertex IDs.
     * @param <V>        the type of vertex class.
     * @return a FetchWrapper object representing the vertex.
     */
    @SafeVarargs
    @Contract("_, _ -> new")
    public static <ID, V extends Model.V<ID>> @NotNull FetchWrapper<V> vertex(Class<V> tag, ID... vertexIds) {
        return new FetchVertexWrapper<>(fetchV, tag, Arrays.stream(vertexIds)
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA)));
    }

    @Contract("_, _ -> new")
    public static <ID, V extends Model.V<ID>> @NotNull FetchWrapper<V> vertex(Class<V> tag, Collection<ID> vertexIds) {
        return new FetchVertexWrapper<>(fetchV, tag, vertexIds.stream()
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA)));
    }

    /**
     * Creates an instance of FetchWrapper for fetching edges.
     *
     * @param tag   the class representing the edge
     * @param edgeIds the array of edges
     * @param <E>   the type of the edge
     * @return a new instance of FetchWrapper for fetching edges
     */
    @SafeVarargs
    @Contract("_, _ -> new")
    public static <ID, E extends Model.E<ID,?>> @NotNull FetchWrapper<E> edge(Class<E> tag, ID... edgeIds) {
        return new FetchEdgeWrapper<>(fetchE, tag, Arrays.stream(edgeIds)
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA))
        );
    }

    /**
     * Creates a new {@link FetchWrapper} for the given {@link Model.E} class and collection of edges.
     * This method returns a new instance of {@link FetchEdgeWrapper}.
     *
     * @param tag The {@link Class} object representing the {@link Model.E} class.
     * @param edgeIds The collection of {@link Model.E} edges.
     * @return A new instance of {@link FetchWrapper} wrapping the given {@link Model.E} edges.
     * @throws IllegalArgumentException if tag is null.
     */
    @Contract("_, _ -> new")
    public static <ID, E extends Model.E<ID,?>> @NotNull FetchWrapper<E> edge(Class<E> tag, Collection<ID> edgeIds) {
        return new FetchEdgeWrapper<>(fetchE, tag, edgeIds.stream()
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA))
        );
    }

    /**
     * This method is used to create an instance of FetchWrapper with FetchEdgeWrapper implementation.
     *
     * @param tag   the tag to be passed to FetchEdgeWrapper constructor
     * @param edgeIds the edges to be passed to FetchEdgeWrapper constructor
     * @return an instance of FetchWrapper with FetchEdgeWrapper implementation
     */
    @Contract("_, _ -> new")
    public static @NotNull FetchWrapper<Model.E<String,?>> edge(String tag, String... edgeIds) {
        return new FetchEdgeWrapper<>(fetchE, tag, Arrays.stream(edgeIds)
                .map(MapleDslDialectRenderHelper::quote)
                .collect(Collectors.joining(COMMA))
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull FetchWrapper<Model.E<Number,?>> edge(String tag, Number... edgeIds) {
        return new FetchEdgeWrapper<>(fetchE, tag, Arrays.stream(edgeIds)
                .map(MapleDslDialectRenderHelper::numeric)
                .collect(Collectors.joining(COMMA))
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull FetchWrapper<Model.E<String,?>> edge(String tag, ID... edgeIds) {
        return new FetchEdgeWrapper<>(fetchE, tag, Arrays.stream(edgeIds)
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA))
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull <ID> FetchWrapper<Model.E<ID,?>> edge(String tag, Collection<ID> edgeIds) {
        return new FetchEdgeWrapper<>(fetchE, tag, edgeIds.stream()
                .map(MapleDslDialectRenderHelper::identify)
                .collect(Collectors.joining(COMMA))
        );
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
         * @param renderFunc The function that defines how to render the vertex.
         * @param label The label of the vertex.
         * @param verticesFragment The vertices fragment of fetch statement.
         */
        FetchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label, String verticesFragment) {
            super(DEFAULT_VERTEX_ALIAS, label, verticesFragment, renderFunc, it -> it
                    .setV(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_VERTEX_ALIAS))
            ;
        }

        /**
         * FetchVertexWrapper is a generic class that represents a wrapper for fetching vertices in a graph.
         */
         FetchVertexWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<V> labelClazz, String verticesFragment) {
            super(DEFAULT_VERTEX_ALIAS, labelClazz, verticesFragment, renderFunc, it -> it
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
    static final class FetchEdgeWrapper<E extends Model.E<?,?>> extends FetchWrapper<E> {
        /**
         * FetchEdgeWrapper represents a wrapper for fetching edges in a graph database.
         *
         * @param <R> The type of the edges.
         * @param renderFunc The function used to render the fetch query.
         * @param label The label of the edges.
         * @param edgeFragment The edges fetch fragment.
         */
        <R> FetchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, String label, String edgeFragment) {
            super(DEFAULT_EDGE_ALIAS, label, edgeFragment, renderFunc, it -> it
                    .setE(true)
                    .setInstantiatedLabel(label)
                    .setInstantiatedAlias(DEFAULT_EDGE_ALIAS)
            );
        }

        /**
         * FetchEdgeWrapper is a class that represents a wrapper for fetching edges in a graph database.
         *
         * @param <R> The type of the edges.
         * @param renderFunc the function used to render the fetch query
         * @param labelClazz the label clazz of the edges.
         * @param edgeFragment The edge fetch fragment.
         */
        <R> FetchEdgeWrapper(BiFunction<MapleDslConfiguration, Object[], String> renderFunc, Class<E> labelClazz, String edgeFragment) {
            super(DEFAULT_EDGE_ALIAS, labelClazz, edgeFragment, renderFunc, it -> it
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
    static final class MatchEdgeWrapper<E extends Model.E<?,?>> extends MatchWrapper<E> {
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
