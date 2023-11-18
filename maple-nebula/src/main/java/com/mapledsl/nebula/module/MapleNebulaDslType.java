package com.mapledsl.nebula.module;

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

@SuppressWarnings("unchecked")
public enum MapleNebulaDslType {
    NULL(1) {
        @Override
        public Void apply(Value value) {
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

            Value cur = Value.vVal(it.src);
            vertices.add(VERTEX.apply(cur));

            for (Step step : it.steps) {
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

            final Map<String, Object> ret = new HashMap<>();
            ret.put("vertices", vertices);
            ret.put("edges", edges);
            return ret;
        }
    },
    LIST(12) {
        @Override
        public List<Object> apply(Value value) {
            if (value.getLVal().isSetValues()) return Collections.emptyList();
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
            return new LinkedHashMap(){
                {
                    for (Map.Entry<byte[], Value> entry : value.getMVal().kvs.entrySet()) {
                        put(new String(entry.getKey()).intern(), apply(entry.getValue()));
                    }
                }
            };
        }
    },
    SET(14) {
        @Override
        public HashSet apply(Value value) {
            return new HashSet(){
                {
                    for (Value it : value.getUVal().values) { add(any(it)); }
                }
            };
        }
    },
    DATASET(15) {
        @Override
        public Serializable apply(Value value) {
            throw new UnsupportedOperationException("Has not been implemented.");
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

    private static Object any(Value value) {
        final MapleNebulaDslType nebulaType = NEBULA_DSL_TYPE_MAPPINGS.get(value.getSetField());
        return nebulaType == null ? null : nebulaType.apply(value);
    }
}
