package com.mapledsl.core;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslUncheckedException;
import com.mapledsl.core.extension.introspect.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * The MapleDslDefinitionRegistry class is responsible for managing and storing the bean definitions and customizers used in the Maple DSL configuration.
 * It implements the registry pattern and provides methods to retrieve and register bean definitions and customizers.
 */
final class MapleDslDefinitionRegistry {
    static final Logger LOG = LoggerFactory.getLogger(MapleDslConfiguration.class);
    /**
     * key: bean class, value: bean definition.
     */
    private final Map<Class<?>, BeanDefinition<?>> beanDefinitionMap = new ConcurrentHashMap<>();
    /**
     * key: model class, value: bean property customizer.
     */
    private final Map<Class<?>, BeanPropertyCustomizer<?>> beanPropertyCustomizerMap = new ConcurrentHashMap<>();
    /**
     * Represents an instance of BeanDefinitionIntrospector for introspecting bean definitions.
     */
    private final BeanDefinitionIntrospector beanDefinitionIntrospector;
    /**
     * The default property customizer for model properties.
     * It implements the ModelPropertyCustomizer interface.
     * It provides methods to customize the behavior of model properties.
     */
    private ModelPropertyCustomizer defaultModelPropertyCustomizer = new DefaultModelPropertyCustomizer();

    /**
     * Represents a registry for Maple DSL definitions.
     */
    MapleDslDefinitionRegistry(MapleDslConfiguration context) {
        this.beanDefinitionIntrospector = new BeanDefinitionIntrospector(context);
    }

    /**
     * Retrieves the bean definition for the given bean class.
     *
     * @param beanClazz the class of the bean
     * @param <BEAN> the type of the bean
     * @return the bean definition for the given bean class
     * @throws MapleDslUncheckedException if the bean class is null, an interface, a primitive type,
     *         an array, a subclass of Number, CharSequence, Boolean, Collection, or Map
     */
    @SuppressWarnings("unchecked")
    <BEAN> @NotNull BeanDefinition<BEAN> getBeanDefinition(Class<BEAN> beanClazz) {
        if (beanClazz == null) throw new MapleDslUncheckedException("Bean clazz must not be null.");
        if (beanClazz.getName().startsWith("java.") || beanClazz.getName().startsWith("sun.misc.")) throw new MapleDslUncheckedException("initialization lookup for class " + beanClazz.getName() + " is not supported.");
        if (beanClazz.isInterface()) throw new MapleDslUncheckedException("Interface does not have bean definition.");
        if (beanClazz.isPrimitive()) throw new MapleDslUncheckedException("Primitive does not have bean definition.");
        if (beanClazz.isArray()) throw new MapleDslUncheckedException("Array does not have bean definition.");
        if (Number.class.isAssignableFrom(beanClazz)) throw new MapleDslUncheckedException("Number does not have bean definition.");
        if (CharSequence.class.isAssignableFrom(beanClazz)) throw new MapleDslUncheckedException("CharSequence does not have bean definition.");
        if (Boolean.class.isAssignableFrom(beanClazz)) throw new MapleDslUncheckedException("Boolean does not have bean definition.");
        if (Collection.class.isAssignableFrom(beanClazz)) throw new MapleDslUncheckedException("Collection does not have bean definition.");
        if (Map.class.isAssignableFrom(beanClazz)) throw new MapleDslUncheckedException("Map does not have bean definition.");

        return (BeanDefinition<BEAN>) beanDefinitionMap.computeIfAbsent(beanClazz, beanDefinitionIntrospector::resolve);
    }

    /**
     * Retrieves the BeanPropertyCustomizer for the specified bean class.
     *
     * @param beanClazz the class of the bean
     * @param <BEAN> the type of the bean
     * @return the BeanPropertyCustomizer for the specified bean class
     * @throws NullPointerException if the beanClazz is null
     */
    @SuppressWarnings("unchecked")
    <BEAN> BeanPropertyCustomizer<BEAN> getBeanPropertyCustomizer(Class<BEAN> beanClazz) {
        requireNonNull(beanClazz, "bean type must not be null.");
        return (BeanPropertyCustomizer<BEAN>) beanPropertyCustomizerMap.get(beanClazz);
    }

    /**
     * Retrieves the ModelPropertyCustomizer for bean properties in the Maple DSL configuration.
     *
     * @return The ModelPropertyCustomizer instance.
     */
    @NotNull ModelPropertyCustomizer getModelPropertyCustomizer() {
        return defaultModelPropertyCustomizer;
    }

    /**
     * Registers a {@link ModelPropertyCustomizer} for bean properties in the Maple DSL configuration.
     *
     * @param customizer The {@link ModelPropertyCustomizer} to register.
     */
    void registerModelPropertyCustomizer(ModelPropertyCustomizer customizer) {
        requireNonNull(customizer, "customizer must not be null.");
        if (LOG.isWarnEnabled()) LOG.warn("{} Model property customizer will be override.", defaultModelPropertyCustomizer.toString());
        defaultModelPropertyCustomizer = customizer;
    }

    /**
     * Registers a customizer for bean properties in the Maple DSL configuration.
     *
     * @param customizer the BeanPropertyCustomizer to register
     * @throws NullPointerException      if the customizer is null
     * @throws NullPointerException      if the beanClazz of the customizer is null
     * @throws MapleDslBindingException  if a BeanPropertyCustomizer for the same beanClazz is already registered
     */
    void registerBeanPropertyCustomizer(BeanPropertyCustomizer<?> customizer) {
        requireNonNull(customizer, "customizer must not be null.");
        requireNonNull(customizer.beanClazz(), "bean type must not be null.");
        if (beanPropertyCustomizerMap.containsKey(customizer.beanClazz()))
            throw new MapleDslBindingException("BeanPropertyCustomizer:" + customizer.beanClazz() + " PropertyCustomizer is already registered.");

        beanPropertyCustomizerMap.put(customizer.beanClazz(), customizer);
    }
}
