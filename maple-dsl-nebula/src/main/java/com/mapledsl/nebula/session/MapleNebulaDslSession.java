package com.mapledsl.nebula.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.session.MapleDslSession;
import com.vesoft.nebula.Row;
import com.vesoft.nebula.Value;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a session for executing DSL statements against a Nebula Graph database.
 */
public final class MapleNebulaDslSession implements MapleDslSession {
    final Session session;
    /**
     * Represents the configuration for a Maple DSL session.
     * <p></p>
     * This configuration is used by the MapleNebulaDslSession class, which is responsible for executing Maple DSL queries
     * against a database.
     * <p></p>
     * This class defines the properties and methods necessary to configure and manage a Maple DSL session.
     */
    final MapleDslConfiguration context;

    static final Logger LOG = LoggerFactory.getLogger(MapleNebulaDslSession.class);
    // Assert Row#values is not null and not empty, so row.getValues().get(0) it will not occur ArrayIndexOutOfBoundsException.
    static final Predicate<Row> ROW_VALUE_EMPTY_PREDICATE = row -> !row.isSetValues() || row.getValues().isEmpty();

    /**
     * Represents a session for executing Maple DSL queries.
     * This session is responsible for maintaining the connection to the database and executing the queries.
     *
     * @param session  The underlying session to interact with the database.
     * @param context  The configuration defining the behavior of the DSL session.
     */
    public MapleNebulaDslSession(Session session, MapleDslConfiguration context) {
        this.session = session;
        this.context = context;
    }

    public @NotNull Map<String, Object> selectMap(@NotNull String stmt) {
        return singleResult(stmt, Collections::emptyMap, this::createMapResult);
    }

    @Override
    public @NotNull List<Map<String, Object>> selectMaps(@NotNull String stmt) {
        return collectResult(stmt, this::createMapResult);
    }

    @Override
    public <ID> Model.@Nullable V<ID> selectVertex(@NotNull String stmt) {
        return singleResult(stmt, ((values, columns) -> {
            final BeanDefinition<Model.V<ID>> vBeanDefinition = context.vertexDefinition();
            return createModelResult(vBeanDefinition, this::isVertexValue, values, columns);
        }));
    }

    @Override
    public <ID> @NotNull List<Model.V<ID>> selectVertexList(@NotNull String stmt) {
        return collectResult(stmt, (values, columns) -> {
            final BeanDefinition<Model.V<ID>> vBeanDefinition = context.vertexDefinition();
            return createModelResult(vBeanDefinition, this::isVertexValue, values, columns);
        });
    }

    @Override
    public <ID, R> Model.@Nullable E<ID,R> selectEdge(@NotNull String stmt) {
        return singleResult(stmt, ((values, columns) -> {
            final BeanDefinition<Model.E<ID,R>> eBeanDefinition = context.edgeDefinition();
            return createModelResult(eBeanDefinition, this::isEdgeValue, values, columns);
        }));
    }

    @Override
    public <ID,R> @NotNull List<Model.E<ID,R>> selectEdgeList(@NotNull String stmt) {
        return collectResult(stmt, (values, columns) -> {
            final BeanDefinition<Model.E<ID,R>> eBeanDefinition = context.edgeDefinition();
            return createModelResult(eBeanDefinition, this::isEdgeValue, values, columns);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T selectOne(String stmt, Class<T> mappedEntityType) {
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
    public <T> @NotNull List<T> selectList(String stmt, Class<T> mappedEntityType) {
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
    public boolean execute(@NotNull String stmt) throws MapleDslException {
        try {
            return executeQuery(stmt).isSucceeded();
        } catch (MapleDslException ignored) {
            return false;
        }
    }

    @Override
    public void close() {
        if (session == null) return;
        session.release();
    }

    @Override
    public @NotNull MapleDslConfiguration configuration() {
        return context;
    }

    private ResultSet executeQuery(String stmt) throws MapleDslException {
        ResultSet resultSet;
        try {
            if (LOG.isInfoEnabled()) LOG.info("Execution statement:{}", stmt);
            resultSet = session.execute(stmt);
        } catch (Exception e) {
            LOG.error("Execution Error, statement:{}", stmt, e);
            throw new MapleDslExecutionException(e.getMessage(), e);
        }
        if (resultSet.isSucceeded()) return resultSet;
        LOG.error("Execution Statement:{}, Error:{}, Reason:{}", stmt, resultSet.getErrorCode(), resultSet.getErrorMessage());
        throw new MapleDslExecutionException(resultSet.getErrorCode() + ":" + resultSet.getErrorMessage());
    }

    private <R> R singleResult(String stmt, BiFunction<List<Value>, List<String>, R> recordValueFunc) {
        return singleResult(stmt, () -> null, recordValueFunc);
    }

    private <R> R singleResult(String stmt, Supplier<R> defaultSupplier, BiFunction<List<Value>, List<String>, R> recordValueFunc) {
        final ResultSet resultSet = executeQuery(stmt);

        final List<Row> rows = resultSet.getRows();
        if (rows == null || rows.isEmpty()) return defaultSupplier.get();
        final Row row = rows.get(0);

        if (ROW_VALUE_EMPTY_PREDICATE.test(row)) return defaultSupplier.get();
        final List<String> columnNames = resultSet.getColumnNames();
        if (columnNames == null || columnNames.isEmpty()) return defaultSupplier.get();

        return recordValueFunc.apply(row.getValues(), columnNames);
    }

    private <R> List<R> collectResult(String stmt, BiFunction<List<Value>, List<String>, R> recordValueFunc) {
        return collectMultiResult(stmt, recordValueFunc.andThen(Collections::singletonList));
    }

    private <R> List<R> collectMultiResult(String stmt, BiFunction<List<Value>, List<String>, List<R>> recordValueFunc) {
        BiConsumer<List<R>, List<R>> combiner;
        final ResultSet resultSet = executeQuery(stmt);

        final List<Row> rows = resultSet.getRows();
        if (rows == null || rows.isEmpty()) return Collections.emptyList();

        final List<String> columnNames = resultSet.getColumnNames();
        if (columnNames == null || columnNames.isEmpty()) return Collections.emptyList();

        final List<R> ret = new ArrayList<>(rows.size());
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < rows.size(); i++) {
            final Row row = rows.get(i);
            if (ROW_VALUE_EMPTY_PREDICATE.test(row)) continue;

            ret.addAll(recordValueFunc.apply(row.getValues(), columnNames));
        }

        return ret;
    }

    private Map<String, Object> createMapResult(@NotNull List<Value> values, List<String> columnNames) {
        final Map<String, Object> ret = new LinkedHashMap<>(columnNames.size());
        for (int i = 0; i < values.size(); i++) {
            final Value value = values.get(i);
            if (value == null || value.getFieldValue() == null) continue;
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
            if (value == null || value.getFieldValue() == null) continue;
            if (filter.negate().test(value)) continue;
            ret = context.resultant(value, modelDefinition);
        }

        if (ret == null) ret = modelDefinition.newInstance();
        for (int i = 0; i < rowValueList.size(); i++) {
            final Value value = rowValueList.get(i);
            if (value == null || value.getFieldValue() == null) continue;
            if (filter.test(value)) continue;
            modelDefinition.setter(ret, columnNames.get(i), value);
        }

        return ret;
    }

    private <T> T createBeanResult(@NotNull BeanDefinition<T> definition, @NotNull List<Value> values, List<String> columnNames) {
        final T target = definition.newInstance();
        for (int i = 0; i < values.size(); i++) {
            final Value value = values.get(i);
            if (value == null || value.getFieldValue() == null) continue;

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
            if (value == null || value.getFieldValue() == null) continue;

            ret.add(context.resultant(value, valueType));
        }

        return ret;
    }

    private boolean isEdgeValue(Value value) {
        if (value == null) return false;
        return value.getSetField() == Value.EVAL;
    }

    private boolean isVertexValue(Value value) {
        if (value == null) return false;
        return value.getSetField() == Value.VVAL;
    }
}
