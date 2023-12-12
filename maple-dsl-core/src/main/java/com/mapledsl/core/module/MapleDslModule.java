package com.mapledsl.core.module;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.function.Predicate;

/**
 * The abstract class {@code MapleDslModule} represents a Maple DSL module.
 * Subclasses of this class must implement methods to provide the version, dialect, and dialect properties.
 */
public abstract class MapleDslModule {
    /**
     * Returns the version of the Maple DSL module.
     *
     * @return The version of the Maple DSL module as a non-null string.
     */
    public abstract @NotNull String version();
    /**
     * Returns the dialect of the Maple DSL module.
     * This method should be implemented in subclasses of {@code MapleDslModule}.
     * It returns a {@code String} representing the dialect of the DSL module.
     *
     * @return The dialect of the Maple DSL module as a non-null string.
     */
    public abstract @NotNull String dialect();
    /**
     * Returns the properties specific to the dialect of the Maple DSL module.
     * This method should be implemented in subclasses of {@code MapleDslModule}.
     * It returns a {@code Properties} object that contains the dialect-specific properties.
     *
     * @return The dialect properties of the Maple DSL module as a non-null {@code Properties} object.
     */
    public abstract @NotNull Properties dialectProperties();

    /**
     * Returns a predicate that checks if the given version string is equal to the version of the Maple DSL module.
     * The version is obtained by calling the {@link #version()} method of the {@code MapleDslModule} instance.
     *
     * @return A predicate that checks if the given version string is equal to the version of the Maple DSL module.
     */
    public final Predicate<String> versionPredicate() {
        return version()::equalsIgnoreCase;
    }

    /**
     * Returns a predicate that checks if the given string is equal to the dialect of the Maple DSL module.
     * The dialect is obtained by calling the {@link #dialect()} method of the {@code MapleDslModule} instance.
     *
     * @return A predicate that checks if the given string is equal to the dialect of the Maple DSL module.
     */
    public final Predicate<String> dialectPredicate() {
        return dialect()::equalsIgnoreCase;
    }

    @Override
    public String toString() {
        return version();
    }
}
