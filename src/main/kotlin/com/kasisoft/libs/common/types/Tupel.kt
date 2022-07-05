package com.kasisoft.libs.common.types

/**
 * Simple class used to work as a container.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
data class Tupel<T: Any?>(var values: Array<T>): HasFirstAndLast<T, T> {

    val length: Int
        get() = values.size

    val empty: Boolean
        get() = length == 0

    override fun findLast(): T? = if (length > 0) values[length - 1] else null

    override fun findFirst(): T? = if (length > 0) values[0] else null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Tupel<*>
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }

} /* ENDCLASS */
