package me.shikashi.img.representations.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares that the return value of a method should be serialized to the JSON object.
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface ExposedMethod {
    /**
     * @return Gets the name of the property name in the JSON object.
     */
    String value() default "";
}