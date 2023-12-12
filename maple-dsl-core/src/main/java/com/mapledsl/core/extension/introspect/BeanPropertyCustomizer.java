package com.mapledsl.core.extension.introspect;

import org.apiguardian.api.API;

/**
 * The BeanPropertyCustomizer interface allows customization of bean property handling.
 *
 * @param <T> the type of the bean being customized
 */
@API(status = API.Status.STABLE)
public interface BeanPropertyCustomizer<T> extends BeanPropertyCustomizerAccessor<T>, BeanPropertyCustomizerWriter<T> {
    Class<T> beanClazz();
}