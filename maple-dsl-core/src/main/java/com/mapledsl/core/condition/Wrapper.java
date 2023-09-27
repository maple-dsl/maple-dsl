package com.mapledsl.core.condition;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;

/**
 * @author bofa1ex
 * @since 2023/08/28
 */
public interface Wrapper<Children> {
    Children limit(int limit);
    Children limit(int skip, int limit);

    String render(MapleDslConfiguration context);

    default String render() throws MapleDslException {
        return render(MapleDslConfiguration.primaryConfiguration());
    }
}