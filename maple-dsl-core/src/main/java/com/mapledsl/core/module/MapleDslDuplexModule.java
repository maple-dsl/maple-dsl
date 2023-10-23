package com.mapledsl.core.module;

import java.util.function.Predicate;

public abstract class MapleDslDuplexModule extends MapleDslModule {
    public abstract <IN, OUT> Predicate<MapleDslResultHandler<IN, OUT>> resultHandlerPredicate();
    public abstract Predicate<Class<?>> resultValuePredicate();
}
