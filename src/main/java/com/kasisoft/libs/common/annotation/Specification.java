package com.kasisoft.libs.common.annotation;

import java.lang.annotation.*;

/**
 * This annotation is purely meant to be used for documentation purposes.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Specification {

    /**
     * Returns the location of a specification. Typically a URL instance.
     *
     * @return The location of a specification.
     */
    String value();

    /**
     * Returns the date when the specification has been checked the last time.
     *
     * @return The date when the specification has been checked the last time.
     */
    String date() default "";

} /* ENDANNOTATION */
