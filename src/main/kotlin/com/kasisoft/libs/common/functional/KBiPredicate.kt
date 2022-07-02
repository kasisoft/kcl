package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import java.util.function.*;

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
fun interface KBiPredicate<T: Any?, U: Any?> {

    @Throws(Exception::class)
    fun test(t: T, u: U): Boolean

    fun protect(): BiPredicate<T, U> =
        BiPredicate<T, U> { t: T, u: U ->
            try {
                test(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun negate(): BiPredicate<T, U> =
        BiPredicate { t: T, u: U ->
            try {
                !test(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun and(after: BiPredicate<T, U>) =
        BiPredicate { t: T, u: U ->
            try {
                test(t, u) && after.test(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun and(after: KBiPredicate<T, U>) =
        BiPredicate { t: T, u: U ->
            try {
                test(t, u) && after.test(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun or(after: BiPredicate<T, U>) =
        BiPredicate { t: T, u: U ->
            try {
                test(t, u) || after.test(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun or(after: KBiPredicate<T, U>) =
        BiPredicate { t: T, u: U ->
            try {
                test(t, u) || after.test(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */
