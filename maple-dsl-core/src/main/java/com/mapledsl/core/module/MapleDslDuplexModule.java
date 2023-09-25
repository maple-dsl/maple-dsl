package com.mapledsl.core.module;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class MapleDslDuplexModule extends MapleDslModule {
    @SuppressWarnings("rawtypes")
    public abstract @NotNull Class<MapleDslResultHandler> resultHandlerClazz();
    public abstract @NotNull Class<?> resultValueClazz();

    @Override
    public Predicate<Class<?>> resultValuePredicate() {
        return resultValueClazz()::equals;
    }

    @Override
    public <IN, OUT> Predicate<MapleDslResultHandler<IN, OUT>> resultHandlerPredicate() {
        return it -> it.getClass().isAssignableFrom(resultHandlerClazz());
    }
}
