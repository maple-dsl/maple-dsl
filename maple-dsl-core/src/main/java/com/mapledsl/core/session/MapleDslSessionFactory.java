package com.mapledsl.core.session;

import com.mapledsl.core.MapleDslConfiguration;

/**
 * @author bofa1ex
 * @since 2022/05/18
 */
public interface MapleDslSessionFactory {
    MapleDslSession openSession();

    MapleDslConfiguration configuration();
}
