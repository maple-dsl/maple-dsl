package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslReflectionException;
import com.mapledsl.core.extension.func.ThrowingFunction;
import com.mapledsl.core.extension.func.ThrowingSupplier;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * The {@code BeanDefinition} class represents the definition of a bean.
 * It stores information about the bean class, property accessors and writers,
 * and provides methods for creating new instances, accessing properties, and setting properties on the bean.
 *
 * @param <BEAN> the type of the bean
 */
public class BeanDefinition<BEAN> {
    static final Logger LOG = LoggerFactory.getLogger(BeanDefinition.class);
    private final MapleDslConfiguration context;

    /**
     * Variable lookup for retrieving method handles.
     */
    @NotNull private final MethodHandles.Lookup lookup;
    @NotNull private final BeanCreator<BEAN> creator;
    @Nullable private final String label;

    /**
     * The accessor for customizing bean properties.
     * <p></p>
     * This field allows you to define a custom accessor for modifying and accessing bean properties.
     * The accessor should implement the `Bean
     */
    private final BeanPropertyCustomizerAccessor<BEAN> propertyCustomizerAccessor;
    /**
     * A variable that represents a functional interface used for customizing the injection of extension property key and value.
     * <p></p>
     * The {@code propertyCustomizerWriter} is used to customize the injection of extension property key and value for a target bean. It allows the user to specify a custom implementation
     * for setting the property value.
     */
    private final BeanPropertyCustomizerWriter<BEAN> propertyCustomizerWriter;
    /** key: property name, value: property accessor */
    private final Map<String, BeanPropertyAccessor> propertyAccessorMap = new HashMap<>();
    /** key: property name, value: property writer */
    private final Map<String, BeanPropertyWriter> propertyWriterMap = new HashMap<>();
    /** key: accessor name, value: property_name */
    private final Map<String, String> accessorNamePropertyMap = new HashMap<>();
    /** key: property name, value: property_type */
    private final Map<String, Class<?>> propertyTypeMap = new HashMap<>();
    /** item: property name, exclude the fields which annotated with @Property(defined=false) */
    private final Set<String> definedPropertyNameSet = new HashSet<>();

    @SuppressWarnings("unchecked")
    BeanDefinition(@NotNull MapleDslConfiguration context, @NotNull Class<BEAN> beanClazz, @Nullable String label) {
        this.context = context;
        this.label = label;
        this.lookup = LookupSettings.privateLookupIn(beanClazz);
        this.creator = new BeanCreator<>(lookup, beanClazz);

        if (Model.class.isAssignableFrom(beanClazz)) {
            this.propertyCustomizerAccessor = (BeanPropertyCustomizerAccessor<BEAN>) context.modelPropertyCustomizer();
            this.propertyCustomizerWriter = (BeanPropertyCustomizerWriter<BEAN>) context.modelPropertyCustomizer();
        } else {
            this.propertyCustomizerAccessor = context.beanPropertyCustomizer(beanClazz);
            this.propertyCustomizerWriter = context.beanPropertyCustomizer(beanClazz);
        }
    }

    public BEAN newInstance() {
        return requireNonNull(creator, "Missing creator").newInstance();
    }

    public @Nullable String label() {
        return label;
    }

    public @Nullable String propertyName(@NotNull String accessorName) {
        return accessorNamePropertyMap.get(accessorName);
    }

    /**
     * Returns a set of property names for the given bean.
     *
     * @param bean the target bean (nullable)
     * @return a set of property names
     */
    public Set<String> propertyNames(@Nullable BEAN bean) {
        if (bean == null) return Collections.emptySet();
        if (propertyCustomizerAccessor == null) return definedPropertyNameSet;

        final Set<String> propertyKeys = new HashSet<>(this.definedPropertyNameSet);
        propertyKeys.addAll(propertyCustomizerAccessor.propertyKeys(bean, context));

        return propertyKeys;
    }

    /**
     * Diff reflecting the according object to invoke getter method return the value of the property.
     *
     * @param target       the target model to invoke getter method.
     * @param propertyName property name.
     * @return the value of the property as specified string.
     */
    public String getter(BEAN target, String propertyName) {
        if (target == null) return null;

        Object propertyValue = null;
        if (propertyAccessorMap.containsKey(propertyName)) {
            final BeanPropertyAccessor beanPropertyAccessor = propertyAccessorMap.get(propertyName);
            propertyValue = beanPropertyAccessor.delegate.apply(target);
            return context.parameterized(propertyValue);
        }

        if (propertyCustomizerAccessor != null) {
            propertyValue = propertyCustomizerAccessor.getter(target, propertyName, context);
            return context.parameterized(propertyValue);
        }

        return context.parameterized(propertyValue);
    }

    /**
     * Diff reflecting the according object to invoke setter method with given property value.
     * Only use for session query result, creator a model instance then filled the all properties.
     *
     * @param target         the target object to inject into the properties.
     * @param propertyName   property name.
     * @param propertyValue  property value.
     */
    public void setter(BEAN target, String propertyName, Object propertyValue) {
        if (target == null) return;
        if (propertyValue == null) return;

        final Class<?> propertyType = propertyTypeMap.get(propertyName);
        final Object resultant = propertyType == null ? context.resultant(propertyValue) : context.resultant(propertyValue, propertyType);
        if (propertyWriterMap.containsKey(propertyName)) {
            final BeanPropertyWriter beanPropertyWriter = propertyWriterMap.get(propertyName);
            beanPropertyWriter.delegate.accept(target, resultant);
            return;
        }

        if (label != null && LOG.isWarnEnabled()) LOG.warn("Property:{} does not found it writer.", propertyName);
        if (propertyCustomizerWriter != null) propertyCustomizerWriter.setter(target, propertyName, resultant, context);
    }

    void addDefinedBeanPropertyName(String propertyName) {
        if (definedPropertyNameSet.contains(propertyName)) throw new MapleDslBindingException("Duplicate defined property " + propertyName);
        definedPropertyNameSet.add(propertyName);
    }

    boolean containsBeanPropertyType(String propertyName) {
        return propertyTypeMap.containsKey(propertyName);
    }

    void putBeanPropertyType(String propertyName, Class<?> propertyType) {
        if (containsBeanPropertyType(propertyName)) throw new MapleDslBindingException("Duplicate property " + propertyName + ", it type:" + propertyType);
        propertyTypeMap.put(propertyName, propertyType);
    }

    boolean containsBeanPropertyAccessorName(String accessorName) {
        return accessorNamePropertyMap.containsKey(accessorName);
    }

    void putAccessorNameProperty(String accessorName, String propertyName) {
        if (containsBeanPropertyAccessorName(accessorName)) throw new MapleDslBindingException("Duplicate getter name " + accessorName + " with " + propertyName);

        accessorNamePropertyMap.put(accessorName, propertyName);
    }

    boolean containsBeanPropertyAccessor(String propertyName) {
        return propertyAccessorMap.containsKey(propertyName);
    }

    void putBeanPropertyAccessor(String propertyName, @NotNull Method getterMethod) {
        if (containsBeanPropertyAccessor(propertyName)) throw new MapleDslBindingException("Duplicate getter property " + propertyName + " in " + getterMethod);

        BeanPropertyAccessor beanPropertyAccessor = new BeanPropertyAccessor(lookup, getterMethod);
        switch (propertyName) {
            case Model.ID:
            case Model.E.SRC:
            case Model.E.DST: beanPropertyAccessor.override(context.keyPolicyStrategy());
        }

        accessorNamePropertyMap.put(getterMethod.getName(), propertyName);
        propertyAccessorMap.put(propertyName, beanPropertyAccessor);
    }

    boolean containsBeanPropertyWriter(String propertyName) {
        return propertyWriterMap.containsKey(propertyName);
    }

    void putBeanPropertyWriter(String propertyName, @NotNull Method setterMethod) {
        if (containsBeanPropertyWriter(propertyName)) throw new MapleDslBindingException("Duplicate setter property " + propertyName + " in " + setterMethod);

        propertyWriterMap.put(propertyName, new BeanPropertyWriter(lookup, setterMethod));
    }

    /**
     * Allow private access to fields across using a JVM version-appropriate strategy
     * without having a compile-time dependency on Java 9+.
     * <p>
     * This entire class can be replaced by a direct call to JDK 9+
     * {@link MethodHandles}{@code .privateLookupIn}
     */
    public static class LookupSettings {
        public static MethodHandles.Lookup privateLookupIn(Class<?> lookupFor) {
            final String className = lookupFor.getName();
            if (className.startsWith("java.") || className.startsWith("sun.misc.")) {
                throw new MapleDslReflectionException("initialization lookup for class " + className + " is not supported.");
            }
            if (LookupSettings.Java9Up.FACTORY != null) {
                return LookupSettings.Java9Up.privateLookupIn(lookupFor, MethodHandles.lookup());
            }
            return LookupSettings.Java8.privateLookupIn(lookupFor);
        }

        static class Java9Up {
            static MethodHandle FACTORY;

            static {
                try {
                    //noinspection JavaLangInvokeHandleSignature
                    FACTORY = MethodHandles.lookup().findStatic(
                            MethodHandles.class,
                            "privateLookupIn",
                            MethodType.methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class));
                } catch (ReflectiveOperationException e) {
                    if (LOG.isWarnEnabled()) LOG.warn("Unable to find Java 9+ MethodHandles.privateLookupIn.");
                    FACTORY = null;
                }
            }

            public static MethodHandles.Lookup privateLookupIn(Class<?> lookup, MethodHandles.Lookup orig) {
                return ThrowingSupplier.sneaky(() -> (MethodHandles.Lookup) FACTORY.invokeExact(lookup, orig)).get();
            }
        }

        static class Java8 {
            static Constructor<MethodHandles.Lookup> FACTORY;

            static {
                try {
                    FACTORY = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
                    FACTORY.setAccessible(true);
                } catch (ReflectiveOperationException e) {
                    if (LOG.isWarnEnabled()) LOG.warn("Unable to use private Lookup constructor", e);
                    FACTORY = null;
                }
            }

            public static MethodHandles.Lookup privateLookupIn(Class<?> lookup) {
                return ThrowingFunction.sneaky(FACTORY::newInstance).apply(lookup);
            }
        }
    }
}
