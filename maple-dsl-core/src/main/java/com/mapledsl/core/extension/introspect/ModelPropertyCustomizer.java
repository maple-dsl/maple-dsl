package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.model.Model;

/**
 * This interface represents a customizer for model properties.
 * This customizer provides methods for customizing the properties of a Model object.
 */
public interface ModelPropertyCustomizer extends BeanPropertyCustomizerWriter<Model<?>>, BeanPropertyCustomizerAccessor<Model<?>> {
}
