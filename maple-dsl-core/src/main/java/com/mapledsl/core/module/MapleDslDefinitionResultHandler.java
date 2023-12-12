package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.introspect.BeanDefinition;

public interface MapleDslDefinitionResultHandler<IN> {
    Class<IN> inboundType();
    <OUT> OUT apply(IN in, BeanDefinition<OUT> definition, MapleDslConfiguration context);
}
