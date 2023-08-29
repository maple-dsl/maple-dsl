package com.mapledsl.core.condition;

import com.mapledsl.core.MapleDslConfiguration;

/**
 * @author bofa1ex
 * @since 2023/08/28
 */
public interface Wrapper {
    String render(MapleDslConfiguration context);
}
