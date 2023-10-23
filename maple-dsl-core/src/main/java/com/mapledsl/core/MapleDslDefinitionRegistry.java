package com.mapledsl.core;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.extension.introspect.BeanDefinitionIntrospector;
import com.mapledsl.core.extension.introspect.BeanPropertyCustomizer;
import com.mapledsl.core.extension.introspect.DefaultModelPropertyCustomizer;
import com.mapledsl.core.model.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
@SuppressWarnings({"rawtypes", "unchecked"})
final class MapleDslDefinitionRegistry {
    final MapleDslConfiguration configuration;
   /**
     * key: model class, value: bean definition.
     */
    final Map<Class, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    /**
     * key: model class, value: bean property customizer.
     */
    final Map<Class, BeanPropertyCustomizer> beanPropertyCustomizerMap = new HashMap<>();
    final BeanDefinitionIntrospector beanDefinitionIntrospector;
    BeanPropertyCustomizer<Model<?>> defaultModelPropertyCustomizer = new DefaultModelPropertyCustomizer();

    MapleDslDefinitionRegistry(MapleDslConfiguration configuration) {
        this.configuration = configuration;
        this.beanDefinitionIntrospector = new BeanDefinitionIntrospector(configuration);
    }

    <BEAN> BeanDefinition<BEAN> getBeanDefinition(Class<BEAN> beanClazz) {
        requireNonNull(beanClazz, "bean type must not be null.");
        return beanDefinitionMap.computeIfAbsent(beanClazz, beanDefinitionIntrospector::resolve);
    }

    <BEAN> BeanPropertyCustomizer<BEAN> getBeanPropertyCustomizer(Class<BEAN> beanClazz) {
        requireNonNull(beanClazz, "bean type must not be null.");
        return beanPropertyCustomizerMap.get(beanClazz);
    }

    BeanPropertyCustomizer<Model<?>> getModelPropertyCustomizer() {
        return defaultModelPropertyCustomizer;
    }

    void registerModelPropertyCustomizer(BeanPropertyCustomizer<Model<?>> customizer) {
        requireNonNull(customizer, "customizer must not be null.");
        defaultModelPropertyCustomizer = customizer;
    }

    <BEAN> void registerBeanDefinition(Class<BEAN> beanClazz) {
        requireNonNull(beanClazz, "bean type must not be null.");
        beanDefinitionMap.putIfAbsent(beanClazz, beanDefinitionIntrospector.resolve(beanClazz));
    }

    <BEAM> void registerBeanPropertyCustomizer(Class<BEAM> beanClazz, BeanPropertyCustomizer<BEAM> customizer) {
        requireNonNull(beanClazz, "bean type must not be null.");
        requireNonNull(customizer, "customizer must not be null.");
        if (beanPropertyCustomizerMap.containsKey(beanClazz)) throw new MapleDslBindingException("BeanPropertyCustomizer:" + beanClazz + " PropertyCustomizer is already registered.");

        beanPropertyCustomizerMap.put(beanClazz, customizer);
    }
}
