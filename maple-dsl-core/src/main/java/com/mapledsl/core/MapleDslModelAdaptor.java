package com.mapledsl.core;

import com.mapledsl.core.extension.introspect.BeanDefinition;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * The MapleDslModelAdaptor class is responsible for adapting model objects to the Maple DSL.
 * <p></p>
 * It implements the ModelAdaptor interface, which allows it to provide the necessary functionality
 * to interact with the model objects.
 * <p></p>
 * It also implements the MapleDslDialectRenderHelper interface, which provides additional helper methods
 * for rendering DSL expressions in the Maple DSL dialect.
 */
@SuppressWarnings("unchecked")
class MapleDslModelAdaptor implements ModelAdaptor<Object>, MapleDslDialectRenderHelper {
    private final MapleDslConfiguration context;

    MapleDslModelAdaptor(MapleDslConfiguration context) {
        this.context = context;
    }

    @Override
    public Object getProperty(Interpreter interp, ST self, Object value, Object property, String propertyName) throws STNoSuchPropertyException {
        if (value == null) return NULL;
        if (Collection.class.isAssignableFrom(value.getClass())) {
            final Collection<?> valueCollection = (Collection<?>) value;
            if (valueCollection.isEmpty()) return NULL;
            value = valueCollection.toArray();
        }

        if (value.getClass().isArray()) {
            final int length = Array.getLength(value);
            if (length == 0) return NULL;
            value = Array.get(value, 0);
        }

        final BeanDefinition<Object> definition = (BeanDefinition<Object>) context.beanDefinitionUnchecked(value.getClass());
        final String propertyValue = definition.getter(value, propertyName);
        return propertyValue == null ? NULL : propertyValue;
    }
}