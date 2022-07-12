package com.kasisoft.libs.common.utils

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
open class ExtendedList<T: Any?>(val origin : MutableList<T>) : MutableList<T> {

    override val size : Int
        get() {
            return origin.size
        }

    override fun isEmpty() : Boolean = origin.isEmpty()

    override fun add(element : T): Boolean = origin.add(element)

    override fun add(index : Int, element: T) = origin.add(MiscFunctions.adjustIndex(size, index, false), element)

    override fun addAll(elements : Collection<T>): Boolean = addAll(size, elements)

    override fun addAll(index : Int, elements : Collection<T>): Boolean = origin.addAll(MiscFunctions.adjustIndex(size, index, false), elements)

    override fun get(index : Int): T = origin.get(MiscFunctions.adjustIndex(size, index, false))

    override fun removeAt(index : Int): T = origin.removeAt(MiscFunctions.adjustIndex(size, index, false))

    override fun set(index : Int, element : T): T = origin.set(MiscFunctions.adjustIndex(size, index, false), element)

    override fun listIterator(index : Int): MutableListIterator<T> = origin.listIterator(MiscFunctions.adjustIndex(size, index, false))

    override fun subList(fromIndex : Int, toIndex : Int): MutableList<T> = origin.subList(MiscFunctions.adjustIndex(size, fromIndex, false), MiscFunctions.adjustIndex(size, toIndex, true))

    override fun contains(element : @UnsafeVariance T) : Boolean = origin.contains(element)

    override fun iterator() : MutableIterator<T> =  origin.iterator()

    override fun remove(element : T): Boolean = origin.remove(element)

    override fun containsAll(elements : Collection<@UnsafeVariance T>) : Boolean = origin.containsAll(elements)

    override fun removeAll(elements : Collection<T>) : Boolean = origin.removeAll(elements)

    override fun retainAll(elements : Collection<T>) : Boolean = origin.retainAll(elements)

    override fun clear() {
        origin.clear()
    }

    override fun indexOf(element : @UnsafeVariance T): Int = origin.indexOf(element)

    override fun lastIndexOf(element : @UnsafeVariance T): Int = origin.lastIndexOf(element)

    override fun listIterator(): MutableListIterator<T> = origin.listIterator()

} /* ENDCLASS */
