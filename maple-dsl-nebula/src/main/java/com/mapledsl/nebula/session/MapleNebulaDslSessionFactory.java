package com.mapledsl.nebula.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.session.MapleDslSession;
import com.mapledsl.core.session.MapleDslSessionFactory;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.exception.AuthFailedException;
import com.vesoft.nebula.client.graph.exception.ClientServerIncompatibleException;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;

import java.util.List;

/**
 * The MapleNebulaDslSessionFactory class is an implementation of the MapleDslSessionFactory interface.
 * It provides methods for opening a session and retrieving the configuration for executing Maple DSL queries against Nebula Graph.
 */
public class MapleNebulaDslSessionFactory implements MapleDslSessionFactory {
    private final NebulaPool nebulaPool;
    private final String userName;
    private final String password;
    private final String space;
    private final boolean reconnect;

    private final MapleDslConfiguration configuration;

    /**
     * Creates an instance of MapleNebulaDslSessionFactory.
     *
     * @param configuration   The MapleDslConfiguration object used for Maple DSL execution.
     * @param hostAddress     The list of Nebula Graph server addresses and ports.
     * @param space           The name of the space in Nebula Graph to connect to.
     * @param userName        The username for authentication.
     * @param password        The password for authentication.
     * @param nebulaPoolConfig  The configurations for multiple connections to Nebula Graph.
     */
    public MapleNebulaDslSessionFactory(MapleDslConfiguration configuration, List<HostAddress> hostAddress, String space, String userName, String password, NebulaPoolConfig nebulaPoolConfig) {
        this(configuration, hostAddress, space, userName, password, nebulaPoolConfig, true);
    }

    /**
     * Creates an instance of MapleNebulaDslSessionFactory.
     *
     * @param configuration The MapleDslConfiguration object used for Maple DSL execution.
     * @param hostAddress   The list of Nebula Graph server addresses and ports.
     * @param space         The name of the space in Nebula Graph to connect to.
     * @param userName      The username for authentication.
     * @param password      The password for authentication.
     * @param nebulaPoolConfig The configurations for multiple connections to Nebula Graph.
     * @param reconnect     Whether to reconnect to Nebula Graph when the connection is lost.
     */
    public MapleNebulaDslSessionFactory(MapleDslConfiguration configuration, List<HostAddress> hostAddress, String space, String userName, String password, NebulaPoolConfig nebulaPoolConfig, boolean reconnect) {
        this.space = space;
        this.userName = userName;
        this.password = password;
        this.reconnect = reconnect;
        this.configuration = configuration;
        this.nebulaPool = createNebulaPool(hostAddress, nebulaPoolConfig);
    }

    @Override
    public final MapleDslSession openSession() {
        try {
            final Session session = this.nebulaPool.getSession(userName, password, reconnect);
            session.execute("use " + space);
            return new MapleNebulaDslSession(session, configuration);
        } catch (NotValidConnectionException | AuthFailedException | ClientServerIncompatibleException |
                 IOErrorException e) {
            throw new MapleDslExecutionException("Error opening session. Cause: " + e.getMessage(), e);
        }
    }

    @Override
    public MapleDslConfiguration configuration() {
        return configuration;
    }

    final NebulaPool createNebulaPool(List<HostAddress> hostAddresses, NebulaPoolConfig nebulaPoolConfig){
        try {
            final NebulaPool pool = new NebulaPool();
            pool.init(hostAddresses, nebulaPoolConfig);
            return pool;
        } catch (Exception e) {
            throw new MapleDslExecutionException("Error connect . Cause: " + e.getMessage(), e);
        }
    }
}
