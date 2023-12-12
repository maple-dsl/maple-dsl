package com.mapledsl.core.extension.func;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A functional interface representing a supplier that can throw a checked exception.
 *
 * @param <T> the type of the result supplied by the supplier
 * @param <E> the type of the checked exception that can be thrown
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {
    /**
     * Retrieves a result through the supplier function.
     *
     * @param <T> the type of the result
     * @param <E> the type of the checked exception that can be thrown
     * @return the result obtained by executing the supplier function
     * @throws E if the supplier function throws a checked exception
     */
    T get() throws E;

    /**
     * Returns an Optional object that contains the result of the supplier function
     * or an empty Optional if an exception is thrown.
     *
     * @param <T>      the type of the result supplied by the supplier
     * @param supplier the supplier function to be executed
     * @return an Optional object that contains the result of the supplier function
     *         or an empty Optional if an exception is thrown
     * @throws NullPointerException if the supplier is null
     */
    static <T> Optional<T> opt(ThrowingSupplier<? extends T, ?> supplier) {
        requireNonNull(supplier);
        try {
            return Optional.of(supplier.get());
        } catch (Throwable ex) {
            return SneakyThrowUtil.sneakyThrow(ex);
        }
    }

    /**
     * Returns a supplier that wraps a throwing supplier and handles checked exceptions by throwing them as unchecked exceptions.
     *
     * @param <T> the type of the result supplied by the supplier
     * @param supplier the throwing supplier to be wrapped
     * @return a supplier that wraps the throwing supplier and handles checked exceptions
     * @throws NullPointerException if the supplier is null
     */
    static <T> Supplier<T> sneaky(ThrowingSupplier<? extends T, ?> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable ex) {
                return SneakyThrowUtil.sneakyThrow(ex);
            }
        };
    }
}
