package com.mapledsl.core.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * The MapleDslSessionTemplate class represents a session template for executing Maple DSL queries.
 * It implements the MapleDslSession and MapleDslSessionFactory interfaces.
 *
 * @see MapleDslSession
 * @see MapleDslSessionFactory
 */
public class MapleDslSessionTemplate implements MapleDslSession, MapleDslSessionFactory {
    private final MapleDslSession sessionProxy;
    private final MapleDslSessionFactory sessionFactory;

    private MapleDslSessionTemplate(MapleDslSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.sessionProxy = (MapleDslSession) Proxy.newProxyInstance(MapleDslSession.class.getClassLoader(), new Class[]{ MapleDslSession.class }, new MapleDslSessionInterceptor());
    }

    public static MapleDslSessionTemplate newInstance(MapleDslSessionFactory sessionFactory) {
        return new MapleDslSessionTemplate(sessionFactory);
    }

    /**
     * Selects a vertex from the graph based on the provided statement.
     *
     * @param stmt the statement used to select the vertex from the graph
     * @return the selected vertex as a Model.V object, or null if no vertex is found
     */
    @Override
    public <ID> Model.@Nullable V<ID> selectVertex(@NotNull String stmt) {
        return sessionProxy.selectVertex(stmt);
    }

    /**
     * Selects a list of vertices from the graph based on the provided statement.
     *
     * @param stmt the statement used to select the vertices from the graph
     * @return the selected vertices as a list of Model.V objects, or an empty list if no vertices are found
     */
    @Override
    public <ID> @NotNull List<Model.V<ID>> selectVertexList(@NotNull String stmt) {
        return sessionProxy.selectVertexList(stmt);
    }

    /**
     * Selects an edge from the graph based on the provided statement.
     *
     * @param stmt the statement used to select the edge from the graph
     * @return the selected edge as a Model.E object, or null if no edge is found
     */
    @Override
    public <ID, R> Model.@Nullable E<ID, R> selectEdge(@NotNull String stmt) {
        return sessionProxy.selectEdge(stmt);
    }

    /**
     * Selects a list of edges from the graph based on the provided statement.
     *
     * @param stmt the statement used to select the edges from the graph
     * @return the selected edges as a list of Model.E objects, or an empty list if no edges are found
     */
    @Override
    public <ID, R> @NotNull List<Model.E<ID, R>> selectEdgeList(@NotNull String stmt) {
        return sessionProxy.selectEdgeList(stmt);
    }

    /**
     * Selects a single row mapped from the statement key and parameter.
     *
     * @param stmt              the complete SQL statement
     * @param mappedEntityType  the class representing the entity type to be mapped
     * @param <T>               the type of the entity object returned
     * @return the mapped object of type T, or null if no row is found
     */
    @Override
    public <T> T selectOne(String stmt, Class<T> mappedEntityType) {
        return sessionProxy.selectOne(stmt, mappedEntityType);
    }

    /**
     * Executes a SQL statement and returns a list of mapped objects.
     *
     * @param <T>                the type of the mapped objects in the list
     * @param stmt               the SQL statement to execute
     * @param mappedEntityType   the class representing the entity type to be mapped
     * @return a list of mapped objects of type T
     */
    @Override
    public <T> @NotNull List<T> selectList(String stmt, Class<T> mappedEntityType) {
        return sessionProxy.selectList(stmt, mappedEntityType);
    }

    /**
     * Selects a map of results from the database based on the provided SQL statement.
     *
     * @param stmt the SQL statement used to select the results
     * @return a map containing the selected results as key-value pairs
     */
    @Override
    public @NotNull Map<String, Object> selectMap(@NotNull String stmt) {
        return sessionProxy.selectMap(stmt);
    }

    /**
     * Selects a map of results from the database based on the provided SQL statement.
     *
     * @param stmt the SQL statement used to select the results
     * @return a list of maps containing the selected results as key-value pairs
     */
    @Override
    public @NotNull List<Map<String, Object>> selectMaps(@NotNull String stmt) {
        return sessionProxy.selectMaps(stmt);
    }

    @Override
    public boolean execute(@NotNull String stmt) throws MapleDslException {
        return sessionProxy.execute(stmt);
    }

    @Override
    public void close() {
        sessionProxy.close();
    }

    @Override
    public @NotNull MapleDslConfiguration configuration() {
        return sessionFactory.configuration();
    }

    @Override
    public MapleDslSession openSession() {
        return sessionFactory.openSession();
    }

    /**
     * The MapleDslSessionInterceptor class is an {@link InvocationHandler} that intercepts method invocations
     * on a MapleDslSession proxy. It is responsible for opening a MapleDslSession and invoking the corresponding
     * method on it.
     */
    class MapleDslSessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            try (MapleDslSession autoSession = openSession()) {
                return method.invoke(autoSession, args);
            } catch (Throwable t) {
                throw new MapleDslException(t);
            }
        }
    }
}
