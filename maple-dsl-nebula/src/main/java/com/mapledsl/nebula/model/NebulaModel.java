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
    interface E<ID, EDGE extends Model.E<ID>> extends Model<ID> {
        /**
         * Retrieves an instance of the EDGE class.
         *
         * @return An instance of the EDGE class.
         */
        EDGE instance();

        /**
         * Retrieves the rank of the model. If the rank property is not set, it returns 0L as the default value.
         *
         * @return The rank of the model, or 0L if the rank property is not set.
         */
        default @PropertyGetter(RANK) long rank() {
            return getOrDefault(RANK, 0L);
        }

        /**
         * Sets the rank of the model.
         *
         * @param rank The rank to be set.
         * @return An instance of the EDGE class with the rank property set to the provided value.
         */
        default @PropertySetter(RANK) EDGE setRank(long rank) {
            put(RANK, rank);
            return instance();
        }

        String RANK = "rank";
    }
}
