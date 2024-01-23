package com.mapledsl.core.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.condition.Wrapper;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * The MapleDslSession interface represents a session for executing Maple DSL queries.
 * This session is responsible for maintaining the connection to the database and executing the queries.
 *
 * @see Closeable
 */
public interface MapleDslSession extends Closeable {
    /**
     * Select a vertex from the model using the provided SQL statement.
     *
     * @param stmt the SQL statement used to select the vertex
     * @param <ID> the type of the identifier for the vertex
     * @return the selected vertex of type `Model.V`
     */
    @Nullable <ID> Model.V<ID> selectVertex(@NotNull String stmt);
    default @Nullable <ID> Model.V<ID> selectVertex(@NotNull Wrapper<? extends Model.V<?>> stmtWrapper) {
        return selectVertex(stmtWrapper.render(configuration()));
    }

    /**
     * Selects a list of vertices from the model using the provided SQL statement.
     *
     * @param stmt the SQL statement used to select the vertices
     * @param <ID> the type of the identifier for the vertices
     * @return a list of selected vertices of type `Model.V`
     */
    @NotNull <ID> List<Model.V<ID>> selectVertexList(@NotNull String stmt);
    default @NotNull <ID> List<Model.V<ID>> selectVertexList(@NotNull Wrapper<? extends Model.V<?>> stmtWrapper) {
        return selectVertexList(requireNonNull(stmtWrapper).render(configuration()));
    }

    /**
     * Selects an edge from the model using the provided SQL statement.
     *
     * @param stmt the SQL statement used to select the edge
     * @param <ID> the type of the identifier for the edge
     * @param <R> the type of the edge endpoint(src,dst) value
     * @return the selected edge of type Model.E
     */
    @Nullable <ID, R> Model.E<ID, R> selectEdge(@NotNull String stmt);
    default @Nullable <ID, R> Model.E<ID, R> selectEdge(@NotNull Wrapper<? extends Model.E<?,?>> stmtWrapper) {
        return selectEdge(requireNonNull(stmtWrapper).render(configuration()));
    }

    /**
     * Selects a list of edges from the model using the provided SQL statement.
     *
     * @param stmt the SQL statement used to select the edges
     * @param <ID> the type of the identifier for the edges
     * @param <R> the type of the edge endpoint(src,dst) value
     * @return a list of selected edges of type `Model.E`
     */
    @NotNull <ID, R> List<Model.E<ID, R>> selectEdgeList(@NotNull String stmt);
    default @NotNull <ID, R> List<Model.E<ID, R>> selectEdgeList(@NotNull Wrapper<? extends Model.E<?,?>> stmtWrapper) {
        return selectEdgeList(requireNonNull(stmtWrapper).render(configuration()));
    }

    /**
     * Retrieve a single row mapped from the statement key and parameter.
     *
     * @param <T>  the returned model(vertex/edge) type.
     * @param stmt complete sql statement
     * @param mappedEntityType mapped entity type
     * @return optional of mapped object
     */
    @Nullable <T> T selectOne(String stmt, Class<T> mappedEntityType);

    default @Nullable <T> T selectOne(Wrapper<?> stmtWrapper, Class<T> mappedEntityType) {
        return selectOne(requireNonNull(stmtWrapper).render(configuration()), mappedEntityType);
    }

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.
     *
     * @param <T>  the returned model(vertex/edge) type.
     * @param stmt complete sql statement
     * @param mappedEntityType mapped entity type
     * @return List of mapped object
     */
    @NotNull <T> List<T> selectList(String stmt, Class<T> mappedEntityType);

    default @NotNull <T> List<T> selectList(Wrapper<?> stmtWrapper, Class<T> mappedEntityType) {
        return selectList(requireNonNull(stmtWrapper).render(configuration()), mappedEntityType);
    }

    /**
     * The selectMap is a special case in that it is designed to convert a list
     * of results into a Map based on one of the properties in the resulting
     * objects.
     *
     * @param stmt complete sql statement
     * @return Map containing key pair data.
     */
    @NotNull Map<String, Object> selectMap(@NotNull String stmt);

    default @NotNull Map<String, Object> selectMap(@NotNull Wrapper<?> stmtWrapper) {
        return selectMap(requireNonNull(stmtWrapper).render(configuration()));
    }

    /**
     * The selectMaps is a special case in that it is designed to convert a list
     * of results into a Map based on one of the properties in the resulting
     * objects.
     *
     * @param stmt complete sql statement
     * @return containing key pair data.
     */
    @NotNull List<Map<String, Object>> selectMaps(@NotNull String stmt);

    default @NotNull List<Map<String, Object>> selectMaps(@NotNull Wrapper<?> stmtWrapper) {
        return selectMaps(requireNonNull(stmtWrapper).render(configuration()));
    }

    /**
     * Execute a execution statement with the given parameters.
     *
     * @param stmt complete sql statement
     * @return boolean the execution of results affected by the execution.
     */
    boolean execute(@NotNull String stmt) throws MapleDslException;

    default boolean execute(Wrapper<?> stmtWrapper) {
        return execute(requireNonNull(stmtWrapper).render(configuration()));
    }

    /**
     * Closes the session.
     */
    @Override void close();

    /**
     * Retrieves configuration
     *
     * @return configuration
     */
    @NotNull MapleDslConfiguration configuration();
}
