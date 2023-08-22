package com.mapledsl.core.extension.func;

/**
 * @author bofa1ex
 * @since 2022/08/22
 */
@FunctionalInterface
public interface ObjLongBiFunction {
    Object apply(Object bean, long value);
}
