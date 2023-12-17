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
 * The MapleDslTemplateRegistry class is responsible for managing the template registry for the Maple DSL.
 * <p></p>
 * It provides methods for borrowing and returning template objects, as well as initializing the template pool.
 * <p></p>
 * The template registry is implemented as a GenericObjectPool, which provides pooling functionality for the STGroup template objects.
 */
final class MapleDslTemplateRegistry {
    static final int DEFAULT_TEMPLATE_GROUP_POOL_MAX_TOTAL = Runtime.getRuntime().availableProcessors() * 5;
    static final int DEFAULT_TEMPLATE_GROUP_POOL_MAX_IDLE = Runtime.getRuntime().availableProcessors() * 2;
    static final int DEFAULT_TEMPLATE_GROUP_POOL_MIN_IDLE = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

    /**
     * The MapleDslModelAdaptor class is responsible for adapting model objects to the Maple DSL.
     * <p></p>
     * It implements the ModelAdaptor interface, which allows it to provide the necessary functionality
     * to interact with the model objects.
     * <p></p>
     * It also implements the MapleDslDialectRenderHelper interface, which provides additional helper methods
     * for rendering DSL expressions in the Maple DSL dialect.
     */
    final MapleDslModelAdaptor modelAdaptor;
    /**
     * The MapleDslClazzRender class is an implementation of the AttributeRenderer interface for rendering Class objects.
     * It is also aware of the MapleDslDialect context and provides helper methods for rendering DSL expressions.
     */
    final MapleDslClazzRender clazzRender;
    /**
     * This variable is of type MapleDslDialectSelectionRender. It is an abstract base class for rendering a MapleDslDialectSelection into a string representation.
     * It provides methods for rendering various types of elements like vertices, edges, in-vertices, and out-vertices.
     * Implementations of this class should provide the specific rendering logic for the target dialect.
     */
    final MapleDslDialectSelectionRender selectionRender;
    /**
     * This abstract class provides functionality for rendering Maple DSL functions in different dialects.
     */
    final MapleDslDialectFunctionRender functionRender;
    /**
     * This variable represents an instance of the MapleDslDialectPredicateRender class.
     * It is an abstract class that provides methods for rendering various parts of a predicate query.
     * Subclasses must implement the necessary methods to provide custom rendering logic.
     * The variable is intended to be used for rendering predicate queries in a specific Maple DSL dialect.
     */
    final MapleDslDialectPredicateRender predicateRender;
    /**
     * The templateGroup variable represents a generic object pool that holds instances of the STGroup class.
     */
    final GenericObjectPool<STGroup> templateGroup;
    /**
     * The templateProperties variable holds the properties that are used for configuring a template.
     * This variable is of type Properties, which is a subclass of Hashtable<Object,Object>.
     * The properties are key-value pairs, where the key is a String and the value is an Object.
     * This variable is declared as final, meaning that it cannot be reassigned once initialized.
     */
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

        this.templateProperties = context.module().dialectProperties(context);
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

    /**
     * Retrieves a template from the template group with the specified name.
     *
     * @param templateName the name of the template to retrieve
     * @return the retrieved template as an instance of {@link ST}
     * @throws MapleDslException if an error occurs during template retrieval
     * @throws MapleDslBindingException if the dialect template fetch error occurs
     */
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

    /**
     * Returns a template to the template group.
     *
     * @param st the template to return
     */
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
