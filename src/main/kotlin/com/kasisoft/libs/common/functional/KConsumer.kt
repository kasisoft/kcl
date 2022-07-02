package com.kasisoft.libs.common.functional

import com.kasisoft.libs.common.*

import java.util.function.*

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
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
