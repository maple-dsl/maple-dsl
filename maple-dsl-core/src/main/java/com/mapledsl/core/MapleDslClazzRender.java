package com.mapledsl.core;

import com.mapledsl.core.model.Model;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MapleDslClazzRender implements AttributeRenderer<Class>, MapleDslDialectContextAware, MapleDslDialectRenderHelper {
    private MapleDslConfiguration context;
    @Override
    public String toString(Class value, String formatString, Locale locale) {
        if (Model.class.isAssignableFrom(value)) {
            String label = context.label(value.asSubclass(Model.class));
            return label == null ? NULL : label;
        }
        return NULL;
    }

    @Override
    public MapleDslClazzRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }
}
