package com.mapledsl.nebula.module;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.module.MapleDslDuplexModule;
import com.mapledsl.core.module.MapleDslModule;
import com.mapledsl.core.module.MapleDslResultHandler;
import com.vesoft.nebula.Value;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class MapleNebulaDslModule extends MapleDslDuplexModule {
    public static final String VERSION = "0.1.0";
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

    @Override
    public <IN, OUT> Predicate<MapleDslResultHandler<IN, OUT>> resultHandlerPredicate() {
        return it -> it.getClass().isAssignableFrom(MapleNebulaDslResultHandler.class);
    }

    @Override
    public Predicate<Class<?>> resultValuePredicate() {
        return Value.class::equals;
    }
}
