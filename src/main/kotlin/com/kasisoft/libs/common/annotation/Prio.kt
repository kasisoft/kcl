package com.kasisoft.libs.common.annotation

/**
 * This annotation allows to provide some ordering to class types.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Prio(val value: Int = 0)
