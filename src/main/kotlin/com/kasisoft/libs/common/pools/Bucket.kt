package com.kasisoft.libs.common.pools

import java.util.function.Function
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.*

import java.lang.ref.*

/**
 * Collector for often used objects like collections, maps etc. .
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
class Bucket<T>(val creator: Supplier<T>, val reset: Consumer<T>) {

    private val references = LinkedList<SoftReference<T>>()

    /**
     * Returns the number of references currently stored which means objects that can be acquired
     * without the need to create a new instance.
     *
     * @return   The number of references currently stored.
     */
    val size: Int
        get() = references.size

    val empty: Boolean
        get() = references.isEmpty()

    val notEmpty: Boolean
        get() = references.isNotEmpty()

    fun reset() {
        synchronized(references) {
            references.forEach { it.clear() }
            references.clear()
        }
    }

    /**
     * Creates a new object (potentially a reused one).
     *
     * @return   A new object.
     */
    fun allocate(): T {
        var result: T? = null
        synchronized(references) {
            while ((result == null) && references.isNotEmpty()) {
                val reference = references.removeAt(0)
                result = reference.get()
                reference.clear()
            }
        }
        return result ?: creator.get()
    }

    /**
     * Frees the supplied object, so it's allowed to be reused.
     *
     * @param object   The object that shall be freed.
     */
    fun free(obj: T?) {
        if (obj != null) {
            synchronized(references) {
                reset.accept(obj)
                references.add(SoftReference(obj))
            }
        }
    }

    /**
     * Executes the supplied function with the desired instance.
     *
     * @param function   The function that is supposed to be executed.
     *
     * @return   The return value of the supplied function.
     */
    fun <R> forInstance(function: Function<T, R>): R {
        val instance = allocate()
        return try {
            function.apply(instance)
        } finally {
            free(instance)
        }
    }

    /**
     * Executes the supplied consumer with the desired instance.
     *
     * @param consumer   The consumer that is supposed to be executed.
     */
    fun forInstanceDo(consumer: Consumer<T>) {
        val instance = allocate()
        try {
            consumer.accept(instance)
        } finally {
            free(instance)
        }
    }

} /* ENDCLASS */
