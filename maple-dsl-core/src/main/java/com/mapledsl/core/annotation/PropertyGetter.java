package com.mapledsl.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation that can be used to define a non-static,
 * no-argument value-returning (non-void) method to be used as a "getter"
 * for a logical property.
 * It can be used as an alternative to more general
 * {@link Property} annotation (which is the recommended choice in
 * general case).
 * Getter means that when serializing Object instance of class that has
 * this method (possibly inherited from a super class), a call is made
 * through the method, and return value will be serialized as value of
 * the property.
 <pre>
 class Person extends Model.V {
     String name;
    {@literal @PropertyGetter}("name") String getPersonName() { return name; }
 }
 </pre>
 * @author bofa1ex
 * @since 2023/08/08
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyGetter {
    /**
     * Defines name of the logical property this
     * method is used to access ("get"); empty String means that
     * name should be derived from the underlying method (using
     * standard Bean name detection rules)
     *
     * @return Name of the logical property (or "" to use 'default',
     * if available)
     */
    String value() default "";

    /**
     * @return property getter order.
     */
    int order() default -1;
}
