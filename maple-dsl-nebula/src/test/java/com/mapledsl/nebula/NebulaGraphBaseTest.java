package com.mapledsl.nebula;

import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.model.Model;
import com.mapledsl.nebula.model.NebulaModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class NebulaGraphBaseTest {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Label("person")
    public static class Person extends Model.V<String> {
        private String name;
        private Integer age;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Label("person")
    public static class PersonHash extends Model.V<Long> {
        private String name;
        private Integer age;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Label("impact")
    public static class Impact extends Model.E<String> implements NebulaModel.E<String, Impact> {
        private String type;

        @Override
        public Impact instance() {
            return this;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Label("impact")
    public static class ImpactHash extends Model.E<Long> implements NebulaModel.E<Long, ImpactHash> {
        private String type;

        @Override
        public ImpactHash instance() {
            return this;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Label("follow")
    public static class Follow extends Model.E<String> implements NebulaModel.E<String, Follow> {
        @Override
        public Follow instance() {
            return this;
        }
    }
}
