package com.mapledsl.core.condition;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;

/**
 * @author bofa1ex
 * @since 2023/08/28
 */
@FunctionalInterface
public interface Wrapper extends WrapperConstants {
    String render(MapleDslConfiguration context);

    default String render() throws MapleDslException {
        return render(MapleDslConfiguration.primaryConfiguration());
    }

    interface UnsupportedWrapper extends Wrapper {
        @Override
        default String render(MapleDslConfiguration context) {
            throw new UnsupportedOperationException();
        }
    }
}