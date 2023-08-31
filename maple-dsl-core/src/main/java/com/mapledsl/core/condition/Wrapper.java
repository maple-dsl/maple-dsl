package com.mapledsl.core.condition;

import com.mapledsl.core.MapleDslConfiguration;

/**
 * @author bofa1ex
 * @since 2023/08/28
 */
public interface Wrapper<Children> {
    String render(MapleDslConfiguration context);

    Children limit(int limit);
    Children limit(int skip, int limit);
}
