package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * This class is an implementation of the ModelPropertyCustomizer interface.
 * It provides methods for customizing the properties of a Model object.
 */
public class DefaultModelPropertyCustomizer implements ModelPropertyCustomizer {

    @Override
    public Set<String> propertyKeys(@NotNull Model<?> bean, MapleDslConfiguration context) {
        final @NotNull Map<String, Object> modelPropertyMap = bean.props();
        if (modelPropertyMap.isEmpty()) return Collections.emptySet();

        return modelPropertyMap.keySet();
    }

    @Override
    public Object getter(@NotNull Model<?> bean, @NotNull String propertyKey, MapleDslConfiguration context) {
        return bean.get(propertyKey);
    }

    @Override
    public void setter(@NotNull Model<?> bean, @NotNull String propertyKey, Object propertyValue, MapleDslConfiguration context) {
        bean.put(propertyKey, propertyValue);
    }

    @Override
    public String toString() {
        return "DefaultModelPropertyCustomizer";
    }
}
