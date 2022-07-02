package com.kasisoft.libs.common.functional

import com.kasisoft.libs.common.*

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
fun interface TriConsumer<T: Any?, U: Any?, Z: Any?> {

    fun accept(t: T, u: U, z: Z)

} /* ENDINTERFACE */

fun interface KTriConsumer<T: Any?, U: Any?, Z: Any?> {

    @Throws(Exception::class)
    fun accept(t: T, u: U, z: Z)

    fun protect(): TriConsumer<T, U, Z> =
        TriConsumer { t: T, u: U, z: Z ->
            try {
                accept(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */
