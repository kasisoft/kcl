package com.kasisoft.libs.common.text

import com.kasisoft.libs.common.utils.*

import javax.validation.constraints.*

import java.util.function.*

import java.util.stream.*

import java.util.regex.Pattern
import java.util.regex.*
import java.util.function.Function
import java.util.*

import java.io.*

/**
 * StringF(ormatting)Buffer equivalent which supports formatting. This buffer also supports negative indices which
 * means that the original index is calculated beginning from the end of the buffer.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
open class StringFBuffer: Serializable, StringLike<StringFBuffer> {

    companion object {

        private const val serialVersionUID = 6094891463351971217L

    }

    // the original implementation
    private var origin: StringBuffer

    override val length: Int
        get() = origin.length

    /**
     * @see StringBuffer.StringBuffer
     */
    constructor() {
        origin = StringBuffer()
    }

    /**
     * @see StringBuffer.StringBuffer
     */
    constructor(capacity: @Min(1) Int) {
        origin = StringBuffer(capacity)
    }

    /**
     * @see StringBuffer.StringBuffer
     */
    constructor(seq: CharSequence) {
        origin = StringBuffer(seq)
    }

    @Synchronized
    override fun get(index: Int): Char = origin[adjustIndex(index)]

    @Synchronized
    override fun capacity(): Int = origin.capacity()

    @Synchronized
    override fun ensureCapacity(minimum: Int): StringFBuffer {
        origin.ensureCapacity(minimum)
        return this
    }

    @Synchronized
    override fun trimToSize(): StringFBuffer {
        origin.trimToSize()
        return this
    }

    @Synchronized
    override fun setLength(newlength: Int): StringFBuffer {
        origin.setLength(newlength)
        return this
    }

    @Synchronized
    override fun codePointAt(index: Int): Int = origin.codePointAt(adjustIndex(index))

    @Synchronized
    override fun codePointBefore(index: Int): Int = origin.codePointBefore(adjustIndex(index))

    @Synchronized
    override fun codePointCount(begin: Int, end: Int): Int = origin.codePointCount(adjustIndex(begin), adjustIndex(end, true))

    @Synchronized
    override fun offsetByCodePoints(index: Int, codepointoffset: Int): Int = origin.offsetByCodePoints(adjustIndex(index), codepointoffset)

    @Synchronized
    override fun getChars(start: Int, end: Int, destination: CharArray, destbegin: Int): StringFBuffer {
        origin.getChars(
            adjustIndex(start),
            adjustIndex(end, true),
            destination,
            adjustIndex(destbegin, false, destination.size)
        )
        return this
    }

    @Synchronized
    override fun setCharAt(index: Int, ch: Char): StringFBuffer {
        origin.setCharAt(adjustIndex(index), ch)
        return this
    }

    override fun setCodepointAt(index: Int, codepoint: Int): StringFBuffer {
        val idx   = adjustIndex(index)
        val count = Character.charCount(codepoint)
        delete(idx, idx + count)
        return this
    }

    @Synchronized
    override fun append(obj: Any): StringFBuffer {
        origin.append(obj)
        return this
    }

    @Synchronized
    override fun append(sequence: CharSequence): StringFBuffer {
        origin.append(sequence)
        return this
    }

    @Synchronized
    override fun append(sequence: CharSequence, start: Int, end: Int): StringFBuffer {
        origin.append(
            sequence,
            adjustIndex(start, false, sequence.length),
            adjustIndex(end, true, sequence.length)
        )
        return this
    }

    @Synchronized
    override fun append(charray: CharArray): StringFBuffer {
        origin.append(charray)
        return this
    }

    @Synchronized
    override fun append(charray: CharArray, offset: Int, length: Int): StringFBuffer {
        origin.append(charray, adjustIndex(offset, false, charray.size), length)
        return this
    }

    @Synchronized
    override fun append(value: Boolean): StringFBuffer {
        origin.append(value)
        return this
    }

    @Synchronized
    override fun append(value: Char): StringFBuffer {
        origin.append(value)
        return this
    }

    @Synchronized
    override fun append(value: Int): StringFBuffer {
        origin.append(value)
        return this
    }

    @Synchronized
    override fun appendCodePoint(codepoint: Int): StringFBuffer {
        origin.appendCodePoint(codepoint)
        return this
    }

    @Synchronized
    override fun append(value: Long): StringFBuffer {
        origin.append(value)
        return this
    }

    @Synchronized
    override fun append(value: Float): StringFBuffer {
        origin.append(value)
        return this
    }

    @Synchronized
    override fun append(value: Double): StringFBuffer {
        origin.append(value)
        return this
    }

    @Synchronized
    override fun delete(start: Int, end: Int): StringFBuffer {
        origin.delete(adjustIndex(start), adjustIndex(end, true))
        return this
    }

    @Synchronized
    override fun deleteCharAt(index: Int): StringFBuffer {
        origin.deleteCharAt(adjustIndex(index))
        return this
    }

    @Synchronized
    override fun replace(start: Int, end: Int, str: String): StringFBuffer {
        origin.replace(adjustIndex(start), adjustIndex(end, true), str)
        return this
    }

    @Synchronized
    override fun substring(start: Int): String = origin.substring(adjustIndex(start))

    @Synchronized
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = origin.subSequence(adjustIndex(startIndex), adjustIndex(endIndex, true))

    @Synchronized
    override fun substring(start: Int, end: Int): String = origin.substring(adjustIndex(start), adjustIndex(end, true))

    @Synchronized
    override fun insert(index: Int, charray: CharArray, offset: Int, length: Int): StringFBuffer {
        origin.insert(adjustIndex(index), charray, adjustIndex(offset, false, charray.size), length)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, obj: Any): StringFBuffer {
        origin.insert(adjustIndex(offset), obj)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: CharArray): StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: CharSequence):StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: CharSequence, start: Int, end: Int): StringFBuffer {
        origin.insert(
            adjustIndex(offset),
            value,
            adjustIndex(start, false, value.length),
            adjustIndex(end, true, value.length)
        )
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: Boolean): StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: Char): StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: Int): StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: Long): StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: Float): StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun insert(offset: Int, value: Double): StringFBuffer {
        origin.insert(adjustIndex(offset), value)
        return this
    }

    @Synchronized
    override fun indexOf(str: String): Int = origin.indexOf(str)

    @Synchronized
    override fun indexOf(str: String, index: Int): Int = origin.indexOf(str, adjustIndex(index))

    @Synchronized
    override fun indexOf(vararg characters: Char): Int = super.indexOf(*characters)

    @Synchronized
    override fun indexOf(index: Int, vararg characters: Char): Int = super.indexOf(index, *characters)

    @Synchronized
    override fun lastIndexOf(str: String): Int = origin.lastIndexOf(str)

    @Synchronized
    override fun lastIndexOf(str: String, index: Int): Int = origin.lastIndexOf(str, adjustIndex(index))

    @Synchronized
    override fun lastIndexOf(vararg characters: Char): Int = super.lastIndexOf(*characters)

    @Synchronized
    override fun lastIndexOf(index: Int, vararg characters: Char): Int = super.lastIndexOf(index, *characters)

    @Synchronized
    override fun reverse(): StringFBuffer {
        origin.reverse()
        return this
    }

    @Synchronized
    override fun toString(): String = origin.toString()

    @Synchronized
    override fun trimLeading(): StringFBuffer = super.trimLeading()

    @Synchronized
    override fun trimTrailing(): StringFBuffer = super.trimTrailing()

    @Synchronized
    override fun trim(): StringFBuffer = super.trim()

    @Synchronized
    override fun startsWith(totest: CharSequence): Boolean = super.startsWith(totest)

    @Synchronized
    override fun startsWith(casesensitive: Boolean, totest: CharSequence): Boolean = super.startsWith(casesensitive, totest)

    @Synchronized
    override fun <R : CharSequence?> startsWithMany(vararg candidates: R): R = super.startsWithMany<R>(*candidates)

    @Synchronized
    override fun <R : CharSequence?> startsWithMany(casesensitive: Boolean, vararg candidates: R): R = super.startsWithMany<R>(casesensitive, *candidates)

    @Synchronized
    override fun endsWith(totest: String): Boolean = super.endsWith(totest)

    @Synchronized
    override fun <R : CharSequence?> endsWithMany(vararg candidates: R): R = super.endsWithMany<R>(*candidates)

    @Synchronized
    override fun <R : CharSequence?> endsWithMany(casesensitive: Boolean, vararg candidates: R): R = super.endsWithMany<R>(casesensitive, *candidates)

    @Synchronized
    override fun endsWith(casesensitive: Boolean, totest: CharSequence): Boolean = super.endsWith(casesensitive, totest)

    @Synchronized
    override fun equals(totest: String): Boolean = super.equals(totest)

    @Synchronized
    override fun equals(casesensitive: Boolean, totest: String): Boolean = super.equals(casesensitive, totest)

    @Synchronized
    override fun remove(toremove: String): StringFBuffer = super.remove(toremove)

    @Synchronized
    override fun split(delimiters: String): Array<String> = super.split(delimiters)

    @Synchronized
    override fun splitRegex(regex: String): Array<String> = super.splitRegex(regex)

    @Synchronized
    override fun splitRegex(pattern: Pattern): Array<String> = super.splitRegex(pattern)

    @Synchronized
    override fun replace(from: Char, to: Char): StringFBuffer = super.replace(from, to)

    @Synchronized
    override fun replaceAll(regex: String, replacement: String): StringFBuffer = super.replaceAll(regex, replacement)

    @Synchronized
    override fun replaceAll(pattern: Pattern, replacement: String): StringFBuffer = super.replaceAll(pattern, replacement)

    @Synchronized
    override fun replaceFirst(regex: String, replacement: String): StringFBuffer = super.replaceFirst(regex, replacement)

    @Synchronized
    override fun replaceFirst(pattern: Pattern, replacement: String): StringFBuffer = super.replaceFirst(pattern, replacement)

    @Synchronized
    override fun replaceLast(regex: String, replacement: String): StringFBuffer = super.replaceLast(regex, replacement)

    @Synchronized
    override fun replaceLast(pattern: Pattern, replacement: String): StringFBuffer = super.replaceLast(pattern, replacement)

    @Synchronized
    override fun appendIfMissing(seq: CharSequence): StringFBuffer = super.appendIfMissing(seq)

    @Synchronized
    override fun appendIfMissing(ignoreCase: Boolean, seq: CharSequence): StringFBuffer = super.appendIfMissing(ignoreCase, seq)

    @Synchronized
    override fun prependIfMissing(seq: CharSequence): StringFBuffer = super.prependIfMissing(seq)

    @Synchronized
    override fun prependIfMissing(ignoreCase: Boolean, seq: CharSequence): StringFBuffer = super.prependIfMissing(ignoreCase, seq)

    @Synchronized
    override fun chars(): IntStream = origin.chars()

    @Synchronized
    override fun codePoints(): IntStream = origin.codePoints()

    @Synchronized
    override fun compareTo(other: StringFBuffer): Int =
        if (this === other) {
            0
        } else {
            origin.compareTo(other.origin)
        }

    @Synchronized
    override fun firstUp(): StringFBuffer = super.firstUp()

    @Synchronized
    override fun firstDown(): StringFBuffer = super.firstDown()

    @Synchronized
    override fun camelCase(): StringFBuffer = super.camelCase()

    @Synchronized
    override fun toLowerCase(): StringFBuffer = super.toLowerCase()

    @Synchronized
    override fun toUpperCase(): StringFBuffer = super.toUpperCase()

    @Synchronized
    override fun replaceAll(replacements: Map<String, String>): StringFBuffer = super.replaceAll(replacements)

    @Synchronized
    override fun replaceAll(replacements: Map<String, String>, fmt: String?): StringFBuffer = super.replaceAll(replacements, fmt)

    @Synchronized
    override fun replaceRegions(open: String, replacement: String): StringFBuffer = super.replaceRegions(open, replacement)

    @Synchronized
    override fun replaceRegions(open: String, close: String, replacement: String): StringFBuffer = super.replaceRegions(open, close, replacement)

    @Synchronized
    override fun replaceRegions(open: String, replacement: Function<String, CharSequence>): StringFBuffer = super.replaceRegions(open, replacement)

    @Synchronized
    override fun replaceRegions(open: String, close: String, replacement: Function<String, CharSequence>): StringFBuffer = super.replaceRegions(open, close, replacement)

    @Synchronized
    override fun appendFilling(count: @Min(1) Int, ch: Char): StringFBuffer = super.appendFilling(count, ch)

    @Synchronized
    @Throws(IOException::class)
    private fun writeObject(s: ObjectOutputStream) {
        s.writeObject(origin)
    }

    @Synchronized
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(s: ObjectInputStream) {
        origin = s.readObject() as StringBuffer
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
