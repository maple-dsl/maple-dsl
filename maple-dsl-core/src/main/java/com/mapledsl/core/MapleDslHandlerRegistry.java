package com.mapledsl.core;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.module.MapleDslModule;
import com.mapledsl.core.module.MapleDslParameterHandler;
import com.mapledsl.core.module.MapleDslParameterHandlerCollector;
import com.mapledsl.core.module.MapleDslResultHandler;
import com.mapledsl.core.module.MapleDslResultHandlerCollector;
import com.mapledsl.core.spi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
final class MapleDslHandlerRegistry {
    final MapleDslConfiguration context;
    /**
     * key: field type, value: parameter handler.
     */
    final Map<Class<?>, MapleDslParameterHandler> parameterHandlerMap;
    /**
     * key: field type, value: result handler.
     */
    final Map<Class<?>, MapleDslResultHandler<?,?>> resultHandlerMap;
    /**
     * Extension properties is not make sure of the specific field type, so needs a default result handler to process it.
     * Diff the other result handler, it returns object type in direct.
     */
    final MapleDslResultHandler<?,?> defaultResultHandler;
    /**
     * Processed for NULL parameter value
     * @see com.mapledsl.core.extension.introspect.BeanDefinition#getter(Object, String)
     * @see com.mapledsl.core.extension.introspect.BeanDefinition#parameterized(String, Object)
     */
    final MapleDslParameterHandler nullParameterHandler;

    MapleDslHandlerRegistry(@NotNull MapleDslConfiguration context) {
        this.context = context;
        final @NotNull MapleDslModule module = context.module();
        final Predicate<String> versionPredicate = module.versionPredicate();

        this.parameterHandlerMap = StreamSupport.stream(ServiceLoader.load(MapleDslParameterHandlerCollector.class).spliterator(), false)
                .filter(Objects::nonNull)
                .filter(it -> versionPredicate.test(it.version()))
                .findFirst()
                .map(Supplier::get)
                .orElseThrow(() -> new MapleDslBindingException(String.format("The module version:[%s] of the parameter handler collector does not found.", module)));

        this.resultHandlerMap = StreamSupport.stream(ServiceLoader.load(MapleDslResultHandlerCollector.class).spliterator(), false)
                .filter(Objects::nonNull)
                .filter(it -> versionPredicate.test(it.version()))
                .findFirst()
                .map(Supplier::get)
                .orElseThrow(() -> new MapleDslBindingException(String.format("The module version:[%s] of the result handler collector does not found.", module)));

        // parameter handler collector must contain the null parameter handler(void.class).
        if (!parameterHandlerMap.containsKey(void.class)) throw new MapleDslBindingException("Missing null parameter handler, Please check the related-dependency has been configured.");
        this.nullParameterHandler = parameterHandlerMap.remove(void.class);

        // result handler collector must contain the default handler(Object.class).
        if (!resultHandlerMap.containsKey(Object.class)) throw new MapleDslBindingException("Missing the default handler, Please check the related-dependency has been configured.");
        this.defaultResultHandler = resultHandlerMap.remove(Object.class);
    }

    void registerParameterHandler(Class<?> parameterType, MapleDslParameterHandler parameterHandler) {
        requireNonNull(parameterType, "parameterType must not be null");
        requireNonNull(parameterHandler, "parameterHandler must not be null");
        parameterHandlerMap.put(parameterType, parameterHandler);
    }

    <IN, OUT> void registerResultHandler(Class<OUT> resultType, MapleDslResultHandler<IN, OUT> resultHandler) {
        requireNonNull(resultType, "resultType must not be null");
        requireNonNull(resultHandler, "resultHandler must not be null");
        final @NotNull MapleDslModule module = context.module();
        if (!module.resultHandlerPredicate().test(resultHandler.getClass())) throw new MapleDslBindingException("ResultHandler predicate does not passed, please keep the related-module specification.");

        resultHandlerMap.put(resultType, resultHandler);
    }

    MapleDslParameterHandler getParameterHandler(Class<?> parameterType) {
        return parameterHandlerMap.get(parameterType);
    }

    MapleDslResultHandler<?, ?> getResultHandler(Class<?> resultType) {
        return resultHandlerMap.getOrDefault(resultType, defaultResultHandler);
    }

    MapleDslParameterHandler getNullParameterHandler() {
        return nullParameterHandler;
    }

    MapleDslResultHandler<?,?> getDefaultResultHandler() {
        return defaultResultHandler;
    }
}
