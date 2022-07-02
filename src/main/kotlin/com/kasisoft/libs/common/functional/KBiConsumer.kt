package com.kasisoft.libs.common.functional

import com.kasisoft.libs.common.*

import java.util.function.*

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
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
