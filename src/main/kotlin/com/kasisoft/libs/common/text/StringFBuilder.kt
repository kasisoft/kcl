package com.kasisoft.libs.common.text

import com.kasisoft.libs.common.utils.*

import javax.validation.constraints.*

import java.util.stream.*

import java.io.*

/**
 * StringF(ormatting)Builder  equivalent which supports formatting. This builder also supports negative indices which
 * means that the original index is calculated beginning from the end of the buffer.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
open class StringFBuilder: Serializable, StringLike<StringFBuilder> {

    companion object {

        private const val serialVersionUID = 1050795857819832439L

    }

    // the original implementation
    private var origin: StringBuilder

    override val length: Int
        get() = origin.length

    /**
     * @see StringBuilder.StringBuilder
     */
    constructor() {
        origin = StringBuilder()
    }

    /**
     * @see StringBuilder.StringBuilder
     */
    constructor(capacity: @Min(1) Int) {
        origin = StringBuilder(capacity)
    }

    /**
     * @see StringBuilder.StringBuilder
     */
    constructor(seq: CharSequence) {
        origin = StringBuilder(seq)
    }

    override fun get(index: Int): Char = origin[adjustIndex(index)]

    override fun capacity(): Int = origin.capacity()

    override fun ensureCapacity(minimum: Int): StringFBuilder {
        origin.ensureCapacity(minimum)
        return this
    }

    override fun trimToSize(): StringFBuilder {
        origin.trimToSize()
        return this
    }

    override fun setLength(newlength: Int): StringFBuilder {
        origin.setLength(newlength)
        return this
    }

    override fun codePointAt(index: Int): Int = origin.codePointAt(adjustIndex(index))

    override fun codePointBefore(index: Int): Int = origin.codePointBefore(adjustIndex(index))

    override fun codePointCount(begin: Int, end: Int): Int = origin.codePointCount(adjustIndex(begin), adjustIndex(end, true))

    override fun offsetByCodePoints(index: Int, codepointoffset: Int): Int = origin.offsetByCodePoints(adjustIndex(index), codepointoffset)

    override fun getChars(start: Int, end: Int, destination: CharArray, destbegin: Int): StringFBuilder {
        origin.getChars(
            adjustIndex(start),
            adjustIndex(end, true),
            destination,
            adjustIndex(destbegin, false, destination.size)
        )
        return this
    }

    override fun setCharAt(index: Int, ch: Char): StringFBuilder {
        origin.setCharAt(adjustIndex(index), ch)
        return this
    }

    override fun setCodepointAt(index: Int, codepoint: Int): StringFBuilder {
        val idx   = adjustIndex(index)
        val count = Character.charCount(codepoint)
        delete(idx, idx + count)
        return this
    }

    override fun append(obj: Any): StringFBuilder {
        origin.append(obj)
        return this
    }

    override fun append(sequence: CharSequence): StringFBuilder {
        origin.append(sequence)
        return this
    }

    override fun append(sequence: CharSequence, start: Int, end: Int): StringFBuilder {
        origin.append(
            sequence,
            adjustIndex(start, false, sequence.length),
            adjustIndex(end, true, sequence.length)
        )
        return this
    }

    override fun append(charray: CharArray): StringFBuilder {
        origin.append(charray)
        return this
    }

    override fun append(charray: CharArray, offset: Int, length: Int): StringFBuilder {
        origin.append(charray, adjustIndex(offset, false, charray.size), length)
        return this
    }

    override fun append(value: Boolean): StringFBuilder {
        origin.append(value)
        return this
    }

    override fun append(value: Char): StringFBuilder {
        origin.append(value)
        return this
    }

    override fun append(value: Int): StringFBuilder {
        origin.append(value)
        return this
    }

    override fun appendCodePoint(codepoint: Int): StringFBuilder {
        origin.appendCodePoint(codepoint)
        return this
    }

    override fun append(value: Long): StringFBuilder {
        origin.append(value)
        return this
    }

    override fun append(value: Float): StringFBuilder {
        origin.append(value)
        return this
    }

    override fun append(value: Double): StringFBuilder {
        origin.append(value)
        return this
    }

    override fun delete(start: Int, end: Int): StringFBuilder {
        origin.delete(adjustIndex(start), adjustIndex(end, true))
        return this
    }

    override fun deleteCharAt(index: Int): StringFBuilder {
        origin.deleteCharAt(adjustIndex(index))
        return this
    }

    override fun replace(start: Int, end: Int, str: String): StringFBuilder {
        origin.replace(adjustIndex(start), adjustIndex(end, true), str)
        return this
    }

    override fun substring(start: Int): String = origin.substring(adjustIndex(start))

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = origin.subSequence(adjustIndex(startIndex), adjustIndex(endIndex, true))

    override fun substring(start: Int, end: Int): String = origin.substring(adjustIndex(start), adjustIndex(end, true))

    override fun insert(index: Int, charray: CharArray, offset: Int, length: Int): StringFBuilder {
        origin.insert(adjustIndex(index), charray, adjustIndex(offset, false, charray.size), length)
        return this
    }

    override fun insert(offset: Int, obj: Any): StringFBuilder {
        origin.insert(adjustIndex(offset), obj)
        return this
    }

    override fun insert(offset: Int, value: CharArray): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun insert(offset: Int, value: CharSequence): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun insert(offset: Int, value: CharSequence, start: Int, end: Int): StringFBuilder {
        origin.insert(
            adjustIndex(offset),
            value,
            adjustIndex(start, false, value.length),
            adjustIndex(end, true, value.length)
        )
        return this
    }

    override fun insert(offset: Int, value: Boolean): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun insert(offset: Int, value: Char): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun insert(offset: Int, value: Int): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun insert(offset: Int, value: Long): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun insert(offset: Int, value: Float): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun insert(offset: Int, value: Double): StringFBuilder {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    override fun indexOf(str: String): Int = origin.indexOf(str)

    override fun indexOf(str: String, index: Int): Int = origin.indexOf(str, adjustIndex(index))

    override fun lastIndexOf(str: String): Int = origin.lastIndexOf(str)

    override fun lastIndexOf(str: String, index: Int): Int = origin.lastIndexOf(str, adjustIndex(index))

    override fun reverse(): StringFBuilder {
        origin.reverse()
        return this
    }

    override fun toString(): String = origin.toString()

    override fun chars(): IntStream = origin.chars()

    override fun codePoints(): IntStream = origin.codePoints()

    override fun compareTo(other: StringFBuilder): Int =
        if (this === other) {
            0
        } else {
            origin.compareTo(other.origin)
        }

    @Throws(IOException::class)
    private fun writeObject(s: ObjectOutputStream) {
        s.writeObject(origin)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(s: ObjectInputStream) {
        origin = s.readObject() as StringBuilder
    }

    private fun adjustIndex(index: Int, isEnd: Boolean = false, length: Int = origin.length): Int =
        if (index < 0) {
            length + index
        } else if ((index == 0) && isEnd) {
            length
        } else {
            index
        }

} /* ENDCLASS */
