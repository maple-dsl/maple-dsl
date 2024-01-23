package com.mapledsl.cypher;

import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.model.Model;

public class CypherBaseTest {

    @Label("person")
    public static class Person extends Model.V<Long> {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }

    @Label("impact")
    public static class Impact extends Model.E<Long, Long> {
        private String type;

        public String getType() {
            return type;
        }
    }

    @Label("follow")
    public static class Follow extends Model.E<Long, Long> {
    }

}
