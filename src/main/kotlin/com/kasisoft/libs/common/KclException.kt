package com.kasisoft.libs.common

import java.util.function.*

/**
 * Specialisation of the RuntimeException which is commonly used within this library.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
open class KclException: RuntimeException {

    constructor() : super()

    constructor(ex: Exception) : super(ex)

    constructor(fmt: String, vararg args: Any) : super(formatString(fmt, *args))

    constructor(ex: Exception, fmt: String, vararg args: Any): super(formatString(fmt, *args), ex)

    companion object {

        private fun formatString(fmt: String, vararg args: Any): String =
            if (args.isNotEmpty()) {
                String.format(fmt, *args)
            } else {
                fmt
            }

        @JvmStatic
        fun wrap(ex: Exception): KclException =
            when (ex) {
                is KclException -> ex
                else -> KclException(ex)
            }

        @JvmStatic
        fun wrap(ex: Exception, fmt: String, vararg args: Any): KclException =
            when (ex) {
                is KclException -> ex
                else -> KclException(ex, fmt, *args)
            }


        @JvmStatic
        fun unwrap(ex: Exception?): KclException? =
            if  (ex is KclException) {
                ex
            } else if (ex!!.cause is Exception) {
                unwrap(ex.cause as Exception)
            } else {
                null
            }

        @JvmStatic
        @Deprecated("KASI")
        fun <R> execute(supplier: Supplier<R>, fmt: String, vararg args: Any): R {
            return try {
                supplier.get()
            } catch (ex: Exception) {
                throw wrap(ex, fmt, *args)
            }
        }

    } /* ENDOBJECT */

} /* ENDCLASS */