package com.mapledsl.core.condition;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.model.Model;

/**
 * Wrapper is a functional interface representing a rendering operation for a MapleDslConfiguration context.
 * It provides a default method to render using the primary configuration.
 * It also includes an UnsupportedWrapper interface which can be implemented to throw an UnsupportedOperationException.
 *
 * @see MapleDslConfiguration
 * @see UnsupportedOperationException
 */
@FunctionalInterface
public interface Wrapper<M extends Model<?>> extends WrapperConstants {
    String render(MapleDslConfiguration context);

    default String render() throws MapleDslException {
        return render(MapleDslConfiguration.primaryConfiguration());
    }

    interface UnsupportedWrapper<M extends Model<?>> extends Wrapper<M> {
        @Override
        default String render(MapleDslConfiguration context) {
            throw new UnsupportedOperationException();
        }
    }
}