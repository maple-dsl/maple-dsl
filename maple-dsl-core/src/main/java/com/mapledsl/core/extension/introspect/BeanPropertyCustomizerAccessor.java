package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Interface for accessing and customizing bean property handling.
 *
 * @param <T> the type of the bean being customized
 */
interface BeanPropertyCustomizerAccessor<T> {
    /**
     * Exposed the extension property keys.
     * @param bean the target bean.
     * @param context the context include(feature,namingStrategy,etc).
     * @return the extension property keys.
     */
    Set<String> propertyKeys(@NotNull T bean, MapleDslConfiguration context);
    /**
     * Customize the extraction of extension property value.
     * @param bean the target bean.
     * @param propertyKey the property key which exclude the basic properties.
     * @param context the context include(feature,namingStrategy,etc).
     * @return the extension property value.
     */
    Object getter(@NotNull T bean, @NotNull String propertyKey, MapleDslConfiguration context);
}
