package com.mapledsl.core.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
public interface MapleDslSession extends Closeable {
    /**
     * Retrieve a single row mapped from the statement key and parameter.
     *
     * @param <T>  the returned model(vertex/edge) type.
     * @param stmt complete sql statement
     * @return optional of mapped object
     */
    <T> T selectOne(String stmt, Class<T> mappedEntityType);

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.
     *
     * @param <T>  the returned model(vertex/edge) type.
     * @param stmt complete sql statement
     * @return List of mapped object
     */
    <T> List<T> selectList(String stmt, Class<T> mappedEntityType);

    /**
     * The selectMap is a special case in that it is designed to convert a list
     * of results into a Map based on one of the properties in the resulting
     * objects.
     *
     * @param stmt complete sql statement
     * @return Map containing key pair data.
     */
    Map<String, Object> selectMap(String stmt);

    /**
     * The selectMaps is a special case in that it is designed to convert a list
     * of results into a Map based on one of the properties in the resulting
     * objects.
     *
     * @param stmt complete sql statement
     * @return Map containing key pair data.
     */
    List<Map<String, Object>> selectMaps(String stmt);

    /**
     * Execute a execution statement with the given parameters.
     *
     * @param stmt complete sql statement
     * @return boolean the execution of results affected by the execution.
     */
    boolean execute(String stmt) throws MapleDslException;

    /**
     * Closes the session.
     */
    @Override void close();

    /**
     * Retrieves configuration
     *
     * @return configuration
     */
    MapleDslConfiguration configuration();
}
