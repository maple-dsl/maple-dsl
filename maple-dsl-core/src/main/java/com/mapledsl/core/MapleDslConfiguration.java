package com.mapledsl.core;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.extension.KeyPolicyStrategy;
import com.mapledsl.core.extension.NamingStrategies;
import com.mapledsl.core.extension.NamingStrategy;
import com.mapledsl.core.extension.introspect.BeanDefinition;
import com.mapledsl.core.extension.introspect.BeanPropertyCustomizer;
import com.mapledsl.core.model.Model;
import com.mapledsl.core.spi.MapleDslParameterHandler;
import com.mapledsl.core.spi.MapleDslResultHandler;
import com.mapledsl.core.spi.MapleDslModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.StreamSupport;

public final class MapleDslConfiguration {
    static final Logger LOG = LoggerFactory.getLogger(MapleDslConfiguration.class);
    @NotNull final MapleDslModule module;
    @NotNull final MapleDslTemplateRegistry templateRegistry;
    @NotNull final MapleDslHandlerRegistry handlerRegistry;
    @NotNull final MapleDslDefinitionRegistry mapperRegistry;
    @NotNull final RegionConfig regionConfig;
    @NotNull final NamingStrategy namingStrategy;
    @NotNull final KeyPolicyStrategy keyPolicyStrategy;

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

    public <BEAM> MapleDslConfiguration registerBeanPropertyCustomizer(Class<BEAM> clazz, BeanPropertyCustomizer<BEAM> customizer) {
        mapperRegistry.registerBeanPropertyCustomizer(clazz, customizer);
        return this;
    }

    public MapleDslConfiguration registerModelPropertyCustomizer(BeanPropertyCustomizer<Model<?>> customizer) {
        mapperRegistry.registerModelPropertyCustomizer(customizer);
        return this;
    }

    public MapleDslConfiguration registerParameterHandler(Class<?> parameterType, MapleDslParameterHandler parameterHandler) {
        handlerRegistry.registerParameterHandler(parameterType, parameterHandler);
        return this;
    }

    public <IN, OUT> MapleDslConfiguration registerResultHandler(Class<OUT> resultType, MapleDslResultHandler<IN, OUT> resultHandler) {
        handlerRegistry.registerResultHandler(resultType, resultHandler);
        return this;
    }

    public <M extends Model<?>> String getLabel(Class<M> beanClazz) {
        if (beanClazz.isPrimitive() || beanClazz.isArray()) return null;
        if (Number.class.isAssignableFrom(beanClazz)) return null;
        if (CharSequence.class.isAssignableFrom(beanClazz)) return null;
        if (Boolean.class.isAssignableFrom(beanClazz)) return null;
        if (Collection.class.isAssignableFrom(beanClazz) || Map.class.isAssignableFrom(beanClazz)) return null;
        return mapperRegistry.getBeanDefinition(beanClazz).label();
    }

    public <BEAN> BeanDefinition<BEAN> getBeanDefinition(Class<BEAN> clazz) {
        return mapperRegistry.getBeanDefinition(clazz);
    }

    public <BEAN> BeanPropertyCustomizer<BEAN> getBeanPropertyCustomizer(Class<BEAN> clazz) {
        return mapperRegistry.getBeanPropertyCustomizer(clazz);
    }

    public BeanPropertyCustomizer<Model<?>> getModelPropertyCustomizer() {
        return mapperRegistry.getModelPropertyCustomizer();
    }

    public MapleDslParameterHandler getParameterHandler(Class<?> clazz) {
        return handlerRegistry.getParameterHandler(clazz);
    }

    public MapleDslResultHandler<?,?> getResultHandler(Class<?> clazz) {
        return handlerRegistry.getResultHandler(clazz);
    }

    public MapleDslParameterHandler getNullParameterHandler() {
        return handlerRegistry.getNullParameterHandler();
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

    protected static class RegionConfig {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        Locale locale = Locale.getDefault();
        TimeZone timeZone = zoneId != null ? TimeZone.getTimeZone(zoneId) : TimeZone.getDefault();
    }

    public static class Builder {
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

        public Builder templatePoolConfig(int maxTotal) {
            this.templatePoolConfigMaxTotal = maxTotal;
            return this;
        }

        public Builder templatePoolConfig(int maxTotal, int maxIdle) {
            this.templatePoolConfigMaxTotal = maxTotal;
            this.templatePoolConfigMaxIdle = maxIdle;
            return this;
        }

        public Builder templatePoolConfig(int maxTotal, int maxIdle, int minIdle) {
            this.templatePoolConfigMaxTotal = maxTotal;
            this.templatePoolConfigMaxIdle = maxIdle;
            this.templatePoolConfigMinIdle = minIdle;
            return this;
        }

        public MapleDslConfiguration build() {
            if (namingStrategy == null) namingStrategy = NamingStrategies.SNAKE_CASE;
            if (keyPolicyStrategy == null) keyPolicyStrategy = KeyPolicyStrategies.MANUAL;

            this.module = StreamSupport.stream(ServiceLoader.load(MapleDslModule.class).spliterator(), false)
                    .filter(Objects::nonNull)
                    .filter(it -> Objects.isNull(moduleClazz) || it.getClass().equals(moduleClazz))
                    .findFirst()
                    .orElseThrow(() -> new MapleDslBindingException("Module not found."));

            return new MapleDslConfiguration(
                    module, regionConfig, namingStrategy, keyPolicyStrategy,
                    templatePoolConfigMaxTotal, templatePoolConfigMaxIdle, templatePoolConfigMinIdle
            );
        }
    }

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

    static {
        disableAccessWarnings();
    }
}
