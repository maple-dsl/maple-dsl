package com.mapledsl.core.extension;

/**
 * Represents a strategy for generating key policies.
 */
@FunctionalInterface
public interface KeyPolicyStrategy {
    Object generate(Object source);
}
