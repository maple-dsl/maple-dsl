package com.mapledsl.core.extension.func;

import com.mapledsl.core.exception.MapleDslException;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Functional interface for a throwing function.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of exception that can be thrown
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
    /**
     * Applies the throwing function to the given argument.
     *
     * @param arg the argument to apply the function to
     * @return the result of applying the function to the argument
     * @throws E if an exception occurs while applying the function
     */
    R apply(T arg) throws E;

    /**
     * Returns a function that wraps a throwing function, handling any thrown exceptions and throwing them
     * as unchecked exceptions. This allows the throwing function to be used in functional interfaces
     * that do not allow checked exceptions to be thrown.
     *
     * @param <T1>     the type of the input to the function
     * @param <R>      the type of the result of the function
     * @param function the throwing function to be wrapped
     * @return a function that wraps the throwing function
     * @throws NullPointerException if the input throwing function is null
     */
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

    /**
     * Applies the given throwing function to the argument and handles any thrown exceptions by throwing a custom exception.
     *
     * @param <T1> the type of the input to the function
     * @param <R> the type of the result of the function
     * @param <X> the type of the custom exception
     * @param function the throwing function to be applied to the argument
     * @param exceptionSupplier a supplier that provides the custom exception to be thrown
     * @return a function that applies the throwing function to the argument and throws a custom exception if an exception occurs
     * @throws NullPointerException if either the function or the exceptionSupplier is null
     */
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
