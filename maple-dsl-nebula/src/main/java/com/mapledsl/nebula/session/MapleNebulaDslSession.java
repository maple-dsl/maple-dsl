package com.mapledsl.nebula.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.model.ID;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.session.MapleDslSession;
import com.mapledsl.nebula.model.NebulaModel;
import com.mapledsl.nebula.module.MapleNebulaDslResultHandler;
import com.vesoft.nebula.*;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class MapleNebulaDslSession implements MapleDslSession {
    final Session session;
    final MapleDslConfiguration context;

    static final Logger LOG = LoggerFactory.getLogger(MapleNebulaDslSession.class);
    // Assert Row#values is not null and not empty, so row.getValues().get(0) it will not occur ArrayIndexOutOfBoundsException.
    static final Predicate<Row> ROW_VALUE_EMPTY_PREDICATE = row -> !row.isSetValues() || row.getValues().isEmpty();

    public MapleNebulaDslSession(Session session, MapleDslConfiguration context) {
        this.session = session;
        this.context = context;
    }

    public Map<String, Object> selectMap(String stmt) {
        return singleResult(stmt, Collections::emptyMap, this::createMapResult);
    }

    @Override
    public List<Map<String, Object>> selectMaps(String stmt) {
        return collectResult(stmt, this::createMapResult);
    }

    @Override
    public Model.V selectVertex(String stmt) {
        return singleResult(stmt, this::createVertexResult);
    }

    @Override
    public List<Model.V> selectVertexList(String stmt) {
        return collectResult(stmt, this::createVertexResult);
    }

    @Override
    public Model.E selectEdge(String stmt) {
        return singleResult(stmt, this::createEdgeResult);
    }

    @Override
    public List<Model.E> selectEdgeList(String stmt) {
        return collectResult(stmt, this::createEdgeResult);
    }

    @Override
    public <T> T selectOne(String stmt, Class<T> mappedEntityType) {
        final ResultSet resultSet = executeQuery(stmt);

        final List<Row> rows = resultSet.getRows();
        if (rows == null || rows.isEmpty()) return null;
        final Row row = rows.get(0);

        if (ROW_VALUE_EMPTY_PREDICATE.test(row)) return null;
        final List<String> columnNames = resultSet.getColumnNames();
        if (columnNames == null || columnNames.isEmpty()) return null;

        final List<Value> rowValueList = row.getValues();
        final BeanDefinition<T> definition = context.beanDefinition(mappedEntityType);

        if (definition == null) return processValue(rowValueList.get(0), mappedEntityType);
        return createBeanResult(definition, rowValueList, columnNames);
    }

    @Override
    public <T> List<T> selectList(String stmt, Class<T> mappedEntityType) {
        final ResultSet resultSet = executeQuery(stmt);

        final List<Row> rows = resultSet.getRows();
        if (rows == null || rows.isEmpty()) return Collections.emptyList();

        final List<String> columnNames = resultSet.getColumnNames();
        if (columnNames == null || columnNames.isEmpty()) return Collections.emptyList();

        final BeanDefinition<T> definition = context.beanDefinition(mappedEntityType);
        final List<T> ret = new ArrayList<>(rows.size());

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < rows.size(); i++) {
            final Row row = rows.get(i);
            if (ROW_VALUE_EMPTY_PREDICATE.test(row)) continue;
            final List<Value> rowValueList = row.getValues();

            if (definition != null) {
                ret.add(createBeanResult(definition, row.getValues(), columnNames));
                continue;
            }

            //noinspection ForLoopReplaceableByForEach
            for (int j = 0; j < rowValueList.size(); j++) {
                ret.add(processValue(rowValueList.get(j), mappedEntityType));
            }
        }

        return ret;
    }

    @Override
    public void close() {
        if (session == null) return;
        session.release();
    }

    @Override
    public MapleDslConfiguration configuration() {
        return context;
    }

    @Override
    public boolean execute(String stmt) throws MapleDslException {
        try {
            return executeQuery(stmt).isSucceeded();
        } catch (MapleDslException ignored) {
            return false;
        }
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

    private Object processValue(@NotNull Value value) {
        try {
            final MapleNebulaDslResultHandler<?> nebulaDslResultHandler = (MapleNebulaDslResultHandler<?>) context.defaultResultHandler();
            return nebulaDslResultHandler.apply(value, context);
        } catch (Exception e) {
            throw new MapleDslBindingException("Nebula default result handler not found.");
        }
    }

    private <T> T processValue(@NotNull Value value, Class<T> clazz) {
        try {
            final MapleNebulaDslResultHandler<?> nebulaDslResultHandler = (MapleNebulaDslResultHandler<?>) context.resultHandler(clazz);
            //noinspection unchecked
            return (T) nebulaDslResultHandler.apply(value, context);
        } catch (Exception e) {
            throw new MapleDslBindingException("Nebula default result handler not found.");
        }
    }

    private Map<String, Object> createMapResult(@NotNull List<Value> values, List<String> columnNames) {
        if (values.size() == 1) {
            final Value firstValue = values.get(0);
            if (firstValue == null || firstValue.getFieldValue() == null) return Collections.emptyMap();
            final Object firstResult = processValue(firstValue);

            // when single `vertex`, `edge` or `path` as result, unwrap into dict then return it.
            if (firstResult instanceof Map) {
                //noinspection unchecked
                return ((Map<String, Object>) firstResult);
            } else {
                return Collections.singletonMap(columnNames.get(0), firstResult);
            }
        }

        final Map<String, Object> ret = new LinkedHashMap<>(columnNames.size());
        for (int i = 0; i < values.size(); i++) {
            final Value value = values.get(i);
            if (value == null || value.getFieldValue() == null) continue;
            final Object resultant = processValue(value);
            final String columnName = context.namingStrategy().translate(columnNames.get(i), context.globalLocale());
            ret.put(columnName, resultant);
        }

        return ret;
    }

    private <T> T createBeanResult(@NotNull BeanDefinition<T> definition, @NotNull List<Value> values, List<String> columnNames) {
        final T target = definition.newInstance();
        for (int i = 0; i < values.size(); i++) {
            final Value value = values.get(i);
            final String columnName = columnNames.get(i);
            if (value == null || value.getFieldValue() == null) continue;

            if (definition.hasSetter(target, columnName)) {
                definition.setter(target, columnName, value);
                continue;
            }

            switch (value.getSetField()) {
                case Value.VVAL:
                    final Vertex vertex = value.getVVal();
                    definition.setter(target, Model.V.ID, vertex.vid);

                    if (vertex.tags.isEmpty()) continue;
                    final Tag tag = vertex.tags.get(0);
                    if (tag == null) continue;
                    if (tag.isSetName()) definition.setter(target, Model.V.TAG, new String(tag.name).intern());
                    if (tag.isSetProps()) tag.props.forEach((k, v) -> definition.setter(target, new String(k).intern(), v));

                    break;
                case Value.EVAL:
                    final Edge edge = value.getEVal();

                    definition.setter(target, Model.E.SRC, edge.type > 0 ? edge.src : edge.dst);
                    definition.setter(target, Model.E.DST, edge.type > 0 ? edge.dst : edge.src);
                    definition.setter(target, Model.E.TAG, new String(edge.name).intern());
                    definition.setter(target, NebulaModel.E.RANK, edge.ranking);

                    if (edge.isSetProps()) edge.props.forEach((k, v) -> definition.setter(target, new String(k).intern(), v));
                    break;
                default:
                    throw new UnsupportedOperationException("Type:" + value.getSetField() + " does not supported `unwrap` yet.");
            }
        }

        return target;
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

            ret.add(recordValueFunc.apply(row.getValues(), columnNames));
        }

        return ret;
    }

    private Model.V createVertexResult(List<Value> rowValueList, List<String> columnNames) {
        final Model.V ret = new Model.V();
        for (int i = 0; i < rowValueList.size(); i++) {
            final Value value = rowValueList.get(0);
            if (value == null || value.getFieldValue() == null) continue;
            if (value.getSetField() != Value.VVAL) {
                ret.put(columnNames.get(i), processValue(value));
                continue;
            }

            final Vertex vertex = value.getVVal();
            ret.setId(processValue(vertex.vid, ID.class));

            if (vertex.tags.isEmpty()) continue;
            final Tag tag = vertex.tags.get(0);
            if (tag.isSetName()) ret.setLabel(new String(tag.name).intern());
            if (tag.isSetProps()) tag.props.forEach((k, v) -> ret.put(new String(k).intern(), processValue(v)));
        }

        return ret;
    }


    private Model.E createEdgeResult(List<Value> rowValueList, List<String> columnNames) {
        final NebulaModel.E ret = new NebulaModel.E();
        for (int i = 0; i < rowValueList.size(); i++) {
            final Value value = rowValueList.get(0);
            if (value == null || value.getFieldValue() == null) continue;
            if (value.getSetField() != Value.EVAL) {
                ret.put(columnNames.get(i), processValue(value));
                continue;
            }

            final Edge edge = value.getEVal();
            ret.setSrc(processValue(edge.type > 0 ? edge.src : edge.dst, ID.class));
            ret.setDst(processValue(edge.type > 0 ? edge.dst : edge.src, ID.class));
            ret.setLabel(new String(edge.name).intern());
            ret.setRank(edge.ranking);

            if (edge.isSetProps()) edge.props.forEach((k, v) -> ret.put(new String(k).intern(), v));
        }

        return ret;
    }
}
