package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import java.util.function.*;

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
fun interface KPredicate<T: Any?> {

    @Throws(Exception::class)
    fun test(t: T): Boolean

    fun protect(): Predicate<T> =
        Predicate<T> { t: T ->
            try {
                test(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun negate(): Predicate<T> =
        Predicate { t: T ->
            try {
                !test(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun and(after: Predicate<T>) =
        Predicate { t: T ->
            try {
                test(t) && after.test(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun and(after: KPredicate<T>) =
        Predicate { t: T ->
            try {
                test(t) && after.test(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun or(after: Predicate<T>) =
        Predicate { t: T ->
            try {
                test(t) || after.test(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

    fun or(after: KPredicate<T>) =
        Predicate { t: T ->
            try {
                test(t) || after.test(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */
