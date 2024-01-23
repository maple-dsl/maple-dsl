package com.mapledsl.core;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.exception.MapleDslUncheckedException;
import com.mapledsl.core.extension.KeyPolicyStrategy;
import com.mapledsl.core.extension.NamingStrategy;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.extension.introspect.BeanPropertyCustomizer;
import com.mapledsl.core.extension.introspect.ModelPropertyCustomizer;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.module.MapleDslDefinitionResultHandler;
import com.mapledsl.core.module.MapleDslModule;
import com.mapledsl.core.module.MapleDslParameterHandler;
import com.mapledsl.core.module.MapleDslResultHandler;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.StreamSupport;

/**
 * Represents the configuration for the Maple DSL.
 */
public final class MapleDslConfiguration {
    /**
     * Represents a Maple DSL module.
     */
    @NotNull final MapleDslModule module;
    /**
     * Registry for Maple DSL templates.
     */
    @NotNull final MapleDslTemplateRegistry templateRegistry;
    /**
     * The handler registry for the Maple DSL framework.
     */
    @NotNull final MapleDslHandlerRegistry handlerRegistry;
    /**
     * The MapleDslDefinitionRegistry class represents a registry for Maple DSL bean definitions and customizers.
     */
    @NotNull final MapleDslDefinitionRegistry mapperRegistry;
    /**
     * Represents the region configuration for the Maple DSL.
     *
     * <p>The region configuration includes the following properties:</p>
     * <ul>
     *     <li>{@code zoneId}: The {@code ZoneId} for the region. Default value is {@code ZoneId.systemDefault()}.</li>
     *     <li>{@code dateTimeFormatter}: The {@code DateTimeFormatter} for date and time values in the region. Default value is {@code DateTimeFormatter.ISO_LOCAL_DATE_TIME}.</li
     *>
     *     <li>{@code dateFormatter}: The {@code DateTimeFormatter} for date values in the region. Default value is {@code DateTimeFormatter.ISO_LOCAL_DATE}.</li>
     *     <li>{@code timeFormatter}: The {@code DateTimeFormatter} for time values in the region. Default value is {@code DateTimeFormatter.ISO_LOCAL_TIME}.</li>
     *     <li>{@code locale}: The {@code Locale} for the region. Default value is {@code Locale.getDefault()}.</li>
     *     <li>{@code timeZone}: The {@code TimeZone} for the region. Default value is the system default time zone associated with the {@code ZoneId}.</li>
     * </ul>
     */
    @NotNull final RegionConfig regionConfig;
    /**
     * The {@code NamingStrategy} interface represents a strategy for translating input strings.
     * It provides a method to translate an input string based on a given locale.
     */
    @NotNull final NamingStrategy namingStrategy;
    /**
     * The interface KeyPolicyStrategy represents a strategy for generating keys.
     */
    @NotNull final KeyPolicyStrategy keyPolicyStrategy;

    /**
     * Represents the configuration for the Maple DSL.
     *
     * @param module The Maple DSL module.
     * @param regionConfig The region configuration.
     * @param namingStrategy The naming strategy.
     * @param keyPolicyStrategy The key policy strategy.
     * @param templatePoolConfigMaxTotal The maximum total number of templates in the template pool configuration. Can be null.
     * @param templatePoolConfigMaxIdle The maximum number of idle templates in the template pool configuration. Can be null.
     * @param templatePoolConfigMinIdle The minimum number of idle templates in the template pool configuration. Can be null.
     */
    MapleDslConfiguration(@NotNull MapleDslModule module, @NotNull RegionConfig regionConfig,
                          @NotNull NamingStrategy namingStrategy, @NotNull KeyPolicyStrategy keyPolicyStrategy,
                          @Nullable Integer templatePoolConfigMaxTotal, @Nullable Integer templatePoolConfigMaxIdle, @Nullable Integer templatePoolConfigMinIdle) {
        this.module = module;
        this.regionConfig = regionConfig;
        this.namingStrategy = namingStrategy;
        this.keyPolicyStrategy = keyPolicyStrategy;

        this.mapperRegistry = new MapleDslDefinitionRegistry(this);
        this.handlerRegistry = new MapleDslHandlerRegistry(this);
        this.templateRegistry = new MapleDslTemplateRegistry(this, templatePoolConfigMaxTotal, templatePoolConfigMaxIdle, templatePoolConfigMinIdle);
    }

    /**
     * Registers a customizer for bean properties in the Maple DSL configuration.
     *
     * @param customizer The customizer for bean properties.
     * @return The updated Maple DSL configuration.
     */
    @Contract("_ -> this")
    public MapleDslConfiguration registerBeanPropertyCustomizer(BeanPropertyCustomizer<?> customizer) {
        mapperRegistry.registerBeanPropertyCustomizer(customizer);
        return this;
    }

    /**
     * Registers a {@link ModelPropertyCustomizer} for bean properties in the Maple DSL configuration.
     *
     * @param customizer The {@link ModelPropertyCustomizer} for bean properties.
     * @return The updated {@link MapleDslConfiguration} instance.
     */
    @Contract("_ -> this")
    public MapleDslConfiguration registerModelPropertyCustomizer(ModelPropertyCustomizer customizer) {
        mapperRegistry.registerModelPropertyCustomizer(customizer);
        return this;
    }

    /**
     * Registers a parameter handler in the Maple DSL configuration.
     *
     * @param parameterHandler The parameter handler to be registered.
     * @return The updated Maple DSL configuration.
     */
    @Contract("_ -> this")
    public MapleDslConfiguration registerParameterHandler(MapleDslParameterHandler<?> parameterHandler) {
        handlerRegistry.registerParameterHandler(parameterHandler);
        return this;
    }

    /**
     * Registers a result handler in the Maple DSL configuration.
     *
     * @param resultHandler The result handler to be registered.
     * @return The updated Maple DSL configuration.
     */
    @Contract("_ -> this")
    public MapleDslConfiguration registerResultHandler(MapleDslResultHandler<?,?> resultHandler) {
        handlerRegistry.registerResultHandler(resultHandler);
        return this;
    }

    /**
     * Retrieves the label for the specified model class.
     *
     * @param <M> the model class type
     * @param modelClazz The model class.
     * @return The label.
     * @throws IllegalArgumentException if the modelClazz is null.
     */
    @SuppressWarnings("DataFlowIssue")
    public @NotNull <M extends Model<?>> String label(Class<M> modelClazz) {
        return beanDefinitionUnchecked(modelClazz).label();
    }

    /**
     * Retrieves the bean definition for the specified bean class.
     *
     * @param beanClazz The class of the bean.
     * @param <BEAN> The type of the bean.
     * @return The bean definition for the specified bean class, or null if not found.
     * @throws MapleDslException if an exception occurs during the retrieval of the bean definition.
     */
    public @Nullable <BEAN> BeanDefinition<BEAN> beanDefinition(Class<BEAN> beanClazz) {
        try {
            return mapperRegistry.getBeanDefinition(beanClazz);
        } catch (MapleDslUncheckedException e) {
            return null;
        } catch (Exception e) {
            throw new MapleDslException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public @NotNull <ID> BeanDefinition<Model.V<ID>> vertexDefinition() {
        try {
            final BeanDefinition<?> vertexDefinition = mapperRegistry.getBeanDefinition(Model.V.class);
            return (BeanDefinition<Model.V<ID>>) vertexDefinition;
        } catch (MapleDslUncheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new MapleDslException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public @NotNull <ID, R> BeanDefinition<Model.E<ID, R>> edgeDefinition() {
        try {
            final BeanDefinition<?> edgeDefinition = mapperRegistry.getBeanDefinition(Model.E.class);
            return (BeanDefinition<Model.E<ID, R>>) edgeDefinition;
        } catch (MapleDslUncheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new MapleDslException(e);
        }
    }

    /**
     * Retrieves the bean definition for the specified bean class.
     *
     * @param beanClazz The class of the bean.
     * @param <BEAN> The type of the bean.
     * @return The bean definition for the specified bean class.
     * @throws MapleDslUncheckedException if an exception occurs during the retrieval of the bean definition.
     */
    public @NotNull <BEAN> BeanDefinition<BEAN> beanDefinitionUnchecked(Class<BEAN> beanClazz) throws MapleDslUncheckedException {
        return mapperRegistry.getBeanDefinition(beanClazz);
    }

    /**
     * Retrieves the bean property customizer for the specified bean class.
     *
     * @param clazz The class of the bean.
     * @param <BEAN> The type of the bean.
     * @return The bean property customizer for the specified bean class.
     */
    public @Nullable <BEAN> BeanPropertyCustomizer<BEAN> beanPropertyCustomizer(Class<BEAN> clazz) {
        return mapperRegistry.getBeanPropertyCustomizer(clazz);
    }

    /**
     * Retrieves the {@link ModelPropertyCustomizer} for bean properties in the Maple DSL configuration.
     *
     * @return The {@link ModelPropertyCustomizer} instance.
     */
    public @NotNull ModelPropertyCustomizer modelPropertyCustomizer() {
        return mapperRegistry.getModelPropertyCustomizer();
    }

    /**
     * Process the given parameter and convert it to a string value based on its type.
     *
     * @param <PARAM> The type of the parameter.
     * @param param   The parameter value.
     * @return The string value of the parameter.
     */
    @SuppressWarnings("unchecked")
    public @NotNull <PARAM> String parameterized(@Nullable PARAM param) {
        if (param == null) return handlerRegistry.nullParameterHandler.apply(param, this);
        if (param instanceof List) {
            final List<?> listParam = (List<?>) param;
            return ((MapleDslParameterHandler<List<?>>) handlerRegistry.parameterHandlerMap.get(List.class)).apply(listParam, this);
        }
        if (param instanceof Set) {
            final Set<?> setParam = (Set<?>) param;
            return ((MapleDslParameterHandler<Set<?>>) handlerRegistry.parameterHandlerMap.get(Set.class)).apply(setParam, this);
        }
        if (param instanceof Map) {
            final Map<?,?> mapParam = (Map<?,?>) param;
            return ((MapleDslParameterHandler<Map<?,?>>) handlerRegistry.parameterHandlerMap.get(Map.class)).apply(mapParam, this);
        }

        if (!handlerRegistry.parameterHandlerMap.containsKey(param.getClass())) return handlerRegistry.nullParameterHandler.apply(param, this);
        final MapleDslParameterHandler<PARAM> parameterHandler = (MapleDslParameterHandler<PARAM>) handlerRegistry.parameterHandlerMap.get(param.getClass());
        return parameterHandler.apply(param, this);
    }

    /**
     * Retrieves the result of applying the given inbound value to the specified bean definition.
     *
     * @param <IN>        The type of the inbound value.
     * @param <OUT>       The type of the outbound value.
     * @param inbound     The inbound value. Can be null.
     * @param definition  The bean definition.
     * @return The result of applying the inbound value to the bean definition, or null if the inbound value is null or if no result handler is found for the inbound value.
     */
    @Contract("null, _ -> null")
    @SuppressWarnings("unchecked")
    public @Nullable <IN, OUT> OUT resultant(@Nullable IN inbound, @NotNull BeanDefinition<OUT> definition) {
        if (inbound == null) return null;
        @Nullable MapleDslDefinitionResultHandler<IN> definitionResultHandler = (MapleDslDefinitionResultHandler<IN>) handlerRegistry.definitionResultHandlerMap.get(inbound.getClass());
        if (definitionResultHandler == null) {
            LOG.debug("IN:{} does not found it definition result handler, inspect it superclass or superinterface in deeply.", inbound);
            for (Class<?> definitionResultType : handlerRegistry.definitionResultHandlerMap.keySet()) {
                if (definitionResultType.isAssignableFrom(inbound.getClass())) {
                    definitionResultHandler = (MapleDslDefinitionResultHandler<IN>) handlerRegistry.definitionResultHandlerMap.get(definitionResultType);
                    break;
                }
            }
            if (definitionResultHandler == null) {
                LOG.warn("IN:{} does not found it definition result handler after deep inspection. ", inbound);
                return null;
            }
        }

        return definitionResultHandler.apply(inbound, definition, this);
    }

    @Contract("null -> null")
    public @Nullable <IN> Object resultant(@Nullable IN inbound) {
        return resultant(inbound, Object.class);
    }

    /**
     * Retrieves the result of applying the given inbound value to the specified outbound class.
     *
     * @param inbound The inbound value. Can be null.
     * @param outboundClazz The class of the outbound value.
     * @param <IN> The type of the inbound value.
     * @param <OUT> The type of the outbound value.
     * @return The result of applying the inbound value to the outbound class, or null if the inbound value is null or if no result handler is found for the inbound value.
     */
    @Contract("null, _ -> null")
    @SuppressWarnings("unchecked")
    public @Nullable <IN, OUT> OUT resultant(@Nullable IN inbound, @NotNull Class<OUT> outboundClazz) {
        if (inbound == null) return null;

        final Class<?> resultInboundType = inbound.getClass();
        if (resultInboundType.equals(outboundClazz)) {
            LOG.debug("IN:{} sink as OUT direct.", inbound);
            return (OUT) inbound;
        }

        final BeanDefinition<OUT> outBeanDefinition = beanDefinition(outboundClazz);
        if (outBeanDefinition != null) {
            LOG.debug("OUT:{} resultant via it definition.", outboundClazz);
            return resultant(inbound, outBeanDefinition);
        }

        MapleDslResultHandler<IN, OUT> resultHandler =  (MapleDslResultHandler<IN, OUT>) handlerRegistry.resultHandlerTable.get(resultInboundType, outboundClazz);
        if (resultHandler == null) {
            LOG.debug("IN:{},OUT:{} does not found it result handler, scanning through companion_result_handlers.", inbound, outboundClazz);
            for (Class<?> companionResultInboundType : handlerRegistry.companionResultHandleTable.rowKeySet()) {
                if (companionResultInboundType.isAssignableFrom(resultInboundType)) {
                    resultHandler = (MapleDslResultHandler<IN, OUT>) handlerRegistry.companionResultHandleTable.get(companionResultInboundType, outboundClazz);
                    break;
                }
            }
        }

        if (resultHandler == null) {
            LOG.warn("IN:{},OUT:{} does not found it companion result handler.", inbound, outboundClazz);
            if (outboundClazz == Object.class) {
                LOG.warn("IN:{} sink as OUT direct.", resultInboundType);
                return ((OUT) inbound);
            }

            return null;
        }

        return resultHandler.apply(inbound, this);
    }

    public KeyPolicyStrategy keyPolicyStrategy() {
        return keyPolicyStrategy;
    }

    public NamingStrategy namingStrategy() {
        return namingStrategy;
    }

    public MapleDslModule module() {
        return module;
    }

    public ZoneId globalZoneId() {
        return regionConfig.zoneId;
    }

    public DateTimeFormatter globalDateTimeFormatter() {
        return regionConfig.dateTimeFormatter;
    }

    public DateTimeFormatter globalDateFormatter() {
        return regionConfig.dateFormatter;
    }

    public DateTimeFormatter globalTimeFormatter() {
        return regionConfig.timeFormatter;
    }

    public Locale globalLocale() {
        return regionConfig.locale;
    }

    public TimeZone globalTimeZone() {
        return regionConfig.timeZone;
    }

    /**
     * Configuration for specifying the region settings.
     */
    public static class RegionConfig {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        Locale locale = Locale.getDefault();
        TimeZone timeZone = zoneId != null ? TimeZone.getTimeZone(zoneId) : TimeZone.getDefault();
    }

    public static class Builder {
        boolean primary;
        Integer templatePoolConfigMaxTotal, templatePoolConfigMaxIdle, templatePoolConfigMinIdle;
        Class<? extends MapleDslModule> moduleClazz;
        MapleDslModule module;
        NamingStrategy namingStrategy;
        KeyPolicyStrategy keyPolicyStrategy;
        RegionConfig regionConfig = new RegionConfig();

        public Builder zoneId(String zoneId) {
            if (zoneId == null || zoneId.trim().isEmpty()) return this;
            return zoneId(ZoneId.of(zoneId));
        }

        public Builder zoneId(ZoneId zoneId) {
            if (zoneId == null) return this;
            regionConfig.zoneId = zoneId;
            return this;
        }

        public Builder dateTimeFormatter(String pattern) {
            if (pattern == null || pattern.trim().isEmpty()) return this;
            return dateTimeFormatter(DateTimeFormatter.ofPattern(pattern));
        }

        public Builder dateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
            if (dateTimeFormatter == null) return this;
            regionConfig.dateTimeFormatter = dateTimeFormatter;
            return this;
        }

        public Builder dateFormatter(String pattern) {
            if (pattern == null || pattern.trim().isEmpty()) return this;
            return dateFormatter(DateTimeFormatter.ofPattern(pattern));
        }

        public Builder dateFormatter(DateTimeFormatter dateTimeFormatter) {
            if (dateTimeFormatter == null) return this;
            regionConfig.dateFormatter = dateTimeFormatter;
            return this;
        }

        public Builder timeFormatter(String pattern) {
            if (pattern == null || pattern.trim().isEmpty()) return this;
            return timeFormatter(DateTimeFormatter.ofPattern(pattern));
        }

        public Builder timeFormatter(DateTimeFormatter dateTimeFormatter) {
            if (dateTimeFormatter == null) return this;
            regionConfig.timeFormatter = dateTimeFormatter;
            return this;
        }

        public Builder locale(String language) {
            if (language == null || language.trim().isEmpty()) return this;
            return locale(Locale.forLanguageTag(language));
        }

        public Builder locale(Locale locale) {
            if (locale == null) return this;
            regionConfig.locale = locale;
            return this;
        }

        public Builder timeZone(String timeZone) {
            if (timeZone == null || timeZone.trim().isEmpty()) return this;
            return timeZone(TimeZone.getTimeZone(timeZone));
        }

        public Builder timeZone(TimeZone timeZone) {
            if (timeZone == null) return this;
            regionConfig.timeZone = timeZone;
            return this;
        }

        public Builder namingStrategy(NamingStrategy namingStrategy) {
            this.namingStrategy = namingStrategy;
            return this;
        }

        public Builder keyPolicyStrategy(KeyPolicyStrategy keyPolicyStrategy) {
            this.keyPolicyStrategy = keyPolicyStrategy;
            return this;
        }

        public Builder module(Class<? extends MapleDslModule> moduleClazz) {
            this.moduleClazz = moduleClazz;
            return this;
        }

        public Builder templatePoolConfig(Integer maxTotal) {
            this.templatePoolConfigMaxTotal = maxTotal;
            return this;
        }

        public Builder templatePoolConfig(Integer maxTotal, Integer maxIdle) {
            this.templatePoolConfigMaxTotal = maxTotal;
            this.templatePoolConfigMaxIdle = maxIdle;
            return this;
        }

        public Builder templatePoolConfig(Integer maxTotal, Integer maxIdle, Integer minIdle) {
            this.templatePoolConfigMaxTotal = maxTotal;
            this.templatePoolConfigMaxIdle = maxIdle;
            this.templatePoolConfigMinIdle = minIdle;
            return this;
        }

        public Builder asPrimary() {
            this.primary = true;
            return this;
        }

        /**
         * Builds a MapleDslConfiguration object based on the provided configuration parameters.
         *
         * @return The MapleDslConfiguration object.
         * @throws MapleDslBindingException If the module is not found.
         */
        public MapleDslConfiguration build() {
            this.module = StreamSupport.stream(ServiceLoader.load(MapleDslModule.class).spliterator(), false)
                    .filter(Objects::nonNull)
                    .filter(it -> Objects.isNull(moduleClazz) || it.getClass().equals(moduleClazz))
                    .findFirst()
                    .orElseThrow(() -> new MapleDslBindingException("Module not found."));

            if (namingStrategy == null) namingStrategy = module.namingStrategy();
            if (keyPolicyStrategy == null) keyPolicyStrategy = module.keyPolicyStrategy();

            final MapleDslConfiguration configuration = new MapleDslConfiguration(
                    module, regionConfig, namingStrategy, keyPolicyStrategy,
                    templatePoolConfigMaxTotal, templatePoolConfigMaxIdle, templatePoolConfigMinIdle
            );

            if (primary) {
                final MapleDslConfiguration previousConfiguration = PRIMARY_CONFIGURATION.getAndSet(configuration);
                if (previousConfiguration != null) LOG.warn("{} Previous configuration has been override.", previousConfiguration.module());
            } else {
                PRIMARY_CONFIGURATION.compareAndSet(null, configuration);
            }

            return configuration;
        }
    }

    /**
     * Returns the primary configuration for the Maple DSL.
     *
     * @return The primary MapleDslConfiguration instance.
     * @since Stable
     */
    @API(status = API.Status.STABLE)
    public static MapleDslConfiguration primaryConfiguration() {
        return primaryConfiguration(UnaryOperator.identity());
    }

    /**
     * Retrieves the primary configuration for the Maple DSL.
     *
     * @param configurationBuilderUnary The UnaryOperator to build the configuration.
     * @return The primary MapleDslConfiguration instance.
     */
    @API(status = API.Status.STABLE)
    public static MapleDslConfiguration primaryConfiguration(UnaryOperator<Builder> configurationBuilderUnary) {
        return PRIMARY_CONFIGURATION.updateAndGet(primaryConfiguration -> {
            if (primaryConfiguration == null) {
                primaryConfiguration = configurationBuilderUnary.apply(new Builder()).build();
            }
            return primaryConfiguration;
        });
    }

    /**
     * Disables access warnings.
     * <p>
     * This method uses Java reflection to access and modify internal fields and methods to disable access warnings.
     * It is recommended to use this method only if you understand the implications and necessity of disabling access warnings.
     *
     */
    static void disableAccessWarnings() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
            LOG.warn("Skipped disable access warning");
        }
    }

    static final Logger LOG = LoggerFactory.getLogger(MapleDslConfiguration.class);
    static final AtomicReference<MapleDslConfiguration> PRIMARY_CONFIGURATION = new AtomicReference<>();

    static {
        disableAccessWarnings();
    }
}
