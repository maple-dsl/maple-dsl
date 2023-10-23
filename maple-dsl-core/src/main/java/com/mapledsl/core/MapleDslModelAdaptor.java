package com.mapledsl.core;

import com.mapledsl.core.extension.introspect.BeanDefinition;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.util.Map;
import java.util.Set;

class MapleDslModelAdaptor implements ModelAdaptor<Object> {
    private final MapleDslConfiguration context;

    MapleDslModelAdaptor(MapleDslConfiguration context) {
        this.context = context;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
        if (o instanceof Map) {
            if ("KEYS".equalsIgnoreCase(propertyName))   return ((Map<?, ?>) o).keySet();
            if ("VALUES".equalsIgnoreCase(propertyName)) return ((Map<?, ?>) o).values();
            return ((Map<?,?>) o).get(property);
        }

        final BeanDefinition definition = context.beanDefinition(o.getClass());
        if (definition == null) return null;

        if ("KEYS".equalsIgnoreCase(propertyName)) {
            return definition.propertyKeys(o);
        }

        if ("VALUES".equalsIgnoreCase(propertyName)) {
            final Set<String> propertyKeys = definition.propertyKeys(o);
            final Object[] propertyValues = new Object[propertyKeys.size()];
            int index = 0; for (String propertyKey : propertyKeys) {
                propertyValues[index++] = definition.getter(o, propertyKey);
            }
            return propertyValues;
        }

        return definition.getter(o, propertyName);
    }
}
