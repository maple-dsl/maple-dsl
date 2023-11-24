package com.mapledsl.core;

import com.mapledsl.core.condition.wrapper.MapleDslDialectFunction;
import com.mapledsl.core.condition.wrapper.MapleDslDialectPredicate;
import com.mapledsl.core.condition.wrapper.MapleDslDialectSelection;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.exception.MapleDslExecutionException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.compiler.CompiledST;

import java.util.Objects;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;

/**
 * @author bofa1ex
 * @since 2023/08/21
 */
final class MapleDslTemplateRegistry {
    static final int DEFAULT_TEMPLATE_GROUP_POOL_MAX_TOTAL = Runtime.getRuntime().availableProcessors() * 5;
    static final int DEFAULT_TEMPLATE_GROUP_POOL_MAX_IDLE = Runtime.getRuntime().availableProcessors() * 2;
    static final int DEFAULT_TEMPLATE_GROUP_POOL_MIN_IDLE = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

    final MapleDslModelAdaptor modelAdaptor;
    final MapleDslClazzRender clazzRender;
    final MapleDslDialectSelectionRender selectionRender;
    final MapleDslDialectFunctionRender functionRender;
    final MapleDslDialectPredicateRender predicateRender;
    final MapleDslConfiguration context;
    final GenericObjectPool<STGroup> templateGroup;
    final Properties templateProperties;

    MapleDslTemplateRegistry(MapleDslConfiguration context, Integer maxTotal, Integer maxIdle, Integer minIdle) {
        if (minIdle == null) minIdle = DEFAULT_TEMPLATE_GROUP_POOL_MIN_IDLE;
        if (minIdle < DEFAULT_TEMPLATE_GROUP_POOL_MIN_IDLE) {
            throw new IllegalArgumentException("TemplatePoolConfig#minIdle must not be negative.");
        }

        if (maxIdle == null) maxIdle = DEFAULT_TEMPLATE_GROUP_POOL_MAX_IDLE;
        if (maxIdle < DEFAULT_TEMPLATE_GROUP_POOL_MAX_IDLE) {
            throw new IllegalArgumentException("TemplatePoolConfig#maxIdle must be greater than `Core * 2`, it will occur thread-safe situation.");
        }

        if (maxIdle < minIdle) {
            throw new IllegalArgumentException("TemplatePoolConfig#maxIdle must be greater than `minIdle`.");
        }

        if (maxTotal == null) maxTotal = DEFAULT_TEMPLATE_GROUP_POOL_MAX_TOTAL;
        if (maxTotal < maxIdle) {
            throw new IllegalArgumentException("TemplatePoolConfig#maxTotal must be greater than `maxIdle`.");
        }

        final STGroupPooledObjectFactory templateGroupPooledObjectFactory = new STGroupPooledObjectFactory();
        final GenericObjectPoolConfig<STGroup> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);

        this.context = context;
        this.templateProperties = context.module().dialectProperties();
        this.templateGroup = new GenericObjectPool<>(templateGroupPooledObjectFactory, genericObjectPoolConfig);

        this.modelAdaptor = new MapleDslModelAdaptor(context);
        this.clazzRender = new MapleDslClazzRender().bind(context);
        this.selectionRender = spiStream(MapleDslDialectSelectionRender.class)
                .filter(Objects::nonNull)
                .filter(it -> context.module.dialectPredicate().test(it.dialect()))
                .map(it -> it.bind(context))
                .findFirst()
                .orElseThrow(() -> new MapleDslBindingException("selection initialize not found."));
        this.functionRender = spiStream(MapleDslDialectFunctionRender.class)
                .filter(Objects::nonNull)
                .filter(it -> context.module.dialectPredicate().test(it.dialect()))
                .map(it -> it.bind(context))
                .findFirst()
                .orElseThrow(() -> new MapleDslBindingException("function initialize not found."));
        this.predicateRender = spiStream(MapleDslDialectPredicateRender.class)
                .filter(Objects::nonNull)
                .filter(it -> context.module.dialectPredicate().test(it.dialect()))
                .map(it -> it.bind(context))
                .findFirst()
                .orElseThrow(() -> new MapleDslBindingException("predicate renderer not found."));

        try {
            templateGroup.preparePool();
        } catch (Exception e) {
            throw new MapleDslExecutionException("templateGroup warmup error", e);
        }
    }

    class STGroupPooledObjectFactory extends BasePooledObjectFactory<STGroup> {
        @Override
        public STGroup create() {
            final STGroup templateGroup = new STGroup();
            templateGroup.registerModelAdaptor(Object.class, modelAdaptor);
            templateGroup.registerRenderer(Class.class, clazzRender);
            templateGroup.registerRenderer(MapleDslDialectSelection.class, selectionRender);
            templateGroup.registerRenderer(MapleDslDialectFunction.class, functionRender);
            templateGroup.registerRenderer(MapleDslDialectPredicate.class, predicateRender);

            for (String templateName : templateProperties.stringPropertyNames()) {
                final String template = templateProperties.getProperty(templateName);
                final CompiledST compiledST = templateGroup.compile(null, null, null, template, null);
                templateName = inspect(templateName);
                compiledST.hasFormalArgs = false;
                compiledST.name = templateName;
                compiledST.defineImplicitlyDefinedTemplates(templateGroup);
                templateGroup.rawDefineTemplate(templateName, compiledST, null);
            }

            return templateGroup;
        }

        @Override
        public PooledObject<STGroup> wrap(STGroup stGroup) {
            return new DefaultPooledObject<>(stGroup);
        }

        @Override
        public void destroyObject(PooledObject<STGroup> p) {
            STGroup stGroup = p.getObject();
            if (stGroup != null) stGroup.unload();
        }
    }

    @NotNull ST borrowTemplate(String templateName) {
        try {
            final String inspectedTemplateName = inspect(templateName);
            final STGroup stGroup = templateGroup.borrowObject();
            final ST fmt = stGroup.getInstanceOf(inspectedTemplateName);
            if (fmt == null) throw new MapleDslBindingException("dialect template fetch error.");
            return fmt;
        } catch (MapleDslException e) {
            throw e;
        } catch (Exception cause) {
            throw new MapleDslBindingException(format("Fetch template:%s failed", templateName), cause);
        }
    }

    void returnTemplate(@Nullable ST st) {
        if (st == null) return;
        if (st.groupThatCreatedThisInstance == null) return;
        try {
            templateGroup.returnObject(st.groupThatCreatedThisInstance);
        } catch (Exception cause) {
            throw new MapleDslBindingException("DialectRender return st failed", cause);
        }
    }

    private String inspect(String templateName) {
        return templateName.charAt(0) != '/' ? '/' + templateName : templateName;
    }

    private <T> Stream<T> spiStream(Class<T> spiClazz) {
        return StreamSupport.stream(ServiceLoader.load(spiClazz).spliterator(), false);
    }
}
