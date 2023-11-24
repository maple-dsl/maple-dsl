package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslReflectionException;
import com.mapledsl.core.extension.func.ThrowingFunction;
import com.mapledsl.core.extension.func.ThrowingSupplier;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.module.MapleDslParameterHandler;
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
 * @author bofa1ex
 * @since 2023/08/22
 */
@SuppressWarnings("unchecked")
public class BeanDefinition<BEAN> {
    static final Logger LOG = LoggerFactory.getLogger(BeanDefinition.class);
    private final MapleDslConfiguration context;

    @NotNull private final MethodHandles.Lookup lookup;
    @NotNull private final BeanCreator<BEAN> creator;
    @NotNull private final Class<BEAN> beanClazz;
    @Nullable private final String label;

    @SuppressWarnings("rawtypes")
    private final BeanPropertyCustomizer propertyCustomizer;
    /** key: property name, value: property accessor */
    private final Map<String, BeanPropertyAccessor> propertyAccessorMap = new HashMap<>();
    /** key: property name, value: property writer */
    private final Map<String, BeanPropertyWriter> propertyWriterMap = new HashMap<>();
    /** item: property name, exclude the fields which annotated with @Property(defined=false) */
    private final Set<String> excludeNonDefinedPropertyKeySet = new HashSet<>();
    /** key: getter_method_name, value: property_name */
    private final Map<String, String> propertyGetterImplMethodNameMap = new LinkedHashMap<>();

    public BeanDefinition(MapleDslConfiguration context, Class<BEAN> beanClazz, @Nullable String label) {
        this.context = context;
        this.label = label;
        this.beanClazz = beanClazz;
        this.propertyCustomizer = beanClazz.isAssignableFrom(Model.class) ? context.modelPropertyCustomizer() : context.beanPropertyCustomizer(beanClazz);
        this.lookup = LookupSettings.privateLookupIn(beanClazz);
        this.creator = new BeanCreator<>(lookup, beanClazz);
    }

    public @Nullable String label() {
        return label;
    }

    public BEAN newInstance() {
        return requireNonNull(creator, "Missing creator").newInstance();
    }

    public Set<String> propertyKeys(@NotNull BEAN bean) {
        if (propertyCustomizer == null) return excludeNonDefinedPropertyKeySet;
        final Set<String> propertyKeys = new HashSet<>(this.excludeNonDefinedPropertyKeySet);
        propertyKeys.addAll(propertyCustomizer.propertyKeys(bean, context));
        return propertyKeys;
    }

    public String property(String getterImplMethodName) {
        return propertyGetterImplMethodNameMap.get(getterImplMethodName);
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
        if (propertyAccessorMap.containsKey(propertyName)) {
            final BeanPropertyAccessor beanPropertyAccessor = propertyAccessorMap.get(propertyName);
            final Object getterValue = beanPropertyAccessor.getter(target);
            return beanPropertyAccessor.parameterized(getterValue);
        }

        if (propertyCustomizer != null) {
            Object propertyValue = propertyCustomizer.getter(target, propertyName, context);
            final MapleDslParameterHandler parameterHandler = context.parameterHandler(propertyValue.getClass());
            if (parameterHandler != null) return parameterHandler.apply(propertyValue, context);
        }

        return context.nullParameterHandler().apply(null, context);
    }

    /**
     * Diff {@link #getter(Object, String)}, it only parameterized the target value according the propertyName.
     * @param propertyName   property name.
     * @param propertyValue  property value, need to parameterized it.
     * @return the parameterized value of the property value.
     */
    public String parameterized(String propertyName, Object propertyValue) {
        if (propertyValue == null) return null;
        if (propertyAccessorMap.containsKey(propertyName)) {
            final BeanPropertyAccessor beanPropertyAccessor = propertyAccessorMap.get(propertyName);
            return beanPropertyAccessor.parameterized(propertyValue);
        }

        final MapleDslParameterHandler parameterHandler = context.parameterHandler(propertyValue.getClass());
        if (parameterHandler != null) return parameterHandler.apply(propertyValue, context);

        LOG.warn("{} {} missing the parameterHandler.", beanClazz, propertyName);
        return context.nullParameterHandler().apply(propertyValue, context);
    }

    public boolean hasSetter(BEAN target, String propertyName) {
        if (target == null) return false;
        if (propertyWriterMap.containsKey(propertyName)) return true;
        return propertyCustomizer.hasSetter(target, propertyName, context);
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
        if (propertyWriterMap.containsKey(propertyName)) {
            final BeanPropertyWriter beanPropertyWriter = propertyWriterMap.get(propertyName);
            beanPropertyWriter.setter(target, propertyValue);
            return;
        }

        if (propertyCustomizer != null) propertyCustomizer.setter(target, propertyName, propertyValue, context);
    }

    void putDefinedPropertyKey(String propertyKey) {
        excludeNonDefinedPropertyKeySet.add(propertyKey);
    }

    void putBeanPropertyAccessor(String propertyName, Class<?> propertyType, Method getterMethod) {
        if (getterMethod == null) return;
        if (propertyAccessorMap.containsKey(propertyName)) throw new MapleDslBindingException("Duplicate getter property " + propertyName + " in " + getterMethod);

        BeanPropertyAccessor beanPropertyAccessor = new BeanPropertyAccessor(context, lookup, propertyType, getterMethod);
        switch (propertyName) {
            case Model.ID:
            case Model.E.SRC:
            case Model.E.DST:
                beanPropertyAccessor.override(context.keyPolicyStrategy());
        }

        propertyGetterImplMethodNameMap.put(getterMethod.getName(), propertyName);
        propertyAccessorMap.put(propertyName, beanPropertyAccessor);
    }

    void putBeanPropertyWriter(String propertyName, Class<?> propertyType, Method setterMethod) {
        if (setterMethod == null) return;
        if (propertyWriterMap.containsKey(propertyName)) throw new MapleDslBindingException("Duplicate setter property " + propertyName + " in " + setterMethod);
        propertyWriterMap.put(propertyName, new BeanPropertyWriter(context, propertyType, lookup, setterMethod));
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
