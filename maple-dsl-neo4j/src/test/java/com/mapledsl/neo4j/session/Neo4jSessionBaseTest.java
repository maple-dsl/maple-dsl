package com.mapledsl.neo4j.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.session.MapleDslSessionTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Config;
import org.neo4j.driver.SessionConfig;

import java.util.concurrent.TimeUnit;

public class Neo4jSessionBaseTest {
    static {
        MapleDslConfiguration.primaryConfiguration(it -> it.keyPolicyStrategy(KeyPolicyStrategies.MANUAL));
    }

    static final MapleNeo4jDslSessionFactory mapleNeo4jDslSessionFactory = new MapleNeo4jDslSessionFactory(MapleDslConfiguration.primaryConfiguration(),
            "bolt://localhost:7687",
            AuthTokens.basic("neo4j", "password"),
            Config.builder()
                    .withConnectionTimeout(10, TimeUnit.SECONDS)
                    .withMaxConnectionLifetime(30, TimeUnit.MINUTES)
                    .withMaxConnectionPoolSize(10)
                    .withConnectionAcquisitionTimeout(20, TimeUnit.SECONDS)
                    .build(),
            SessionConfig.builder().withDatabase("maple").build()
    );

    static final MapleNeo4jDslSessionFactory mapleHashNeo4jDslSessionFactory = new MapleNeo4jDslSessionFactory(MapleDslConfiguration.primaryConfiguration(),
            "bolt://localhost:7687",
            AuthTokens.basic("neo4j", "password"),
            Config.builder()
                    .withConnectionTimeout(10, TimeUnit.SECONDS)
                    .withMaxConnectionLifetime(30, TimeUnit.MINUTES)
                    .withMaxConnectionPoolSize(10)
                    .withConnectionAcquisitionTimeout(20, TimeUnit.SECONDS)
                    .build(),
            SessionConfig.builder().withDatabase("maplenum").build()
    );

    static final MapleDslSessionTemplate sessionTemplate = MapleDslSessionTemplate.newInstance(mapleNeo4jDslSessionFactory);
    static final MapleDslSessionTemplate hashSessionTemplate = MapleDslSessionTemplate.newInstance(mapleHashNeo4jDslSessionFactory);


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
    public static class Impact extends Model.E<String, String> {
        private String type;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Label("impact")
    public static class ImpactHash extends Model.E<Long, Long> {
        private String type;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Label("follow")
    public static class Follow extends Model.E<String, String> {
    }
}
