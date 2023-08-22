package com.mapledsl.core.extension.func;

/**
 * @author bofa1ex
 * @since 2023/08/22
 */
@FunctionalInterface
public interface ObjIntBiFunction {
    Object apply(Object bean, int value);
}
