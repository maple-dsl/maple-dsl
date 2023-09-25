package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.annotation.*;
import com.mapledsl.core.extension.func.SerializableFunction;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanDefinitionIntrospector {
    static final Logger LOG = LoggerFactory.getLogger(BeanDefinitionIntrospector.class);
    private final MapleDslConfiguration context;

    public BeanDefinitionIntrospector(MapleDslConfiguration context) {
        this.context = context;
    }

    public <BEAN> BeanDefinition<BEAN> resolve(Class<BEAN> beanClazz) {
        final Map<Field, Class<?>> beanFieldTypeMap = new HashMap<>();
        final Set<Method> candidateMethods = new HashSet<>();

        final String label = findLabel(beanClazz);
        final BeanDefinition<BEAN> beanDefinition = new BeanDefinition<>(context, beanClazz, label);

        for (Class<?> cur = beanClazz; !Object.class.equals(cur); cur = cur.getSuperclass()) {
            for (Field curField : cur.getDeclaredFields()) {
                if (Modifier.isStatic(curField.getModifiers())) continue;
                if (curField.isAnnotationPresent(PropertyIgnore.class)) continue;
                beanFieldTypeMap.put(curField, findFieldType(curField));
            }
            for (Method curMethod : cur.getDeclaredMethods()) {
                if (Modifier.isStatic(curMethod.getModifiers())) continue;
                candidateMethods.add(curMethod);
            }
        }

        for (Map.Entry<Field, Class<?>> fieldTypeEntry : beanFieldTypeMap.entrySet()) {
            final Field candidateField = fieldTypeEntry.getKey();
            final Class<?> candidateFieldType = fieldTypeEntry.getValue();

            final String candidateFieldName = candidateField.getName();
            final String propertyName = findProperty(candidateField);
            final boolean isPropertyDefined = isPropertyDefined(candidateField);

            final Method getter = findGetter(candidateFieldName, propertyName, candidateMethods);
            final Method setter = findSetter(candidateFieldName, propertyName, candidateMethods);

            if (isPropertyDefined) beanDefinition.putBeanProperty(propertyName);
            if (getter != null) SerializableFunction.attr(beanClazz, getter.getName(), propertyName);

            beanDefinition.putBeanPropertyAccessor(propertyName, candidateFieldType, getter);
            beanDefinition.putBeanPropertyWriter(propertyName, candidateFieldType, setter);
        }

        return beanDefinition;
    }

    public @Nullable String findLabel(Class<?> clazz) {
        if (!Model.class.isAssignableFrom(clazz)) return null;
        if (clazz.isAnnotationPresent(Label.class)) return clazz.getDeclaredAnnotation(Label.class).value();

        return context.namingStrategy().translate(clazz.getSimpleName(), context.globalLocale());
    }

    public String findProperty(Field field) {
        if (field.isAnnotationPresent(Property.class)) {
            return field.getDeclaredAnnotation(Property.class).value();
        }
        return context.namingStrategy().translate(field.getName(), context.globalLocale());
    }

    protected boolean isPropertyDefined(Field field) {
        return !field.isAnnotationPresent(Property.class) || field.getDeclaredAnnotation(Property.class).defined();
    }

    protected Method findGetter(String fieldName, String propertyName, Set<Method> candidateMethods) {
        for (Method candidateMethod : candidateMethods) {
            PropertyGetter propertyGetter = candidateMethod.getDeclaredAnnotation(PropertyGetter.class);
            if (propertyGetter != null && propertyGetter.value().equalsIgnoreCase(propertyName)){
                return candidateMethod;
            }
            final String candidateMethodName = candidateMethod.getName();
            if (candidateMethodName.startsWith("get") && candidateMethodName.substring(3).equalsIgnoreCase(fieldName)){
                return candidateMethod;
            }
        }
        LOG.warn("Field:{} missing the getter method", fieldName);
        return null;
    }

    protected Method findSetter(String fieldName, String propertyName, Set<Method> candidateMethods) {
        for (Method candidateMethod : candidateMethods) {
            PropertySetter propertySetter = candidateMethod.getDeclaredAnnotation(PropertySetter.class);
            if (propertySetter != null && propertySetter.value().equalsIgnoreCase(propertyName)){
                return candidateMethod;
            }
            final String candidateMethodName = candidateMethod.getName();
            if (candidateMethodName.startsWith("set") && candidateMethodName.substring(3).equalsIgnoreCase(fieldName)){
                return candidateMethod;
            }
        }
        LOG.warn("Field:{} missing the setter method", fieldName);
        return null;
    }

    protected Class<?> findFieldType(Field field) {
        final Class<?> fieldType = field.getType();
        if (LOG.isDebugEnabled()) LOG.debug("Loading bean definition, Field:{}, Type:{}", field, fieldType);
        return fieldType;
    }
}
