package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
@API(status = API.Status.STABLE)
public interface BeanPropertyCustomizer<T> {
    Class<T> beanClazz();
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

    /**
     * Customize the injection of extension property key and value.
     * @param bean the target bean.
     * @param propertyKey the property key which exclude the basic properties.
     * @param propertyValue the property value.
     * @param context the context include(feature,namingStrategy,etc).
     */
    void setter(@NotNull T bean, @NotNull String propertyKey, @NotNull Object propertyValue, MapleDslConfiguration context);

    /**
     * Customize the injection of extension property key and value
     * @param bean the target bean.
     * @param propertyKey the property key which exclude the basic properties.
     * @param context the context include(feature,namingStrategy,etc).
     * @return the extension property
     */
    boolean hasSetter(@NotNull T bean, @NotNull String propertyKey, MapleDslConfiguration context);

    /**
     * Customize the injection of extension property key and value
     * @param bean the target bean.
     * @param propertyKey the property key which exclude the basic properties.
     * @param context the context include(feature,namingStrategy,etc).
     * @return the extension property
     */
    boolean hasGetter(@NotNull T bean, @NotNull String propertyKey, MapleDslConfiguration context);
}
