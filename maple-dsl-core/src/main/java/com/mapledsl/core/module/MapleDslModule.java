package com.mapledsl.core.module;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public class MapleDslModule {
    @NotNull private final Class<? extends MapleDslResultHandler<?,?>> resultHandlerClazz;
    @NotNull private final Class<?> resultValueClazz;
    @NotNull private final String version;

    public MapleDslModule(Class<?> resultValueClazz, Class<? extends MapleDslResultHandler<?,?>> resultHandlerClazz, String version) {
        this.resultValueClazz = Objects.requireNonNull(resultValueClazz);
        this.resultHandlerClazz = Objects.requireNonNull(resultHandlerClazz);
        this.version = Objects.requireNonNull(version);
    }

    public Predicate<Class<?>> resultValuePredicate() {
        return resultValueClazz::equals;
    }

    public Predicate<Class<? extends MapleDslResultHandler<?,?>>> resultHandlerPredicate() {
        return resultHandlerClazz::isAssignableFrom;
    }

    public Predicate<String> versionPredicate() {
        return version::equalsIgnoreCase;
    }

    @Override
    public String toString() {
        return version;
    }
}
