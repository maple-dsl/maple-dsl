package com.mapledsl.spring.autoconfigure;

import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.extension.NamingStrategies;
import com.mapledsl.core.module.MapleDslModule;
import com.mapledsl.nebula.module.MapleNebulaDslModule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.Locale;

@Data
@ConfigurationProperties(
        prefix = "spring.maple-dsl"
)
public class MapleDslProperties {
    private Locale locale;

    private String dateFormat;
    private String dateTimeFormat;
    private String timeFormat;

    private NamingStrategies namingStrategy;
    private KeyPolicyStrategies keyPolicyStrategy;

    @NestedConfigurationProperty
    private TemplateObjectPoolConfig templateConfig;
    @NestedConfigurationProperty
    private SessionFactoryConfig sessionFactoryConfig;
    @NestedConfigurationProperty
    private NebulaDataSourceConfig nebulaDataSourceConfig;

    private Boolean isPrimaryConfiguration;

    @Data
    public static class TemplateObjectPoolConfig {
        private Integer maxTotal;
        private Integer maxIdle;
        private Integer minIdle;
    }

    @Data
    public static class SessionFactoryConfig {

    }

    @Data
    public static class NebulaDataSourceConfig {
        private List<String> hosts;
        private String space;
        private String username;
        private String password;
        private final Class<? extends MapleDslModule> moduleClazz = MapleNebulaDslModule.class;
    }

    @Data
    public static class Neo4jDataSourceConfig {

    }
}
