package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import java.util.function.Function

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
fun interface KFunction<T: Any?, R: Any?> {

    @Throws(Exception::class)
    fun apply(t: T): R

    fun protect(): Function<T, R> =
        Function<T, R> { t: T ->
            try {
                apply(t)
            } catch (ex: Exception) {
                throw KclException.wrap(ex)
            }
        }

} /* ENDINTERFACE */
