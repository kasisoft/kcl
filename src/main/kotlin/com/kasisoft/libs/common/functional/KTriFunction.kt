package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.KclException

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */

fun interface TriFunction<T: Any?, U: Any?, Z: Any?, R: Any?> {

    fun apply(t: T, u: U, z: Z): R

} /* ENDINTERFACE */


fun interface KTriFunction<T: Any?, U: Any?, Z: Any?, R: Any?> {

    @Throws(Exception::class)
    fun apply(t: T, u: U, z: Z): R

    fun protect(): TriFunction<T, U, Z, R> =
        TriFunction { t: T, u: U, z: Z ->
            try {
                apply(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */
