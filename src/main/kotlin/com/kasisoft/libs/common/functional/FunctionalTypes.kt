package com.kasisoft.libs.common.functional

/*
 * Collection of several functional types and abbreviations. The functional types provide methods using checked
 * exceptions (useful for the java side).
 *
 * The *ST* suffixed types only uses the same type for all generic type parameters.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */

import com.kasisoft.libs.common.*

import java.util.function.Function
import java.util.function.*

fun interface KConsumer<T: Any?> {

    @Throws(Exception::class)
    fun accept(t: T)

    fun protect(): Consumer<T> =
        Consumer<T> { t: T ->
            try {
                accept(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */

fun interface KBiConsumer<T: Any?, U: Any?> {

    @Throws(Exception::class)
    fun accept(t: T, u: U)

    fun protect(): BiConsumer<T, U> =
        BiConsumer<T, U> { t: T, u: U ->
            try {
                accept(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */

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

fun interface KFunction<T: Any?, R: Any?> {

    @Throws(Exception::class)
    fun apply(t: T): R

    fun protect(): java.util.function.Function<T, R> =
        Function<T, R> { t: T ->
            try {
                apply(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */

fun interface KBiFunction<T: Any?, U: Any?, R: Any?> {

    @Throws(Exception::class)
    fun apply(t: T, u: U): R

    fun protect(): BiFunction<T, U, R> =
        BiFunction<T, U, R> { t: T, u: U ->
            try {
                apply(t, u)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */

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

fun interface KSupplier<R: Any?> {

    @Throws(Exception::class)
    fun get(): R

    fun protect(): Supplier<R> =
        Supplier<R> {
            try {
                get()
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */

fun interface KRunnable {

    @Throws(Exception::class)
    fun run()

    fun protect(): Runnable =
        Runnable {
            try {
                run()
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */


fun interface FunctionST<T: Any?>: Function<T, T>
fun interface KFunctionST<T: Any?>: KFunction<T, T>

fun interface BiConsumerST<T: Any?>: BiConsumer<T, T>
fun interface BiPredicateST<T: Any?>: BiPredicate<T, T>
fun interface BiFunctionST<T: Any?>: BiFunction<T, T, T>

fun interface KBiConsumerST<T: Any?>: KBiConsumer<T, T>
fun interface KBiPredicateST<T: Any?>: KBiPredicate<T, T>
fun interface KBiFunctionST<T: Any?>: KBiFunction<T, T, T>

fun interface KTriConsumerST<T: Any?>: KTriConsumer<T, T, T>
fun interface KTriPredicateST<T: Any?>: KTriPredicate<T, T, T>
fun interface KTriFunctionST<T: Any?>: KTriFunction<T, T, T, T>
