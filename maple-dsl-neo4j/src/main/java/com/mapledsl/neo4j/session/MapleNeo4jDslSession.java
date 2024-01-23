package com.mapledsl.neo4j.session;

import com.google.common.collect.Maps;
import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.session.MapleDslSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.Neo4jException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.neo4j.driver.internal.types.InternalTypeSystem.TYPE_SYSTEM;

/**
 * Represents a session for performing DSL operations on a Neo4j database.
 */
public final class MapleNeo4jDslSession implements MapleDslSession {
    private final Session session;
    /**
     * Represents the context of a Maple DSL session.
     * </p>
     * The MapleDslConfiguration context is used by the MapleNeo4jDslSession class
     * to configure and manage the DSL session.
     */
    private final MapleDslConfiguration context;
    static final Logger LOG = LoggerFactory.getLogger(MapleNeo4jDslSession.class);

    /**
     * Constructs a new MapleNeo4jDslSession.
     *
     * @param session The Neo4j session to be used by the MapleNeo4jDslSession.
     * @param context The MapleDslConfiguration to be used by the MapleNeo4jDslSession.
     */
    public MapleNeo4jDslSession(Session session, MapleDslConfiguration context) {
        this.session = session;
        this.context = context;
    }

    @Override
    public @Nullable <ID> Model.V<ID> selectVertex(@NotNull String stmt) {
        return singleResult(stmt, (values, columns) -> {
            final BeanDefinition<Model.V<ID>> vBeanDefinition = context.vertexDefinition();
            return createModelResult(vBeanDefinition, this::isVertexValue, values, columns);
        });
    }

    @Override
    public @NotNull <ID> List<Model.V<ID>> selectVertexList(@NotNull String stmt) {
        return collectResult(stmt, (values, columns) -> {
            final BeanDefinition<Model.V<ID>> vBeanDefinition = context.vertexDefinition();
            return createModelResult(vBeanDefinition, this::isVertexValue, values, columns);
        });
    }

    @Override
    public @Nullable <ID, R> Model.E<ID, R> selectEdge(@NotNull String stmt) {
        return singleResult(stmt, (values, columns) -> {
            final BeanDefinition<Model.E<ID, R>> eBeanDefinition = context.edgeDefinition();
            return createModelResult(eBeanDefinition, this::isEdgeValue, values, columns);
        });
    }

    @Override
    public @NotNull <ID, R> List<Model.E<ID, R>> selectEdgeList(@NotNull String stmt) {
        return collectResult(stmt, (values, columns) -> {
            final BeanDefinition<Model.E<ID, R>> eBeanDefinition = context.edgeDefinition();
            return createModelResult(eBeanDefinition, this::isEdgeValue, values, columns);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T selectOne(String stmt, Class<T> mappedEntityType) {
        return singleResult(stmt, (values, columns) -> {
            final BeanDefinition<T> definition = context.beanDefinition(mappedEntityType);
            if (definition == null) return context.resultant(values.get(0), mappedEntityType);

            if (Model.V.class.isAssignableFrom(mappedEntityType)) {
                //noinspection rawtypes
                final BeanDefinition<? extends Model.V> vBeanDefinition = (BeanDefinition<? extends Model.V>) definition;
                return (T) createModelResult(vBeanDefinition, this::isVertexValue, values, columns);
            }

            if (Model.E.class.isAssignableFrom(mappedEntityType)) {
                //noinspection rawtypes
                final BeanDefinition<? extends Model.E> eBeanDefinition = (BeanDefinition<? extends Model.E>) definition;
                return (T) createModelResult(eBeanDefinition, this::isEdgeValue, values, columns);
            }

            return createBeanResult(definition, values, columns);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> List<T> selectList(String stmt, Class<T> mappedEntityType) {
        final BeanDefinition<T> definition = context.beanDefinition(mappedEntityType);
        return collectMultiResult(stmt, (values, columns) -> {
            if (definition == null) return createValuesResult(mappedEntityType, values);

            if (Model.V.class.isAssignableFrom(mappedEntityType)) {
                //noinspection rawtypes
                final BeanDefinition<? extends Model.V> vBeanDefinition = (BeanDefinition<? extends Model.V>) definition;
                return Collections.singletonList((T) createModelResult(vBeanDefinition, this::isVertexValue, values, columns));
            }

            if (Model.E.class.isAssignableFrom(mappedEntityType)) {
                //noinspection rawtypes
                final BeanDefinition<? extends Model.E> eBeanDefinition = (BeanDefinition<? extends Model.E>) definition;
                return Collections.singletonList((T) createModelResult(eBeanDefinition, this::isEdgeValue, values, columns));
            }

            return Collections.emptyList();
        });
    }

    @Override
    public @NotNull Map<String, Object> selectMap(@NotNull String stmt) {
        return singleResult(stmt, Maps::newHashMap, this::createMapResult);
    }

    @Override
    public @NotNull List<Map<String, Object>> selectMaps(@NotNull String stmt) {
        return collectResult(stmt, this::createMapResult);
    }

    @Override
    public boolean execute(@NotNull String stmt) throws MapleDslException {
        final Transaction tx = session.beginTransaction();
        if (LOG.isInfoEnabled()) LOG.info("Execution statement:{} with transaction.", stmt);

        try {
            tx.run(stmt);
            tx.commit();
            return true;
        } catch (Neo4jException e) {
            LOG.warn("Execution statement:{}, Error:{}, Reason:{}", stmt, e.code(), e.getMessage());
            tx.rollback();
            return false;
        } catch (Exception e) {
            LOG.warn("Execution statement:{}, UnknownError:{}", stmt, e.getMessage());
            tx.rollback();
            return false;
        }
    }

    @Override
    public void close() {
        if (session == null) return;
        session.close();
    }

    @Override
    public @NotNull MapleDslConfiguration configuration() {
        return context;
    }

    private Result executeQuery(String stmt) throws MapleDslException {
        if (LOG.isInfoEnabled()) LOG.info("Execution statement:{}", stmt);
        try {
            return session.run(stmt);
        } catch (Neo4jException e) {
            LOG.error("Execution statement:{}, Error:{}, Reason:{}", stmt, e.code(), e.getMessage());
            throw new MapleDslExecutionException(e);
        } catch (Exception e) {
            LOG.error("Execution statement:{}, UnknownError:{}", stmt, e.getMessage());
            throw new MapleDslExecutionException(e);
        }
    }

    private <R> R singleResult(String stmt, BiFunction<List<Value>, List<String>, R> recordValueFunc) {
        return singleResult(stmt, () -> null, recordValueFunc);
    }

    private <R> R singleResult(String stmt, Supplier<R> defaultSupplier, BiFunction<List<Value>, List<String>, R> recordValueFunc) {
        final Result resultSet = executeQuery(stmt);
        if (!resultSet.hasNext()) return defaultSupplier.get();

        final Record row = resultSet.next();
        if (row == null || row.size() == 0) return defaultSupplier.get();


        return recordValueFunc.apply(row.values(), row.keys());
    }

    private <R> List<R> collectResult(String stmt, BiFunction<List<Value>, List<String>, R> recordValueFunc) {
        return collectMultiResult(stmt, recordValueFunc.andThen(Collections::singletonList));
    }

    private <R> List<R> collectMultiResult(String stmt, BiFunction<List<Value>, List<String>, List<R>> recordValueFunc) {
        BiConsumer<List<R>, List<R>> combiner;
        final Result resultSet = executeQuery(stmt);

        final List<R> ret = new LinkedList<>();
        while (resultSet.hasNext()) {
            final Record row = resultSet.next();
            ret.addAll(recordValueFunc.apply(row.values(), row.keys()));
        }

        return ret;
    }

    private Map<String, Object> createMapResult(@NotNull List<Value> values, List<String> columnNames) {
        final Map<String, Object> ret = new LinkedHashMap<>(columnNames.size());
        for (int i = 0; i < values.size(); i++) {
            final Value value = values.get(i);
            if (value == null || value.isNull()) continue;
            final Object resultant = context.resultant(value);
            final String columnName = columnNames.get(i);
            ret.put(columnName, resultant);
        }

        return ret;
    }

    private <ID, M extends Model<ID>> M createModelResult(BeanDefinition<M> modelDefinition, Predicate<Value> filter, @NotNull List<Value> rowValueList, List<String> columnNames) {
        M ret = null;

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < rowValueList.size(); i++) {
            final Value value = rowValueList.get(i);
            if (value == null || value.isNull()) continue;
            if (filter.negate().test(value)) continue;
            ret = context.resultant(value, modelDefinition);
        }

        if (ret == null) ret = modelDefinition.newInstance();
        for (int i = 0; i < rowValueList.size(); i++) {
            final Value value = rowValueList.get(i);
            if (value == null || value.isNull()) continue;
            if (filter.test(value)) continue;
            modelDefinition.setter(ret, columnNames.get(i), value);
        }

        return ret;
    }

    private <T> T createBeanResult(@NotNull BeanDefinition<T> definition, @NotNull List<Value> values, List<String> columnNames) {
        final T target = definition.newInstance();
        for (int i = 0; i < values.size(); i++) {
            final Value value = values.get(i);
            if (value == null || value.isNull()) continue;

            final String columnName = columnNames.get(i);
            definition.setter(target, columnName, value);
        }

        return target;
    }

    private <T> List<T> createValuesResult(@NotNull Class<T> valueType, @NotNull List<Value> values) {
        final List<T> ret= new ArrayList<>(values.size());
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < values.size(); i++) {
            final Value value = values.get(i);
            if (value == null || value.isNull()) continue;

            ret.add(context.resultant(value, valueType));
        }

        return ret;
    }

    private boolean isEdgeValue(Value value) {
        if (value == null) return false;
        return value.type() == TYPE_SYSTEM.RELATIONSHIP();
    }

    private boolean isVertexValue(Value value) {
        if (value == null) return false;
        return value.type() == TYPE_SYSTEM.NODE();
    }
}
