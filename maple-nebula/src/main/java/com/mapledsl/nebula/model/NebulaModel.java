package com.mapledsl.nebula.model;

import com.mapledsl.core.annotation.Property;
import com.mapledsl.core.annotation.PropertyGetter;
import com.mapledsl.core.annotation.PropertySetter;
import com.mapledsl.core.model.ID;

public interface NebulaModel {
    /**
     * Base graph EDGE model.
     */
    class E extends com.mapledsl.core.model.Model.E {
        protected @Property(value = RANK, defined = false) long rank;

        public @PropertyGetter(RANK) long rank() {
            return rank;
        }

        public @PropertySetter(RANK) NebulaModel.E setRank(long rank) {
            this.rank = rank;
            return this;
        }

        @Override
        public @PropertySetter(ID) NebulaModel.E setId(ID id) {
            // Ignored, Nebula Edge does not have identity.
            return this;
        }

        @Override
        public NebulaModel.E setLabel(String label) {
            super.setLabel(label);
            return this;
        }

        @Override
        public NebulaModel.E setId(String id) {
            super.setId(id);
            return this;
        }

        @Override
        public NebulaModel.E setId(Number id) {
            super.setId(id);
            return this;
        }

        @Override
        public NebulaModel.E setSrc(String id) {
            super.setSrc(id);
            return this;
        }

        @Override
        public NebulaModel.E setSrc(ID src) {
            super.setSrc(src);
            return this;
        }

        @Override
        public NebulaModel.E setSrc(Number id) {
            super.setSrc(id);
            return this;
        }

        @Override
        public NebulaModel.E setDst(String id) {
            super.setDst(id);
            return this;
        }

        @Override
        public NebulaModel.E setDst(ID dst) {
            super.setDst(dst);
            return this;
        }

        @Override
        public NebulaModel.E setDst(Number id) {
            super.setDst(id);
            return this;
        }

        @Override
        public String toString() {
            return "#" +  src + "->" + dst + "@" + rank;
        }

        public static final String RANK = "rank";

        public static NebulaModel.E of(ID id, ID src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, ID src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, ID src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, String src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, String src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, String src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, Number src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, Number src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(ID id, Number src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, ID src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, ID src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, ID src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, String src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, String src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, String src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, Number src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, Number src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(String id, Number src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, ID src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, ID src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, ID src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, String src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, String src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, String src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, Number src, ID dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, Number src, String dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }

        public static NebulaModel.E of(Number id, Number src, Number dst, long rank) {
            return new NebulaModel.E().setId(id).setSrc(src).setDst(dst).setRank(rank);
        }
    }
}
