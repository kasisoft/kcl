package com.kasisoft.libs.common.functional

import com.kasisoft.libs.common.KclException
import java.util.function.Predicate

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */

fun interface TriPredicate<T: Any?, U: Any?, Z: Any?> {

    fun test(t: T, u: U, z: Z): Boolean

} /* ENDINTERFACE */

fun interface KTriPredicate<T: Any?, U: Any?, Z: Any?> {

    @Throws(Exception::class)
    fun test(t: T, u: U, z: Z): Boolean

    fun protect(): TriPredicate<T, U, Z> =
        TriPredicate { t: T, u: U, z: Z ->
            try {
                test(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun negate(): TriPredicate<T, U, Z> =
        TriPredicate { t: T, u: U, z: Z ->
            try {
                !test(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun and(after: KTriPredicate<T, U, Z>) =
        TriPredicate { t: T, u: U, z: Z ->
            try {
                test(t, u,  z) && after.test(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun and(after: TriPredicate<T, U, Z>) =
        TriPredicate { t: T, u: U, z: Z ->
            try {
                test(t, u,  z) && after.test(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun or(after: KTriPredicate<T, U, Z>) =
        TriPredicate { t: T, u: U, z: Z ->
            try {
                test(t, u,  z) || after.test(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun or(after: TriPredicate<T, U, Z>) =
        TriPredicate { t: T, u: U, z: Z ->
            try {
                test(t, u,  z) || after.test(t, u, z)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */
