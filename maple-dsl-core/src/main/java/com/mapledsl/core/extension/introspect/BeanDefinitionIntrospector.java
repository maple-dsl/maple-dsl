package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.annotation.*;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * The BeanDefinitionIntrospector class is responsible for introspecting a bean class and
 * generating a BeanDefinition object that represents the properties and methods of the bean.
 * It uses reflection to gather information about the fields, methods, and annotations of the bean class.
 */
public class BeanDefinitionIntrospector {
    static final Logger LOG = LoggerFactory.getLogger(BeanDefinitionIntrospector.class);
    private final MapleDslConfiguration context;

    public BeanDefinitionIntrospector(MapleDslConfiguration context) {
        this.context = context;
    }

    /**
     * Resolves the BeanDefinition for the given bean class.
     *
     * @param <BEAN>     the type of the bean
     * @param beanClazz  the class of the bean
     * @return the resolved BeanDefinition for the bean class
     */
    public <BEAN> BeanDefinition<BEAN> resolve(Class<BEAN> beanClazz) {
        final Set<Field> beanFieldTypeSet = new LinkedHashSet<>();
        final Set<Method> candidateMethodSet = new LinkedHashSet<>();

        final String label = findLabel(beanClazz);
        final BeanDefinition<BEAN> beanDefinition = new BeanDefinition<>(context, beanClazz, label);

        for (Class<?> cur = beanClazz; !Object.class.equals(cur); cur = cur.getSuperclass()) {
            for (Field curField : cur.getDeclaredFields()) {
                if (Modifier.isStatic(curField.getModifiers())) continue;
                if (curField.isAnnotationPresent(PropertyIgnore.class)) continue;
                beanFieldTypeSet.add(curField);
            }
            for (Method curMethod : cur.getDeclaredMethods()) {
                if (Modifier.isStatic(curMethod.getModifiers())) continue;
                candidateMethodSet.add(curMethod);
            }
        }

        for (Class<?>[] items = beanClazz.getInterfaces(); items.length != 0;) {
            final List<Class<?>> candidates = new LinkedList<>();
            for (Class<?> cur : items) {
                for (Method curMethod : cur.getDeclaredMethods()) {
                    if (Modifier.isStatic(curMethod.getModifiers())) continue;
                    if (curMethod.isAnnotationPresent(PropertyGetter.class) || curMethod.isAnnotationPresent(PropertySetter.class)) candidateMethodSet.add(curMethod);
                }
                Collections.addAll(candidates, cur.getInterfaces());
            }
            items = candidates.toArray(new Class[0]);
        }

        for (Field candidateField : beanFieldTypeSet) {
            final String candidateFieldName = candidateField.getName();
            final String propertyName = findProperty(candidateField);

            if (propertyName.isEmpty()) throw new MapleDslBindingException("Field: " + candidateField + ", propertyName must not be empty.");
            if (isPropertyDefined(candidateField)) beanDefinition.addDefinedBeanPropertyName(propertyName);
            beanDefinition.putBeanPropertyType(propertyName, candidateField.getType());

            final Method getter = findGetter(candidateFieldName, propertyName, candidateMethodSet);
            final Method setter = findSetter(candidateFieldName, propertyName, candidateMethodSet);

            if (getter == null) continue;
            beanDefinition.putBeanPropertyAccessor(propertyName, getter);
            if (setter == null) continue;
            beanDefinition.putBeanPropertyWriter(propertyName, setter);
        }

        for (Method candidateMethod : candidateMethodSet) {
            final PropertyGetter propertyGetter = findPropertyGetter(candidateMethod);
            if (propertyGetter == null || propertyGetter.value().isEmpty()) continue;
            final String propertyName = propertyGetter.value();

            if (beanDefinition.containsBeanPropertyAccessorName(candidateMethod.getName())) continue;
            beanDefinition.putAccessorNameProperty(candidateMethod.getName(), propertyName);

            if (beanDefinition.containsBeanPropertyAccessor(propertyName)) continue;
            beanDefinition.putBeanPropertyAccessor(propertyName, candidateMethod);
        }

        for (Method candidateMethod : candidateMethodSet) {
            final PropertySetter propertySetter = findPropertySetter(candidateMethod);
            if (propertySetter == null || propertySetter.value().isEmpty()) continue;
            final String propertyName = propertySetter.value();

            if (beanDefinition.containsBeanPropertyWriter(propertyName)) continue;
            beanDefinition.putBeanPropertyWriter(propertyName, candidateMethod);
        }

        return beanDefinition;
    }

    protected @Nullable String findLabel(Class<?> clazz) {
        if (!Model.class.isAssignableFrom(clazz)) return null;
        if (Model.class == clazz || Model.V.class == clazz || Model.E.class == clazz || Model.Path.class == clazz) return null;
        if (clazz.isAnnotationPresent(Label.class)) return clazz.getDeclaredAnnotation(Label.class).value();
        return context.namingStrategy().translate(clazz.getSimpleName(), context.globalLocale());
    }

    protected @NotNull String findProperty(Field field) {
        if (field.isAnnotationPresent(Property.class)) return field.getDeclaredAnnotation(Property.class).value();
        return context.namingStrategy().translate(field.getName(), context.globalLocale());
    }

    protected boolean isPropertyDefined(Field field) {
        return !field.isAnnotationPresent(Property.class) || field.getDeclaredAnnotation(Property.class).defined();
    }

    protected boolean isAnnotatedPropertyGetter(Method method) {
        return method.isAnnotationPresent(PropertyGetter.class);
    }

    protected @Nullable PropertyGetter findPropertyGetter(Method method) {
        if (!method.isAnnotationPresent(PropertyGetter.class)) return null;
        return method.getDeclaredAnnotation(PropertyGetter.class);
    }

    protected @Nullable Method findGetter(String fieldName, String propertyName, Set<Method> candidateMethods) {
        for (Method candidateMethod : candidateMethods) {
            final PropertyGetter propertyGetter = findPropertyGetter(candidateMethod);
            if (propertyGetter != null && propertyGetter.value().equalsIgnoreCase(propertyName)) return candidateMethod;

            final String candidateMethodName = candidateMethod.getName();
            if (candidateMethodName.startsWith("get") && candidateMethodName.substring(3).equalsIgnoreCase(fieldName)) return candidateMethod;
        }

        if (LOG.isWarnEnabled()) LOG.warn("Field:{} missing the getter method", fieldName);
        return null;
    }

    protected @Nullable PropertySetter findPropertySetter(Method method) {
        if (!method.isAnnotationPresent(PropertySetter.class)) return null;
        return method.getDeclaredAnnotation(PropertySetter.class);
    }

    protected @Nullable Method findSetter(String fieldName, String propertyName, Set<Method> candidateMethods) {
        for (Method candidateMethod : candidateMethods) {
            final PropertySetter propertySetter = findPropertySetter(candidateMethod);
            if (propertySetter != null && propertySetter.value().equalsIgnoreCase(propertyName)) return candidateMethod;

            final String candidateMethodName = candidateMethod.getName();
            if (candidateMethodName.startsWith("set") && candidateMethodName.substring(3).equalsIgnoreCase(fieldName)) return candidateMethod;
        }

        if (LOG.isWarnEnabled()) LOG.warn("Field:{} missing the setter method", fieldName);
        return null;
    }
}
