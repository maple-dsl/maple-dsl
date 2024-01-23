package com.mapledsl.nebula.model;

import com.mapledsl.core.annotation.PropertyGetter;
import com.mapledsl.core.annotation.PropertySetter;
import com.mapledsl.core.model.Model;

/**
 * NebulaModel is an interface that represents a generic model with properties and getters and setters for those properties.
 * It extends the Model interface and defines additional methods and properties specific to the Nebula model.
 */
public interface NebulaModel {

    /**
     * This interface represents a generic model with properties and getters and setters for those properties.
     *
     * @param <ID> The type of the identifier for the model.
     */
    class E<R> extends Model.E<NebulaEdgeID<R>, R> {
        protected long rank;

        @Override
        public synchronized NebulaEdgeID<R> id() {
            if (id == null) id = new NebulaEdgeID<>(src, dst, rank);
            return id;
        }

        /**
         * Retrieves the rank of the model. If the rank property is not set, it returns 0L as the default value.
         *
         * @return The rank of the model, or 0L if the rank property is not set.
         */
        public @PropertyGetter(RANK) long rank() {
            return rank;
        }

        /**
         * Sets the rank of the model.
         *
         * @param rank The rank to be set.
         * @return An instance of the EDGE class with the rank property set to the provided value.
         */
        public @PropertySetter(RANK) NebulaModel.E<R> setRank(long rank) {
            this.rank = rank;
            return this;
        }

        public static final String RANK = "rank";
    }
}
