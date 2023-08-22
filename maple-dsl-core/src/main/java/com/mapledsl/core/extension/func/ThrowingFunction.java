package com.mapledsl.core.extension.func;

import com.mapledsl.core.exception.MapleDslException;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
    R apply(T arg) throws E;

    static <T1, R> Function<T1, R> sneaky(ThrowingFunction<? super T1, ? extends R, ?> function) {
        requireNonNull(function);
        return t -> {
            try {
                return function.apply(t);
            } catch (final Throwable ex) {
                return SneakyThrowUtil.sneakyThrow(ex);
            }
        };
    }

    static <T1, R, X extends MapleDslException> Function<T1, R> check(ThrowingFunction<? super T1, ? extends R, ?> function, Supplier<? extends X> exceptionSupplier) {
        requireNonNull(function);
        return t -> {
            try {
                return function.apply(t);
            } catch (final Throwable ex) {
                throw exceptionSupplier.get();
            }
        };
    }
}
