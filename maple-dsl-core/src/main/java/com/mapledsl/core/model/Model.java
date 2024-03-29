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

/**
 * The Model interface represents a basic model with properties and getters and setters for those properties.
 *
 * @param <ID> The type of the identifier for the model.
 */
public interface Model<ID> extends Serializable {
    String ID = "id", TAG = "tag";

    @Nullable @PropertyGetter(ID) ID id();
    @Nullable @PropertyGetter(TAG) String label();

    @NotNull Map<String, Object> props();

    default Model<ID> put(String key, Object value) {
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
        return (D) props.getOrDefault(key, defaultValue);
    }

    /**
     * Represents a path in a graph, consisting of a sequence of vertices and edges.
     *
     * @param <R> the type of the vertex and edge IDs
     */
    @ApiStatus.NonExtendable
    final class Path<R> implements Serializable {
        LinkedList<V<R>> vertices;
        LinkedList<E<?,R>> edges;

        public Path(LinkedList<V<R>> vertices, LinkedList<E<?,R>> edges) {
            this.vertices = vertices;
            this.edges = edges;
        }

        public V<R> first() {
            return vertices == null ? null : vertices.getFirst();
        }

        public V<R> last() {
            return vertices == null ? null : vertices.getLast();
        }

        public LinkedList<V<R>> vertices() {
            return vertices;
        }

        public LinkedList<E<?,R>> edges() {
            return edges;
        }
    }

    /**
     * This class represents a generic model with properties and getters and setters for those properties.
     *
     * @param <ID> The type of the identifier for the model.
     */
    class V<ID> implements Model<ID> {
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

        public @PropertySetter(ID) V<ID> setId(ID id) {
            this.id = id;
            return this;
        }

        public @PropertySetter(TAG) V<ID> setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public String toString() {
            return label + "@" + id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            V<?> v = (V<?>) o;
            return Objects.equals(id, v.id) && Objects.equals(label, v.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, label);
        }
    }

    /**
     * The E class represents a generic model with properties and getters and setters for those properties.
     *
     * @param <ID> The type of the identifier for the model.
     */
    class E<ID, R> implements Model<ID> {
        protected @Property(value = ID, defined = false)    ID id;
        protected @Property(value = SRC, defined = false)   R src;
        protected @Property(value = DST, defined = false)   R dst;
        protected @Property(value = TAG, defined = false)   String label;
        protected @PropertyIgnore                           Map<String, Object> props;

        public @PropertyGetter(SRC) R src() {
            return src;
        }

        public @PropertyGetter(DST) R dst() {
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

        public @PropertySetter(TAG) E<ID,R> setLabel(String label) {
            this.label = label;
            return this;
        }

        public @PropertySetter(ID) E<ID,R> setId(ID id) {
            this.id = id;
            return this;
        }

        public @PropertySetter(SRC) E<ID,R> setSrc(R src) {
            this.src = src;
            return this;
        }

        public @PropertySetter(DST) E<ID,R> setDst(R dst) {
            this.dst = dst;
            return this;
        }

        @Override
        public String toString() {
            return "@" + id + "#" + src + "->" + dst;
        }

        public static final String SRC = "src", DST = "dst";
    }
}