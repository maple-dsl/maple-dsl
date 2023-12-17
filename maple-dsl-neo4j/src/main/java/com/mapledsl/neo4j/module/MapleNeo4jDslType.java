package com.mapledsl.neo4j.module;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.mapledsl.core.model.Model;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.types.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.neo4j.driver.internal.types.InternalTypeSystem.TYPE_SYSTEM;

@SuppressWarnings("unchecked")
public enum MapleNeo4jDslType {
    NULL(TYPE_SYSTEM.NULL()) {
        @Override
        <R> R apply(Value value) {
            return null;
        }
    },
    BYTES(TYPE_SYSTEM.BYTES()) {
        @Override
        byte[] apply(Value value) {
            return value.asByteArray();
        }
    },
    BOOLEAN(TYPE_SYSTEM.BOOLEAN()) {
        @Override
        public Boolean apply(Value value) {
            return value.asBoolean(false);
        }
    },
    NUMBER(TYPE_SYSTEM.NUMBER()) {
        @Override
        public Number apply(Value value) {
            return value.asNumber();
        }
    },
    INT(TYPE_SYSTEM.INTEGER()) {
        @Override
        public Long apply(Value value) {
            return value.asLong(0L);
        }
    },
    FLOAT(TYPE_SYSTEM.FLOAT()) {
        @Override
        public Float apply(Value value) {
            return value.asFloat(0f);
        }
    },
    STRING(TYPE_SYSTEM.STRING()) {
        @Override
        public String apply(Value value) {
            return value.asString();
        }
    },
    DATE(TYPE_SYSTEM.DATE()) {
        @Override
        public LocalDate apply(Value value) {
            return value.asLocalDate();
        }
    },
    TIME(TYPE_SYSTEM.TIME()) {
        @Override
        public LocalTime apply(Value value) {
            return value.asOffsetTime().toLocalTime();
        }
    },
    DATETIME(TYPE_SYSTEM.DATE_TIME()) {
        @Override
        public LocalDateTime apply(Value value) {
            return value.asOffsetDateTime().toLocalDateTime();
        }
    },
    LOCAL_TIME(TYPE_SYSTEM.LOCAL_TIME()) {
        @Override
        public LocalTime apply(Value value) {
            return value.asLocalTime();
        }
    },
    LOCAL_DATETIME(TYPE_SYSTEM.LOCAL_DATE_TIME()) {
        @Override
        public LocalDateTime apply(Value value) {
            return value.asLocalDateTime();
        }
    },
    VERTEX(TYPE_SYSTEM.NODE()) {
        @Override
        public Map<String, Object> apply(Value value) {
            final Map<String, Object> ret = new LinkedHashMap<>();
            final Node vertex = value.asNode();

            if (vertex == null) return ret;
            ret.put(Model.V.ID, vertex.id());
            ret.put(Model.V.TAG, Iterators.get(vertex.labels().iterator(), 0));

            if (vertex.size() == 0) return ret;
            final Map<String, Object> props = vertex.asMap(MapleNeo4jDslType::any);
            if (props == null || props.isEmpty()) return ret;

            ret.putAll(props);
            return ret;
        }
    },
    EDGE(TYPE_SYSTEM.RELATIONSHIP()) {
        @Override
        public Map<String, Object> apply(Value value) {
            final Map<String, Object> ret = new LinkedHashMap<>();

            final Relationship edge = value.asRelationship();
            if (edge == null) return ret;

            ret.put(Model.E.SRC, edge.startNodeId());
            ret.put(Model.E.DST, edge.endNodeId());
            ret.put(Model.E.TAG, edge.type());
            ret.put(Model.E.ID, edge.id());

            if (edge.size() == 0) return ret;
            final Map<String, Object> props = edge.asMap(MapleNeo4jDslType::any);
            if (props == null || props.isEmpty()) return ret;

            ret.putAll(props);
            return ret;
        }
    },
    PATH(TYPE_SYSTEM.PATH()) {
        @Override
        public Map<String, Object> apply(Value value) {
            final Path path = value.asPath();

            final LinkedList<Map<String, Object>> vertices = new LinkedList<>();
            final LinkedList<Map<String, Object>> edges = new LinkedList<>();

            final Map<String, Object> ret = new HashMap<>();
            ret.put("vertices", vertices);
            ret.put("edges", edges);


            if (path.start() == null) return ret;

            vertices.add(VERTEX.apply(new NodeValue(path.start())));

            for (Path.Segment step : path) {
                if (step == null) continue;

                final NodeValue nextVertex = new NodeValue(path.end());
                vertices.add(VERTEX.apply(nextVertex));

                edges.add(EDGE.apply(new RelationshipValue(step.relationship())));
            }

            return ret;
        }
    },
    LIST(TYPE_SYSTEM.LIST()) {
        @Override
        public List<Object> apply(Value value) {
            return value.asList(MapleNeo4jDslType::any);
        }
    },
    MAP(TYPE_SYSTEM.MAP()) {
        @Override
        public Map<String, Object> apply(Value value) {
            return value.asMap(MapleNeo4jDslType::any, Maps.newHashMap());
        }
    },
    DURATION(TYPE_SYSTEM.DURATION()) {
        @Override
        public java.time.Duration apply(Value value) {
            final IsoDuration duration = value.asIsoDuration();
            if (duration == null) return null;

            return java.time.Duration.ofSeconds(duration.seconds(), duration.nanoseconds());
        }
    };

    final Type type;

    MapleNeo4jDslType(Type type) {
        this.type = type;
    }

    abstract <R> R apply(Value value);

    static final Map<Type, MapleNeo4jDslType> NEBULA_DSL_TYPE_MAPPINGS = Arrays.stream(MapleNeo4jDslType.values()).collect(Collectors.toMap(it -> it.type, Function.identity()));

    public static Object any(Value value) {
        final MapleNeo4jDslType nebulaType = NEBULA_DSL_TYPE_MAPPINGS.get(value.type());
        return nebulaType == null ? null : nebulaType.apply(value);
    }
}
