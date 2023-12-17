package com.mapledsl.cypher.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.extension.KeyPolicyStrategy;
import com.mapledsl.core.module.MapleDslModule;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The class {@code MapleCypherDslModule} is a subclass of {@code MapleDslModule} that represents a Maple Cypher DSL module.
 * It provides methods to get the version, dialect, and dialect properties specific to the Maple Cypher DSL module.
 */
public class MapleCypherDslModule extends MapleDslModule {
    public static final String VERSION = "cypher:0.1.0-release";
    public static final String DIALECT = "cypher";

    @Override
    public KeyPolicyStrategy keyPolicyStrategy() {
        return KeyPolicyStrategies.INTERNAL;
    }

    @Override
    public @NotNull String version() {
        return VERSION;
    }

    @Override
    public @NotNull String dialect() {
        return DIALECT;
    }

    public static boolean useInternalId(MapleDslConfiguration context) {
        return context.keyPolicyStrategy() == KeyPolicyStrategies.INTERNAL;
    }

    private InputStream getDialectTemplatePropertiesInputStream(MapleDslConfiguration context) {
        return MapleCypherDslModule.class.getClassLoader().getResourceAsStream(
                useInternalId(context) ? "META-INF/cypher-internal-dialect-render.properties" : "META-INF/cypher-dialect-render.properties"
        );
    }

    @Override
    public @NotNull Properties dialectProperties(MapleDslConfiguration context) {
        final Properties dialectTemplateProperties = new Properties();

        try (@NotNull InputStream is = getDialectTemplatePropertiesInputStream(context)) {
            dialectTemplateProperties.load(is);
        } catch (IOException e) {
            throw new MapleDslBindingException("Loading cypher-dialect render properties failed", e);
        }

        return dialectTemplateProperties;
    }
}
