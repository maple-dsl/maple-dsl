package com.mapledsl.nebula.module;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.module.MapleDslModule;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Maple DSL module for the Nebula dialect.
 */
public class MapleNebulaDslModule extends MapleDslModule {
    public static final String VERSION = "nebula:0.1.0-release";
    public static final String DIALECT = "nebula";

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
        try (InputStream is = MapleNebulaDslModule.class.getClassLoader().getResourceAsStream("META-INF/nebula-dialect-render.properties")) {
            dialectTemplateProperties.load(requireNonNull(is, "META-INF/nebula-dialect-render.properties not found."));
        } catch (IOException e) {
            throw new MapleDslBindingException("Loading nebula-dialect render properties failed", e);
        }

        return dialectTemplateProperties;
    }
}
