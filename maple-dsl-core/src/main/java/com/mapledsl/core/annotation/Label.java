package com.mapledsl.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Identifies a domain entity as being backed by a vertex/edge in the graph.
 * This annotation is not needed if the domain entity's simple class name
 * matches at least one of the labels of the vertex/edge in the graph.
 *
 * @author bofa1ex
 * @since 2023/08/22
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Label {
    String value();
}