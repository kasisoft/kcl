package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import java.util.function.*;

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
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
