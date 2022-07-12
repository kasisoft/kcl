package com.kasisoft.libs.common.constants

import java.io.*

/**
 * Collection of constants used to indicate empty elements.
 * 
 * @author daniel.kasmeroglu@kasisoft.com
 */
object Empty {

    @JvmField
    val NO_BOOLEANS = BooleanArray(0)

    @JvmField
    val NO_CHARS = CharArray(0)

    @JvmField
    val NO_FLOATS = FloatArray(0)

    @JvmField
    val NO_DOUBLES = DoubleArray(0)

    @JvmField
    val NO_BYTES = ByteArray(0)

    @JvmField
    val NO_SHORTS = ShortArray(0)

    @JvmField
    val NO_INTS = IntArray(0)

    @JvmField
    val NO_LONGS = LongArray(0)

    @JvmField
    val NO_OBJECTS = arrayOfNulls<Any>(0)

    val NO_FILES = arrayOfNulls<File>(0)

    val NO_STRINGS = arrayOfNulls<String>(0)

    const val NO_STRING = ""

} /* ENDCLASS */
