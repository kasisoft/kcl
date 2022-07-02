package com.kasisoft.libs.common.functional

import com.kasisoft.libs.common.*
import java.util.function.Function
import java.util.function.*

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
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
