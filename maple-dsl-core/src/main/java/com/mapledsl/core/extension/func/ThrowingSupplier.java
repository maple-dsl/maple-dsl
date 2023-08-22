package com.mapledsl.core.extension.func;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {
    T get() throws E;

    static <T> Optional<T> opt(ThrowingSupplier<? extends T, ?> supplier) {
        requireNonNull(supplier);
        try {
            return Optional.of(supplier.get());
        } catch (Throwable ex) {
            return SneakyThrowUtil.sneakyThrow(ex);
        }
    }

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
