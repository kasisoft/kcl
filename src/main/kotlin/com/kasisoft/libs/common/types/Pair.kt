package com.kasisoft.libs.common.types

/**
 * Simple class used to work as a container (f.e. out-parameters).
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
data class Pair<T1: Any?, T2: Any?>(var value1: T1, var value2: T2): HasFirstAndLast<T1, T2>, Map.Entry<T1, T2> {

    override var key: T1
        get() = value1
        set(value) {
            value1 = value
        }

    override var value: T2
        get() = value2
        set(value) {
            value2 = value
        }

    override fun findFirst(): T1? = value1

    override fun findLast(): T2? = value2

} /* ENDCLASS */
