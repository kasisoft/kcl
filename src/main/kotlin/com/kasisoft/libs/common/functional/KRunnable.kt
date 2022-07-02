package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

/**
 * This implementation allows to throw Exceptions (useful on the Java side)
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
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
