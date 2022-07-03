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

private inline fun <T: Any?> guard(op: () -> T): T =
    try {
        op()
    } catch (ex: Exception) {
        throw KclException.wrap(ex)
    }


fun interface KConsumer<T: Any?> {

    @Throws(Exception::class)
    fun accept(t: T)

    fun protect(): Consumer<T> = Consumer<T> { t: T -> guard { accept(t) } }

} /* ENDINTERFACE */

fun interface KBiConsumer<T: Any?, U: Any?> {

    @Throws(Exception::class)
    fun accept(t: T, u: U)

    fun protect(): BiConsumer<T, U> = BiConsumer<T, U> { t: T, u: U -> guard { accept(t, u) } }

} /* ENDINTERFACE */

fun interface TriConsumer<T: Any?, U: Any?, Z: Any?> {

    fun accept(t: T, u: U, z: Z)

} /* ENDINTERFACE */

fun interface KTriConsumer<T: Any?, U: Any?, Z: Any?> {

    @Throws(Exception::class)
    fun accept(t: T, u: U, z: Z)

    fun protect(): TriConsumer<T, U, Z> = TriConsumer { t: T, u: U, z: Z -> guard { accept(t, u, z ) } }

} /* ENDINTERFACE */

fun interface KFunction<T: Any?, R: Any?> {

    @Throws(Exception::class)
    fun apply(t: T): R

    fun protect(): Function<T, R> = Function<T, R> { t: T -> guard { apply(t) } }

} /* ENDINTERFACE */

fun interface KBiFunction<T: Any?, U: Any?, R: Any?> {

    @Throws(Exception::class)
    fun apply(t: T, u: U): R

    fun protect(): BiFunction<T, U, R> = BiFunction<T, U, R> { t: T, u: U -> guard { apply(t, u) } }

} /* ENDINTERFACE */

fun interface TriFunction<T: Any?, U: Any?, Z: Any?, R: Any?> {

    fun apply(t: T, u: U, z: Z): R

} /* ENDINTERFACE */

fun interface KTriFunction<T: Any?, U: Any?, Z: Any?, R: Any?> {

    @Throws(Exception::class)
    fun apply(t: T, u: U, z: Z): R

    fun protect(): TriFunction<T, U, Z, R> =  TriFunction { t: T, u: U, z: Z -> guard { apply(t, u, z) } }

} /* ENDINTERFACE */

fun interface KPredicate<T: Any?> {

    @Throws(Exception::class)
    fun test(t: T): Boolean

    fun protect(): Predicate<T> = Predicate<T> { t: T -> guard { test(t) } }

    fun negate(): Predicate<T> = Predicate { t: T -> guard { !test(t) } }

    fun and(after: Predicate<T>) = Predicate { t: T -> guard { test(t) && after.test(t) } }

    fun and(after: KPredicate<T>) = Predicate { t: T -> guard { test(t) && after.test(t) } }

    fun or(after: Predicate<T>) = Predicate { t: T -> guard { test(t) || after.test(t) } }

    fun or(after: KPredicate<T>) = Predicate { t: T -> guard { test(t) || after.test(t) } }

} /* ENDINTERFACE */

fun interface KBiPredicate<T: Any?, U: Any?> {

    @Throws(Exception::class)
    fun test(t: T, u: U): Boolean

    fun protect(): BiPredicate<T, U> = BiPredicate<T, U> { t: T, u: U -> guard { test(t, u) } }

    fun negate(): BiPredicate<T, U> = BiPredicate { t: T, u: U -> guard { !test(t, u) } }

    fun and(after: BiPredicate<T, U>) = BiPredicate { t: T, u: U -> guard { test(t, u) && after.test(t, u) } }

    fun and(after: KBiPredicate<T, U>) = BiPredicate { t: T, u: U -> guard { test(t, u) && after.test(t, u) } }

    fun or(after: BiPredicate<T, U>) = BiPredicate { t: T, u: U -> guard { test(t, u) || after.test(t, u) } }

    fun or(after: KBiPredicate<T, U>) = BiPredicate { t: T, u: U -> guard { test(t, u) || after.test(t, u) } }

} /* ENDINTERFACE */

fun interface TriPredicate<T: Any?, U: Any?, Z: Any?> {

    fun test(t: T, u: U, z: Z): Boolean

} /* ENDINTERFACE */

fun interface KTriPredicate<T: Any?, U: Any?, Z: Any?> {

    @Throws(Exception::class)
    fun test(t: T, u: U, z: Z): Boolean

    fun protect(): TriPredicate<T, U, Z> = TriPredicate { t: T, u: U, z: Z -> guard { test(t, u, z) } }

    fun negate(): TriPredicate<T, U, Z> = TriPredicate { t: T, u: U, z: Z -> guard { !test(t, u, z) } }

    fun and(after: KTriPredicate<T, U, Z>) = TriPredicate { t: T, u: U, z: Z -> guard { test(t, u,  z) && after.test(t, u, z) } }

    fun and(after: TriPredicate<T, U, Z>) = TriPredicate { t: T, u: U, z: Z -> guard { test(t, u,  z) && after.test(t, u, z) } }

    fun or(after: KTriPredicate<T, U, Z>) = TriPredicate { t: T, u: U, z: Z -> guard { test(t, u,  z) || after.test(t, u, z) } }

    fun or(after: TriPredicate<T, U, Z>) = TriPredicate { t: T, u: U, z: Z -> guard { test(t, u,  z) || after.test(t, u, z) } }

} /* ENDINTERFACE */

fun interface KSupplier<R: Any?> {

    @Throws(Exception::class)
    fun get(): R

    fun protect(): Supplier<R> = Supplier<R> { guard { get() } }

} /* ENDINTERFACE */

fun interface KRunnable {

    @Throws(Exception::class)
    fun run()

    fun protect(): Runnable = Runnable { guard { run() } }

} /* ENDINTERFACE */


/*
 * The following types are shortcuts in case parameterized types are all the same. I would have preferred
 * to use <code>typealias</code> instead which isn't possible as type boundaries aren't supported.
 */

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
