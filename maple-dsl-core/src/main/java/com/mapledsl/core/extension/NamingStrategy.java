package com.mapledsl.core.extension;

import java.util.Locale;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
@FunctionalInterface
public interface NamingStrategy {
    String translate(String input, Locale locale);
}
