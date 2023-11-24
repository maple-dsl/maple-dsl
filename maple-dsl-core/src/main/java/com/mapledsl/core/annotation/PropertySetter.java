package com.mapledsl.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation that can be used to define a non-static,
 * no-argument value-returning (non-void) method to be used as a "setter"
 * for a logical property.
 * It can be used as an alternative to more general
 * {@link Property} annotation (which is the recommended choice in
 * general case).
 * <p>
 * Setters means that when serializing Object instance of class that has
 * this method (possibly inherited from a super class), a call is made
 * through the method, and return value will be serialized as value of
 * the property.
 <pre>
 class Person extends Model.V {
     String name;

    {@literal @PropertySetter}("name")
     Person setPersonName(String name) {
        this.name = name;
        return this;
    }
 }
 </pre>
 * @author bofa1ex
 * @since 2023/08/08
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertySetter {
    /**
     * Optional default argument that defines logical property this
     * method is used to modify ("set"); this is the property
     * name used in JSON content.
     * @return property name
     */
    String value() default "";
}
