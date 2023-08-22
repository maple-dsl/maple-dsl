package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class DefaultModelPropertyCustomizer implements BeanPropertyCustomizer<Model<?>> {

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
    public void setter(@NotNull Model<?> bean, @NotNull String propertyKey, @NotNull Object propertyValue, MapleDslConfiguration context) {
        bean.put(propertyKey, propertyValue);
    }

    @Override
    public boolean hasSetter(@NotNull Model<?> bean, @NotNull String propertyKey, MapleDslConfiguration context) {
        return true;
    }

    @Override
    public boolean hasGetter(@NotNull Model<?> bean, @NotNull String propertyKey, MapleDslConfiguration context) {
        return bean.props().containsKey(propertyKey);
    }
}
