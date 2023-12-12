package com.mapledsl.core.extension;

import java.util.Locale;

/**
 * Defines a functional interface for translating input using a naming strategy.
 */
@FunctionalInterface
public interface NamingStrategy {
    String translate(String input, Locale locale);
}
