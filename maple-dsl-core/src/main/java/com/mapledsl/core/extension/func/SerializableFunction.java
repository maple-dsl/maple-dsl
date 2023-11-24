package com.mapledsl.core.extension.func;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.lambda.LambdaMeta;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author bofa1ex
 * @since 2023/8/14
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {

    @API(status = API.Status.INTERNAL)
    default String asText() {
        @NotNull LambdaMeta meta = LambdaMeta.extract(this);
        @NotNull Class<?> instantiatedClass = meta.getInstantiatedClass();
        @NotNull String implMethodName = meta.getImplMethodName();

        return MapleDslConfiguration.primaryConfiguration()
                .beanDefinition(instantiatedClass)
                .property(implMethodName);
    }
}
