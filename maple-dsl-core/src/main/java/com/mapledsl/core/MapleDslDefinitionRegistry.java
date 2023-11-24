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
@SuppressWarnings({"unchecked", "rawtypes"})
final class MapleDslDefinitionRegistry {
    final MapleDslConfiguration configuration;
   /**
     * key: model class, value: bean definition.
     */
    final Map<Class<?>, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    /**
     * key: model class, value: bean property customizer.
     */
    final Map<Class<?>, BeanPropertyCustomizer> beanPropertyCustomizerMap = new HashMap<>();
    final BeanDefinitionIntrospector beanDefinitionIntrospector;
    BeanPropertyCustomizer defaultModelPropertyCustomizer = new DefaultModelPropertyCustomizer();

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

    <MODEL extends Model<?>> BeanPropertyCustomizer<MODEL> getModelPropertyCustomizer() {
        return defaultModelPropertyCustomizer;
    }

    <MODEL extends Model<?>> void registerModelPropertyCustomizer(BeanPropertyCustomizer<MODEL> customizer) {
        requireNonNull(customizer, "customizer must not be null.");
        defaultModelPropertyCustomizer = customizer;
    }

    <BEAN> void registerBeanDefinition(Class<BEAN> beanClazz) {
        requireNonNull(beanClazz, "bean type must not be null.");
        beanDefinitionMap.putIfAbsent(beanClazz, beanDefinitionIntrospector.resolve(beanClazz));
    }

    <BEAM> void registerBeanPropertyCustomizer(BeanPropertyCustomizer<BEAM> customizer) {
        requireNonNull(customizer, "customizer must not be null.");
        requireNonNull(customizer.beanClazz(), "bean type must not be null.");
        if (beanPropertyCustomizerMap.containsKey(customizer.beanClazz()))
            throw new MapleDslBindingException("BeanPropertyCustomizer:" + customizer.beanClazz() + " PropertyCustomizer is already registered.");

        beanPropertyCustomizerMap.put(customizer.beanClazz(), customizer);
    }
}
