package com.mapledsl.core.session;

import com.mapledsl.core.MapleDslConfiguration;

/**
 * The MapleDslSessionFactory interface represents a factory for creating MapleDslSession instances.
 * It provides methods for opening a session and retrieving the configuration.
 */
public interface MapleDslSessionFactory {
    /**
     * Opens a session for executing Maple DSL queries.
     *
     * @return a MapleDslSession instance representing the opened session
     */
    MapleDslSession openSession();

    /**
     * Returns the configuration used by the MapleDslSessionFactory.
     *
     * @return the MapleDslConfiguration instance representing the configuration used
     */
    MapleDslConfiguration configuration();
}
