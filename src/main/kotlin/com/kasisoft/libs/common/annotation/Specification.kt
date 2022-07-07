package com.kasisoft.libs.common.annotation

/**
 * This annotation is purely meant to be used for documentation purposes.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
annotation class Specification(

    /**
     * Returns the location of a specification. Typically a URL instance.
     *
     * @return The location of a specification.
     */
    val value: String,

    /**
     * Returns the date when the specification has been checked the last time.
     *
     * @return The date when the specification has been checked the last time.
     */
    val date: String = "",

) /* ENDANNOTATION */
