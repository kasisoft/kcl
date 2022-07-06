package com.kasisoft.libs.common.pools

import com.kasisoft.libs.common.text.*

import java.util.*
import java.io.*

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
object Buckets {

    @JvmStatic
    fun <R> arrayList() = Bucket( { ArrayList<R>() }, { it.clear() } )

    @JvmStatic
    fun <R> linkedList() = Bucket( { LinkedList<R>() }, { it.clear() } )

    @JvmStatic
    fun byteArrayOutputStream() = Bucket( { ByteArrayOutputStream() }, { it.reset() } )

    @JvmStatic
    fun charArrayWriter() = Bucket( { CharArrayWriter() }, { it.reset() } )

    @JvmStatic
    fun stringBuilder() = Bucket( { StringBuilder() }, { it.setLength(0) } )

    @JvmStatic
    fun stringFBuilder() = Bucket( { StringFBuilder() }, { it.setLength(0) } )

    @JvmStatic
    fun stringBuffer() = Bucket( { StringBuffer() }, { it.setLength(0) } )

    @JvmStatic
    fun stringWriter() = Bucket( { StringWriter() }, { it.buffer.setLength(0) } )

    @JvmStatic
    fun <K, V> hashMap() = Bucket( { HashMap<K, V>() }, { it.clear() } )

} /* ENDOBJECT */
