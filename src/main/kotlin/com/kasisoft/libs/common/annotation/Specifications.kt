package com.kasisoft.libs.common.annotation

/**
 * Container for multiple {@link Specification} elements.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
annotation class Specifications(

    /**
     * Returns a list of {@link Specification} elements.
     *
     * @return A list of {@link Specification} elements.
     */
    val value: Array<Specification>

) /* ENDANNOTATION */
