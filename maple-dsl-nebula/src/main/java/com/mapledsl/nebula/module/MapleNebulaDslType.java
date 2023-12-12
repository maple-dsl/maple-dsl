package com.mapledsl.nebula.module;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mapledsl.core.model.Model;
import com.mapledsl.nebula.model.NebulaModel;
import com.vesoft.nebula.Date;
import com.vesoft.nebula.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Enum class representing different types in Maple Nebula DSL.
 * <p></p>
 * The MapleNebulaDslType enum provides methods for converting values of type `Value` to the corresponding Java types.
 * Each enum constant has an `apply` method that takes a `Value` object and returns a value of the corresponding Java type.
 * <p></p>
 * The enum constants represent the following types:
 * 1. NULL: Represents a null value.
 * 2. BOOLEAN: Represents a boolean value.
 * 3. LONG: Represents a long value.
 * 4. DOUBLE: Represents a double value.
 * 5. STRING: Represents a string value.
 * 6. DATE: Represents a date value.
 * 7. TIME: Represents a time value.
 * 8. DATETIME: Represents a datetime value.
 * 9. VERTEX: Represents a vertex value.
 * 10. EDGE: Represents an edge value.
 * 11. PATH: Represents a path value.
 * 12. LIST: Represents a list value.
 * 13. MAP: Represents a map value.
 * 14. SET: Represents a set value.
 * 15. DATASET: Represents a dataset value.
 * 16. GEOGRAPHY: Represents a geography value.
 * 17. DURATION: Represents a duration value.
 * <p></p>
 * The enum has a static method `any` which takes a `Value` object and returns the corresponding Java object based on its set field.
 * <p></p>
 */
@SuppressWarnings("unchecked")
public enum MapleNebulaDslType {
    NULL(1) {
        @Override
        <R> R apply(Value value) {
            return null;
        }
    },
    BOOLEAN(2) {
        @Override
        public Boolean apply(Value value) {
            return value.isBVal();
        }
    },
    LONG(3) {
        @Override
        public Long apply(Value value) {
            return value.getIVal();
        }
    },
    DOUBLE(4) {
        @Override
        public Double apply(Value value) {
            return value.getFVal();
        }
    },
    STRING(5) {
        @Override
        public String apply(Value value) {
            return new String(value.getSVal(), UTF_8).intern();
        }
    },
    DATE(6) {
        @Override
        public LocalDate apply(Value value) {
            Date it = value.getDVal();
            return LocalDate.of(it.year, it.month, it.day);
        }
    },
    TIME(7) {
        @Override
        public LocalTime apply(Value value) {
            Time it = value.getTVal();
            return LocalTime.of(it.hour, it.minute, it.sec);
        }
    },
    DATETIME(8) {
        @Override
        public LocalDateTime apply(Value value) {
            DateTime it = value.getDtVal();
            return LocalDateTime.of(it.year, it.month, it.day, it.hour, it.minute, it.sec, it.microsec / 1000);
        }
    },
    VERTEX(9) {
        @Override
        public Map<String, Object> apply(Value value) {
            final Map<String, Object> ret = new LinkedHashMap<>();
            final Vertex vertex = value.getVVal();
            ret.put(Model.V.ID, any(vertex.vid));

            if (vertex.tags.isEmpty()) return ret;
            final Tag tag = vertex.tags.get(0);
            if (tag == null) return ret;
            if (tag.isSetName()) ret.put(Model.V.TAG, new String(tag.name).intern());
            if (tag.isSetProps()) tag.props.forEach((k, v) -> ret.put(new String(k).intern(), any(v)));

            return ret;
        }
    },
    EDGE(10) {
        @Override
        public Map<String, Object> apply(Value value) {
            final Map<String, Object> ret = new LinkedHashMap<>();
            final Edge edge = value.getEVal();
            ret.put(Model.E.SRC, edge.type > 0 ? any(edge.src) : any(edge.dst));
            ret.put(Model.E.DST, edge.type > 0 ? any(edge.dst) : any(edge.src));
            ret.put(Model.E.TAG, new String(edge.name).intern());
            ret.put(NebulaModel.E.RANK, edge.ranking);

            if (edge.isSetProps()) edge.props.forEach((k, v) -> ret.put(new String(k).intern(),any(v)));
            return ret;
        }
    },
    PATH(11) {
        @Override
        public Map<String, Object> apply(Value value) {
            final Path it = value.getPVal();

            final LinkedList<Map<String, Object>> vertices = new LinkedList<>();
            final LinkedList<Map<String, Object>> edges = new LinkedList<>();

            final Map<String, Object> ret = new HashMap<>();
            ret.put("vertices", vertices);
            ret.put("edges", edges);

            if (it.src == null) return ret;

            Value cur = Value.vVal(it.src);
            vertices.add(VERTEX.apply(cur));

            for (Step step : it.steps) {
                if (step.dst == null) continue;
                final Value nextVertex = Value.vVal(step.dst);
                vertices.add(VERTEX.apply(nextVertex));

                edges.add(EDGE.apply(Value.eVal(Edge.builder()
                        .setName(step.name)
                        .setSrc(cur)
                        .setDst(nextVertex)
                        .setRanking(step.ranking)
                        .setType(step.type)
                        .build()
                )));

                cur = nextVertex;
            }

            return ret;
        }
    },
    LIST(12) {
        @Override
        public List<Object> apply(Value value) {
            if (!value.getLVal().isSetValues()) return Collections.emptyList();
            final List<Value> valueList = value.getLVal().values;
            if (valueList.isEmpty()) return Collections.emptyList();

            final List<Object> ret = new ArrayList<>(valueList.size());
            for (Value it : valueList) {
                ret.add(any(it));
            }

            return ret;
        }
    },
    MAP(13) {
        @Override
        public Map<String, Object> apply(Value value) {
            if (!value.getMVal().isSetKvs()) return Maps.newHashMap();
            final Map<byte[], Value> valueMap = value.getMVal().kvs;
            if (valueMap.isEmpty()) return Maps.newHashMap();

            final Map<String, Object> ret = new HashMap<>(valueMap.size());
            for (Map.Entry<byte[], Value> entry : valueMap.entrySet()) {
                ret.put(new String(entry.getKey()).intern(), apply(entry.getValue()));
            }

            return ret;
        }
    },
    SET(14) {
        @Override
        public Set<Object> apply(Value value) {
            if (!value.getUVal().isSetValues()) return Sets.newHashSet();
            final Set<Value> valueSet = value.getUVal().values;
            if (valueSet.isEmpty()) return Sets.newHashSet();

            final Set<Object> ret = new HashSet<>(valueSet.size());
            for (Value it : valueSet) {
                ret.add(any(it));
            }

            return ret;
        }
    },
    DATASET(15) {
        @Override
        public Map<String, Set<Object>> apply(Value value) {
            if (!value.getGVal().isSetRows()) return Maps.newHashMap();
            if (value.getGVal().rows.isEmpty()) return Maps.newHashMap();

            final Map<String, Set<Object>> ret = new HashMap<>(value.getGVal().rows.size());
            for (int i = 0; i < value.getGVal().rows.size(); i++) {
                final Row row = value.getGVal().rows.get(i);
                final String col = new String(value.getGVal().column_names.get(i)).intern();
                if (!row.isSetValues()) {
                    ret.put(col, Collections.emptySet());
                    continue;
                }

                ret.put(col, row.values.stream().map(MapleNebulaDslType::any).collect(Collectors.toSet()));
            }
            return ret;
        }
    },
    GEOGRAPHY(16) {
        @Override
        public Serializable apply(Value value) {
            throw new UnsupportedOperationException("Has not been implemented.");
        }
    },
    DURATION(17) {
        @Override
        public java.time.Duration apply(Value value) {
            final Duration duration = value.getDuVal();
            return java.time.Duration.ofSeconds(duration.seconds, duration.microseconds);
        }
    };

    final int setField;

    MapleNebulaDslType(int setField) {
        this.setField = setField;
    }

    abstract <R> R apply(Value value);

    static final Map<Integer, MapleNebulaDslType> NEBULA_DSL_TYPE_MAPPINGS = Arrays.stream(MapleNebulaDslType.values()).collect(Collectors.toMap(
            it -> it.setField, Function.identity()
    ));

    public static Object any(Value value) {
        final MapleNebulaDslType nebulaType = NEBULA_DSL_TYPE_MAPPINGS.get(value.getSetField());
        return nebulaType == null ? null : nebulaType.apply(value);
    }
}
