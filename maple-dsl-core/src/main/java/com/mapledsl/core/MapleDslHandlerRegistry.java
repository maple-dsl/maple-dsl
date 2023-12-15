package com.mapledsl.core;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.mapledsl.core.module.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * The {@code MapleDslHandlerRegistry} class is responsible for handling the registration of parameter handlers and result handlers in the Maple DSL framework.
 */
final class MapleDslHandlerRegistry {
    /**
     * key: field type, value: parameter handler.
     */
    final Map<Class<?>, MapleDslParameterHandler<?>> parameterHandlerMap;
    /**
     * row: inbound field type, column: value: result handler.
     */
    final Table<Class<?>, Class<?>, MapleDslResultHandler<?,?>> resultHandlerTable;
    final Table<Class<?>, Class<?>, MapleDslResultHandler<?,?>> companionResultHandleTable;
    /**
     * key: inbound field type, value: definition result handler.
     */
    final Map<Class<?>, MapleDslDefinitionResultHandler<?>> definitionResultHandlerMap;
    /**
     * Processed for NULL parameter value
     */
    final MapleDslParameterHandler<Object> nullParameterHandler;

    /**
     * The {@code MapleDslHandlerRegistry} class is responsible for handling the registration of parameter handlers and result handlers in the Maple DSL framework.
     *
     * @param context The {@code MapleDslConfiguration} to be used for registration.
     */
    MapleDslHandlerRegistry(@NotNull MapleDslConfiguration context) {
        final Optional<MapleDslParameterHandlerCollector> parameterHandlerCollectorOpt = StreamSupport.stream(ServiceLoader.load(MapleDslParameterHandlerCollector.class).spliterator(), false)
                .filter(Objects::nonNull)
                .filter(it -> context.module.versionPredicate().test(it.version()))
                .findFirst();

        this.nullParameterHandler = parameterHandlerCollectorOpt
                .map(MapleDslParameterHandlerCollector::nullParameterHandler)
                .orElseGet(MapleDslParameterHandlerCollector::defaultNullParameterHandler);

        this.parameterHandlerMap = Collections.synchronizedMap(Stream.concat(parameterHandlerCollectorOpt
                .map(MapleDslParameterHandlerCollector::parameterHandlers)
                .map(Collection::stream)
                .orElseGet(Stream::empty), MapleDslParameterHandlerCollector.defaultParameterHandlers().stream())
                .collect(Collectors.toMap(MapleDslParameterHandler::parameterType, Function.identity())));

        final Optional<MapleDslResultHandlerCollector> resultHandlerCollectorOpt = StreamSupport.stream(ServiceLoader.load(MapleDslResultHandlerCollector.class).spliterator(), false)
                .filter(Objects::nonNull)
                .filter(it -> context.module.versionPredicate().test(it.version()))
                .findFirst();

        //noinspection UnstableApiUsage
        this.resultHandlerTable = Tables.synchronizedTable(Stream.concat(resultHandlerCollectorOpt
                .map(MapleDslResultHandlerCollector::resultHandlers)
                .map(Collection::stream)
                .orElseGet(Stream::empty), MapleDslResultHandlerCollector.defaultResultHandlers().stream())
                .collect(Tables.toTable(MapleDslResultHandler::inboundType, MapleDslResultHandler::outboundType, Function.identity(), HashBasedTable::create)));

        //noinspection UnstableApiUsage
        this.companionResultHandleTable = Tables.synchronizedTable(resultHandlerCollectorOpt
                        .map(MapleDslResultHandlerCollector::companionResultHandlers)
                        .map(Collection::stream)
                        .orElseGet(Stream::empty)
                .collect(Tables.toTable(MapleDslResultHandler::inboundType, MapleDslResultHandler::outboundType, Function.identity(), HashBasedTable::create)));

        this.definitionResultHandlerMap = Collections.synchronizedMap(Stream.concat(resultHandlerCollectorOpt
                .map(MapleDslResultHandlerCollector::definitionResultHandlers)
                .map(Collection::stream)
                .orElseGet(Stream::empty), MapleDslResultHandlerCollector.defaultDefinitionResultHandlers().stream())
                .collect(Collectors.toMap(MapleDslDefinitionResultHandler::inboundType, Function.identity())));
    }

    /**
     * Registers a parameter handler in the Maple DSL handler registry.
     *
     * @param parameterHandler The parameter handler to be registered.
     */
    void registerParameterHandler(MapleDslParameterHandler<?> parameterHandler) {
        requireNonNull(parameterHandler.parameterType(), "parameterType must not be null");
        requireNonNull(parameterHandler, "parameterHandler must not be null");
        parameterHandlerMap.put(parameterHandler.parameterType(), parameterHandler);
    }

    /**
     * Registers a result handler in the Maple DSL handler registry.
     *
     * @param resultHandler The result handler to be registered.
     * @throws NullPointerException if resultHandler.outboundType() or resultHandler are null.
     */
    void registerResultHandler(MapleDslResultHandler<?, ?> resultHandler) {
        requireNonNull(resultHandler.outboundType(), "resultType must not be null");
        requireNonNull(resultHandler, "resultHandler must not be null");
        resultHandlerTable.put(resultHandler.inboundType(), resultHandler.outboundType(), resultHandler);
    }
}