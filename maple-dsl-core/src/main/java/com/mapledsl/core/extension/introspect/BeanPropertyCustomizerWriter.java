package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * The BeanPropertyCustomizerWriter interface defines a method for customizing the injection of extension property
 * key and value into a target bean. It is a functional interface.
 *
 * @param <T> the type of the bean being customized
 */
@FunctionalInterface
interface BeanPropertyCustomizerWriter<T> {

    /**
     * Customize the injection of extension property key and value.
     * @param bean the target bean.
     * @param propertyKey the property key which exclude the basic properties.
     * @param propertyValue the property value.
     * @param context the context include(feature,namingStrategy,etc).
     */
    void setter(@NotNull T bean, @NotNull String propertyKey, Object propertyValue, MapleDslConfiguration context);
}
