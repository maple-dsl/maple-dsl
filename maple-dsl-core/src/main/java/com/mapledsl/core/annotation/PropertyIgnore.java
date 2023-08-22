package com.mapledsl.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Marker annotation that indicates that the logical property that
 * the accessor (field)
 * is to be ignored by introspection-based
 * serialization and deserialization functionality.
 <p>
 <pre>
 class Person extends Model.V {
    {@literal @Property}("name") String name;
    {@literal @PropertyIgnore} long serializedId;
 }
 </pre>
 * @author bofa1ex
 * @since 2023/08/08
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyIgnore {
}
