package com.mapledsl.cypher;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.model.Model;

public class CypherManualBaseTest {

    @Label("person")
    public static class Person extends Model.V<String> {
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
    public static class Impact extends Model.E<String> {
        private String type;

        public String getType() {
            return type;
        }
    }

    @Label("impact_fork")
    public static class ImpactNum extends Model.E<Long> {
        private String type;

        public String getType() {
            return type;
        }
    }

    @Label("follow")
    public static class Follow extends Model.E<String> {
    }

    static MapleDslConfiguration configuration = new MapleDslConfiguration.Builder()
            .keyPolicyStrategy(KeyPolicyStrategies.MANUAL)
            .build();
}
