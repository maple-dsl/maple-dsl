package com.mapledsl.core.module;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.function.Predicate;

public abstract class MapleDslModule {
    public abstract @NotNull String version();
    public abstract @NotNull String dialect();
    public abstract @NotNull Properties dialectProperties();

    public Predicate<Class<?>> resultValuePredicate() {
        return it -> true;
    }

    public <IN, OUT> Predicate<MapleDslResultHandler<IN, OUT>> resultHandlerPredicate() {
        return it -> true;
    }

    public final Predicate<String> versionPredicate() {
        return version()::equalsIgnoreCase;
    }

    public final Predicate<String> dialectPredicate() {
        return dialect()::equalsIgnoreCase;
    }

    @Override
    public String toString() {
        return version();
    }
}
