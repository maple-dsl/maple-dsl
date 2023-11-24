package com.mapledsl.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;

/**
 * Marker annotation that can be used to define a non-static
 * method as a "setter" or "getter" for a logical property
 * (depending on its signature),
 * or non-static object field to be used (serialized, deserialized) as
 * a logical property.
 <pre>
 class Person extends Model.V {
    {@literal @Property}("person_name") String name;
    {@literal @Property}("person_age") Integer age;
 }
 </pre>
 * @author bofa1ex
 * @since 2023/08/08
 */
@Target({ANNOTATION_TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Property {
    String value() default "";
    boolean defined() default true;
}
