package com.mapledsl.core.model;

import com.mapledsl.core.annotation.Property;
import com.mapledsl.core.annotation.PropertyGetter;
import com.mapledsl.core.annotation.PropertyIgnore;
import com.mapledsl.core.annotation.PropertySetter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public interface Model<R extends Number & CharSequence> extends Serializable {
    String ID = "id", TAG = "tag";

    @Nullable R id();
    @Nullable String label();

    @NotNull Map<String, Object> props();

    default Model<R> put(String key, Object value) {
        final Map<String, Object> props = props();
        props.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    default <D> D get(String key) {
        final Map<String, Object> props = props();
        if (props.isEmpty()) return null;
        return (D) props.get(key);
    }

    @SuppressWarnings("unchecked")
    default <D> D getOrDefault(String key, D defaultValue) {
        final Map<String, Object> props = props();
        if (props.isEmpty()) return null;
        return (D) props.getOrDefault(key, defaultValue);
    }

    /**
     * Base graph PATH model
     */
    @ApiStatus.NonExtendable
    final class Path implements Serializable {
        LinkedList<V> vertices;
        LinkedList<E> edges;

        public Path(LinkedList<V> vertices, LinkedList<E> edges) {
            this.vertices = vertices;
            this.edges = edges;
        }

        public V first() {
            return vertices == null ? null : vertices.getFirst();
        }

        public V last() {
            return vertices == null ? null : vertices.getLast();
        }

        public LinkedList<V> vertices() {
            return vertices;
        }

        public LinkedList<E> edges() {
            return edges;
        }
    }

    /**
     * Base graph VERTEX model.
     */
    class V implements Model<ID> {
        protected @Property(value = ID, defined = false)    ID id;
        protected @Property(value = TAG, defined = false)   String label;
        protected @PropertyIgnore                           Map<String, Object> props;

        public @PropertyGetter(ID) ID id() {
            return id;
        }

        public @PropertyGetter(TAG) String label() {
            return label;
        }

        @Override
        public @NotNull Map<String, Object> props() {
            if (props == null) props = new HashMap<>();
            return props;
        }

        public @PropertySetter(ID) V setId(ID id) {
            this.id = id;
            return this;
        }

        public @PropertySetter(TAG) V setLabel(String label) {
            this.label = label;
            return this;
        }

        public V setId(String id) {
            this.id = new ID(id);
            return this;
        }

        public V setId(Number id) {
            this.id = new ID(id);
            return this;
        }

        @Override
        public String toString() {
            return label + "@" + id;
        }

        public static V of(ID id) {
            return new V().setId(id);
        }

        public static V of(String id) {
            return new V().setId(id);
        }

        public static V of(Number id) {
            return new V().setId(id);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            V v = (V) o;
            return Objects.equals(id, v.id) && Objects.equals(label, v.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, label);
        }
    }

    /**
     * Base graph EDGE model.
     */
    class E implements Model<ID> {
        protected @Property(value = ID, defined = false)    ID id;
        protected @Property(value = SRC, defined = false)   ID src;
        protected @Property(value = DST, defined = false)   ID dst;
        protected @Property(value = TAG, defined = false)   String label;
        protected @PropertyIgnore                           Map<String, Object> props;

        public @PropertyGetter(SRC) ID src() {
            return src;
        }

        public @PropertyGetter(DST) ID dst() {
            return dst;
        }

        public @PropertyGetter(ID) ID id() {
            return id;
        }

        public @PropertyGetter(TAG) String label() {
            return label;
        }

        @Override
        public @NotNull Map<String, Object> props() {
            if (props == null) props = new HashMap<>();
            return props;
        }

        public @PropertySetter(TAG) E setLabel(String label) {
            this.label = label;
            return this;
        }

        public @PropertySetter(ID) E setId(ID id) {
            this.id = id;
            return this;
        }

        public @PropertySetter(SRC) E setSrc(ID src) {
            this.src = src;
            return this;
        }

        public @PropertySetter(DST) E setDst(ID dst) {
            this.dst = dst;
            return this;
        }

        public E setId(String id) {
            this.id = new ID(id);
            return this;
        }

        public E setId(Number id) {
            this.id = new ID(id);
            return this;
        }

        public E setSrc(String id) {
            this.src = new ID(id);
            return this;
        }

        public E setSrc(Number id) {
            this.src = new ID(id);
            return this;
        }

        public E setDst(String id) {
            this.dst = new ID(id);
            return this;
        }

        public E setDst(Number id) {
            this.dst = new ID(id);
            return this;
        }

        @Override
        public String toString() {
            return "@" + id + "#" + src + "->" + dst;
        }

        public static final String SRC = "src", DST = "dst";

        public static E of(ID id, ID src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, ID src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, ID src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, String src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, String src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, String src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, Number src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, Number src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(ID id, Number src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, ID src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, ID src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, ID src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, String src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, String src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, String src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, Number src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, Number src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(String id, Number src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, ID src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, ID src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, ID src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, String src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, String src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, String src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, Number src, ID dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, Number src, String dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }

        public static E of(Number id, Number src, Number dst) {
            return new E().setId(id).setSrc(src).setDst(dst);
        }
    }
}