package com.mapledsl.cypher.module;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.module.MapleDslModule;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class MapleCypherDslModule extends MapleDslModule {
    public static final String VERSION = "0.1.0-release";
    public static final String DIALECT = "cypher";

    @Override
    public @NotNull String version() {
        return VERSION;
    }

    @Override
    public @NotNull String dialect() {
        return DIALECT;
    }

    @Override
    public @NotNull Properties dialectProperties() {
        final Properties dialectTemplateProperties = new Properties();
        try (InputStream is = MapleCypherDslModule.class.getClassLoader().getResourceAsStream("META-INF/cypher-dialect-render.properties")) {
            dialectTemplateProperties.load(requireNonNull(is, "META-INF/cypher-dialect-render.properties not found."));
        } catch (IOException e) {
            throw new MapleDslBindingException("Loading cypher-dialect render properties failed", e);
        }

        return dialectTemplateProperties;
    }
}
