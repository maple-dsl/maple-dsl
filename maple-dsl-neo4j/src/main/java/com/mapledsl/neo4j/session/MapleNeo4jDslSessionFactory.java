package com.mapledsl.neo4j.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.session.MapleDslSession;
import com.mapledsl.core.session.MapleDslSessionFactory;
import org.neo4j.driver.*;

/**
 * The MapleNeo4jDslSessionFactory class is an implementation of the MapleDslSessionFactory interface.
 * It provides methods for opening a session and retrieving the configuration for executing Maple DSL queries.
 */
public class MapleNeo4jDslSessionFactory implements MapleDslSessionFactory {
    private final MapleDslConfiguration configuration;
    private final Driver driver;
    private final SessionConfig sessionConfig;

    public MapleNeo4jDslSessionFactory(MapleDslConfiguration configuration, Driver driver, SessionConfig sessionConfig) {
        this.driver = driver;
        this.sessionConfig = sessionConfig;
        this.configuration = configuration;
    }

    public MapleNeo4jDslSessionFactory(MapleDslConfiguration configuration, String uri, AuthToken authToken, Config driverConfig, SessionConfig sessionConfig) {
        this(configuration, GraphDatabase.driver(uri, authToken, driverConfig), sessionConfig);
    }

    @Override
    public final MapleDslSession openSession() {
        try {
            final Session session = driver.session(sessionConfig);
            return new MapleNeo4jDslSession(session, configuration);
        } catch (Exception e) {
            throw new MapleDslExecutionException("Error opening session. Cause: " + e.getMessage(), e);
        }
    }

    @Override
    public MapleDslConfiguration configuration() {
        return configuration;
    }
}
