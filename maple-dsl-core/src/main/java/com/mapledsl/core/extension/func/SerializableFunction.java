package com.mapledsl.core.extension.func;

import com.mapledsl.core.extension.lambda.LambdaMeta;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

/**
 * @author bofa1ex
 * @since 2023/8/14
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
    Map<Class<?>, WeakHashMap<String, String>> INSTANTIATED_PROPERTY_CACHE = new ConcurrentHashMap<>();

    @API(status = API.Status.INTERNAL)
    static void attr(Class<?> clazz, String methodName, String propertyName) {
        INSTANTIATED_PROPERTY_CACHE.compute(clazz,  (ign, innerCache) -> {
            if (innerCache == null) innerCache = new WeakHashMap<>();
            innerCache.put(methodName, propertyName);
            return innerCache;
        });
    }

    @API(status = API.Status.INTERNAL)
    default String asText() {
        @NotNull LambdaMeta meta = LambdaMeta.extract(this);
        @NotNull Class<?> instantiatedClass = meta.getInstantiatedClass();
        @NotNull String implMethodName = meta.getImplMethodName();
        return ofNullable(INSTANTIATED_PROPERTY_CACHE.get(instantiatedClass))
                .map(it -> it.get(implMethodName))
                .orElse(implMethodName);
    }

    @SuppressWarnings("unchecked")
    @API(status = API.Status.INTERNAL)
    default Map.Entry<Class<T>, String> asMeta() {
        @NotNull LambdaMeta meta = LambdaMeta.extract(this);
        @NotNull Class<T> instantiatedClass = (Class<T>) meta.getInstantiatedClass();
        @NotNull String implMethodName = meta.getImplMethodName();
        return new AbstractMap.SimpleImmutableEntry<>(
                instantiatedClass, ofNullable(INSTANTIATED_PROPERTY_CACHE.get(instantiatedClass))
                .map(it -> it.get(implMethodName))
                .orElse(implMethodName)
        );
    }
}
