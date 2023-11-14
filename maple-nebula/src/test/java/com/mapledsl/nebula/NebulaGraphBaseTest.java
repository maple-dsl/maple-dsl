package com.mapledsl.nebula;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.model.Model;
import com.mapledsl.nebula.model.NebulaModel;
import org.junit.jupiter.api.BeforeAll;

public class NebulaGraphBaseTest {

    @BeforeAll
    public static void init() {
        MapleDslConfiguration.primaryConfiguration(MapleDslConfiguration.Builder::templatePrettyPrint)
                .registerBeanDefinition("com.mapledsl.nebula");
    }

    @Label("person")
    public static class Person extends Model.V {
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
    public static class Impact extends NebulaModel.E {
    }

    @Label("follow")
    public static class Follow extends NebulaModel.E {
    }
}
