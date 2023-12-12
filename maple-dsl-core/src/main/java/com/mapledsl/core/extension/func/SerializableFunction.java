package com.mapledsl.core.extension.func;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.lambda.LambdaMeta;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Function;

/**
 * This is a functional interface representing a serializable function. It extends the Function interface from the java.util.function package and the Serializable interface.
 *
 * <p>This interface is used to define functions that can be serialized and deserialized. It provides the asText() method, which returns the name of the implementation method for
 * the function when it is serialized as text.
 *
 * <p>Example usage:
 * <pre>{@code
 *     SerializableFunction<Integer, String> func = (x) -> x.toString();
 *     String result = func.apply(10); // result is "10"
 *     String methodName = func.asText(); // methodName is the implementation method name for the function
 * }</pre>
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @see Function
 * @see Serializable
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {

    /**
     * Retrieves the name of the implementation method for the SerializableFunction when serialized as text.
     *
     * @return The name of the implementation method as a String.
     */
    @API(status = API.Status.INTERNAL)
    default String asText() {
        @NotNull LambdaMeta meta = LambdaMeta.extract(this);
        @NotNull Class<?> instantiatedClass = meta.getInstantiatedClass();
        @NotNull String implMethodName = meta.getImplMethodName();

        return MapleDslConfiguration.primaryConfiguration()
                .beanDefinitionUnchecked(instantiatedClass)
                .propertyName(implMethodName);
    }
}
