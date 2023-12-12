package com.mapledsl.core;

import com.mapledsl.core.model.Model;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

/**
 * The MapleDslClazzRender class is an implementation of the AttributeRenderer interface for rendering Class objects.
 * It is also aware of the MapleDslDialect context and provides helper methods for rendering DSL expressions.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MapleDslClazzRender implements AttributeRenderer<Class>, MapleDslDialectContextAware, MapleDslDialectRenderHelper {
    private MapleDslConfiguration context;

    @Override
    public String toString(Class value, String formatString, Locale locale) {
        return Model.class.isAssignableFrom(value) ? context.label(((Class<Model<?>>) value)) : NULL;
    }

    @Override
    public MapleDslClazzRender bind(MapleDslConfiguration context) {
        this.context = context;
        return this;
    }
}
