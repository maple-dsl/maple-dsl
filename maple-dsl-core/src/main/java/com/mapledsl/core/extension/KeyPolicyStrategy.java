package com.mapledsl.core.extension;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
@FunctionalInterface
public interface KeyPolicyStrategy {
    Object generate(Object source);
}
