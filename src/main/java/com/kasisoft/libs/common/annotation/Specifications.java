package com.kasisoft.libs.common.annotation;

import java.lang.annotation.*;

/**
 * Container for multiple {@link Specification} elements.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Specifications {

    /**
     * Returns a list of {@link Specification} elements.
     *
     * @return A list of {@link Specification} elements.
     */
    Specification[] value();

} /* ENDANNOTATION */
