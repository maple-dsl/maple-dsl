package com.mapledsl.spring.autoconfigure;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.extension.introspect.BeanPropertyCustomizer;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.module.MapleDslParameterHandler;
import com.mapledsl.core.module.MapleDslResultHandler;
import com.mapledsl.core.session.MapleDslSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MapleDslConfiguration.class, MapleDslSessionFactory.class})
@EnableConfigurationProperties({MapleDslProperties.class})
public class MapleDslAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MapleDslAutoConfiguration.class);
    private final MapleDslProperties properties;
    private final List<MapleDslConfigurationCustomizer> configurationCustomizers;
    private final BeanPropertyCustomizer<Model<?>> modelPropertyCustomizer;
    private final BeanPropertyCustomizer<?>[] beanPropertyCustomizers;
    private final MapleDslParameterHandler[] parameterHandlers;
    private final MapleDslResultHandler<?,?>[] mapleDslResultHandlers;

    public MapleDslAutoConfiguration(MapleDslProperties properties,
                                     ObjectProvider<List<MapleDslConfigurationCustomizer>> configurationCustomizersProvider,
                                     ObjectProvider<BeanPropertyCustomizer<Model<?>>> modelPropertyCustomizerProvider,
                                     ObjectProvider<BeanPropertyCustomizer<?>[]> beanPropertyCustomizersProvider,
                                     ObjectProvider<MapleDslParameterHandler[]> parameterHandlersProvider,
                                     ObjectProvider<MapleDslResultHandler<?,?>[]> resultHandlersProvider) {
        this.properties = properties;
        this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
        this.modelPropertyCustomizer = modelPropertyCustomizerProvider.getIfAvailable();
        this.beanPropertyCustomizers = beanPropertyCustomizersProvider.getIfAvailable();
        this.parameterHandlers = parameterHandlersProvider.getIfAvailable();
        this.mapleDslResultHandlers = resultHandlersProvider.getIfAvailable();
    }

    @Bean
    @ConditionalOnMissingBean
    public MapleDslConfiguration mapleDslConfiguration() {
        final MapleDslConfiguration.Builder configurationBuilder = new MapleDslConfiguration.Builder();
        if (properties.getIsPrimaryConfiguration()) configurationBuilder.asPrimary();
        if (properties.getLocale() != null) configurationBuilder.locale(properties.getLocale());
        if (properties.getDateFormat() != null) configurationBuilder.dateFormatter(properties.getDateFormat());
        if (properties.getTimeFormat() != null) configurationBuilder.timeFormatter(properties.getTimeFormat());
        if (properties.getDateTimeFormat() != null) configurationBuilder.dateTimeFormatter(properties.getDateTimeFormat());
        if (properties.getNamingStrategy() != null) configurationBuilder.namingStrategy(properties.getNamingStrategy());
        if (properties.getKeyPolicyStrategy() != null) configurationBuilder.keyPolicyStrategy(properties.getKeyPolicyStrategy());

        if (properties.getTemplateConfig() != null) {
            final MapleDslProperties.TemplateObjectPoolConfig templateConfig = properties.getTemplateConfig();
            configurationBuilder.templatePoolConfig(templateConfig.getMaxTotal(), templateConfig.getMaxIdle(), templateConfig.getMinIdle());
        }

        final MapleDslConfiguration configuration = configurationBuilder.build();

        if (parameterHandlers != null) {
            for (MapleDslParameterHandler parameterHandler : parameterHandlers) {
                configuration.registerParameterHandler(parameterHandler);
            }
        }

        if (mapleDslResultHandlers != null) {
            for (MapleDslResultHandler<?,?> resultHandler : mapleDslResultHandlers) {
                configuration.registerResultHandler(resultHandler);
            }
        }

        if (modelPropertyCustomizer != null) {
            configuration.registerModelPropertyCustomizer(modelPropertyCustomizer);
        }

        if (beanPropertyCustomizers != null) {
            for (BeanPropertyCustomizer<?> beanPropertyCustomizer : beanPropertyCustomizers) {
                configuration.registerBeanPropertyCustomizer(beanPropertyCustomizer);
            }
        }

        if (configurationCustomizers != null) {
            configurationCustomizers.forEach(it -> it.customize(configuration));
        }

        return configuration;
    }
}
