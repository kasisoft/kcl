package com.kasisoft.libs.common.types

/**
 * Simple class used to work as a container (f.e. out-parameters).
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
data class Triple<T1: Any?, T2: Any?, T3: Any?>(var value1: T1, var value2: T2, var value3: T3): HasFirstAndLast<T1, T3> {

    override fun findFirst(): T1? = value1

    override fun findLast(): T3? = value3

} /* ENDCLASS */
