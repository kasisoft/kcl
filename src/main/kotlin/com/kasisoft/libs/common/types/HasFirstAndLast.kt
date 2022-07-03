package com.kasisoft.libs.common.types

import com.kasisoft.libs.common.*

import java.util.*

interface HasFirst<T> {

    fun findFirst(): T?

    fun getFirst(): T = findFirst() ?: throw KclException("no first element")

} /* ENDINTERFACE */

interface HasLast<T> {

    fun findLast(): T?

    fun getLast(): T = findLast() ?: throw KclException("no last element")

} /* ENDINTERFACE */

interface HasFirstAndLast<F, L>: HasFirst<F>, HasLast<L>
