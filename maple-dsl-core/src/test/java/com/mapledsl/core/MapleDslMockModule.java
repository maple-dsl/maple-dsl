package com.mapledsl.core;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.module.MapleDslModule;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public class MapleDslMockModule extends MapleDslModule {
    public static final String VERSION = "mock:1.0.2";
    public static final String DIALECT = "mock";

    @Override
    public @NotNull String version() {
        return VERSION;
    }

    @Override
    public @NotNull String dialect() {
        return DIALECT;
    }

    @Override
    public @NotNull Properties dialectProperties(MapleDslConfiguration context) {
        final Properties dialectTemplateProperties = new Properties();
        try (InputStream is = MapleDslMockModule.class.getClassLoader().getResourceAsStream("META-INF/dialect-render.properties")) {
            dialectTemplateProperties.load(requireNonNull(is, "META-INF/dialect-render.properties not found."));
        } catch (IOException e) {
            throw new MapleDslBindingException("Loading dialect render properties failed", e);
        }

        return dialectTemplateProperties;
    }
}
