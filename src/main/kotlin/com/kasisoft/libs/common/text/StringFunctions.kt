package com.kasisoft.libs.common.text

import com.kasisoft.libs.common.constants.*

import com.kasisoft.libs.common.pools.*

import javax.validation.constraints.*

import java.util.function.Function
import java.util.function.*

import java.util.*

import java.io.*

/**
 * Collection of functions used for String processing.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
object StringFunctions {

    /**
     * Returns the supplied string without it's prefix if there was any.
     *
     * @param name   The name which might contain a suffix.
     *
     * @return   The string without the suffix (the dot will removed as well).
     */
    @JvmStatic
    fun removeSuffix(name: String): String {
        val lidx = name.lastIndexOf('.')
        return if (lidx == -1) {
            name
        } else {
            name.substring(0, lidx)
        }
    }

    /**
     * Changes the suffix for the supplied name. If the name doesn't provide a suffix it will be appended.
     *
     * @param name     The name which might be altered.
     * @param suffix   The suffix which has to be added (without '.').
     *
     * @return   The name with the updated suffix.
     */
    @JvmStatic
    fun changeSuffix(name: String, suffix: String): String = String.format("%s.%s", removeSuffix(name), suffix)

    /**
     * Makes sure that the supplied String is either null or not empty. The text will be trimmed so there
     * won't be any whitespace at the beginning or the end.
     *
     * @param input   The String that has to be altered.
     *
     * @return   null or a non-empty String.
     */
    @JvmStatic
    fun cleanup(input: String?): String? {
        var result = input
        if (result != null) {
            result = result.trim { it <= ' ' }
            if (result.isEmpty()) {
                result = null
            }
        }
        return result
    }

    /**
     * Makes the first character upper case if there's one.
     *
     * @param input   The String where the first character has to be altered.
     *
     * @return   A possibly in-place altered input.
     */
    @JvmStatic
    fun firstUp(input: String): String = Buckets.stringFBuilder().forInstance { it.append(input).firstUp().toString() }

    /**
     * Makes the first character lower case if there's one.
     *
     * @param input   The CharSequence where the first character has to be altered.
     *
     * @return   A possibly in-place altered input.
     */
    @JvmStatic
    fun firstDown(input: String): String = Buckets.stringFBuilder().forInstance { it.append(input).firstDown().toString() }

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param input         The text that needs to be replaced.
     * @param search        The term that should be replaced.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    @JvmStatic
    fun replaceLiterallyAll(input: String, search: String, replacement: String): String = Buckets.stringFBuilder().forInstance { it.append(input).replaceLiterallyAll(search, replacement).toString() }

    /**
     * Performs a search & replace operation on the supplied input.
     *
     * @param input          The input which has to be modified.
     * @param replacements   A Map of String's used to run the search replace operation.
     *
     * @return   The modified String.
     */
    @JvmStatic
    fun replaceAll(input: String, replacements: Map<String, String?>): String = Buckets.stringFBuilder().forInstance { it.append(input).replaceAll(replacements).toString() }

    /**
     * Performs a search & replace operation on the supplied input.
     *
     * @param input          The input which has to be modified.
     * @param replacements   A Map of String's used to run the search replace operation.
     * @param fmt            A key formatter. Default: '%s' (alternativ: '${%s}' which means that keys will be ${fredo}, ${dodo}, ...)
     *
     * @return   The modified String.
     */
    @JvmStatic
    fun replaceAll(input: String, replacements: Map<String, String?>, fmt: String?): String = Buckets.stringFBuilder().forInstance { it.append(input).replaceAll(replacements, fmt).toString() }

    /**
     * Transforms the supplied value into a camelcase representation.
     *
     * @param input   The object which has to be changed.
     *
     * @return   The supplied sequence if possible. The content is altered to a camelcase variety.
     */
    @JvmStatic
    fun camelCase(input: String): String = Buckets.stringFBuilder().forInstance { it.append(input).camelCase().toString() }

    /**
     * Replaces regions within some text.
     *
     * @param input         The text which might be altered.
     * @param sep           The opening/closing of a region (f.e. "(*").
     * @param replacement   The replacement value.
     *
     * @return   The altered text.
     */
    @JvmStatic
    fun replaceRegions(input: String, sep: String, replacement: CharSequence): String = replaceRegions(input, sep, sep, replacement)

    /**
     * Replaces regions within some text.
     *
     * @param input         The text which might be altered.
     * @param open          The opening of a region (f.e. "(*").
     * @param close         The closing of a region (f.e. "*)").
     * @param replacement   The replacement value.
     *
     * @return   The altered text.
     */
    @JvmStatic
    fun replaceRegions(input: String, open: String, close: String?, replacement: CharSequence): String = replaceRegions(input, open, close) { replacement }

    /**
     * Replaces regions within some text.
     *
     * @param input         The text which might be altered.
     * @param open          The opening of a region (f.e. "(*").
     * @param close         The closing of a region (f.e. "*)").
     * @param replacement   The replacement value.
     *
     * @return   The altered text.
     */
    @JvmStatic
    fun replaceRegions(input: String, open: String, replacement: Function<String, CharSequence?>): String = replaceRegions(input, open, open, replacement)

    /**
     * Replaces regions within some text.
     *
     * @param input         The text which might be altered.
     * @param open          The opening of a region (f.e. "(*").
     * @param close         The closing of a region (f.e. "*)").
     * @param replacement   The replacement value.
     *
     * @return   The altered text.
     */
    @JvmStatic
    fun replaceRegions(input: String, open: String, close: String?, replacement: Function<String, CharSequence?>): String = Buckets.stringFBuilder().forInstance { it.append(input).replaceRegions(open, close, replacement).toString() }

    @JvmStatic
    fun <T : CharSequence> startsWithMany(input: String, vararg candidates: T): T? = startsWithMany(input, true, *candidates)

    @JvmStatic
    fun <T : CharSequence> startsWithMany(input: String, casesensitive: Boolean, vararg candidates: T): T? = Buckets.stringFBuilder().forInstance { it.append(input).startsWithMany<T>(casesensitive, *candidates) }

    @JvmStatic
    fun <T : CharSequence> endsWithMany(input: String, vararg candidates: T): T? = endsWithMany(input, true, *candidates)

    @JvmStatic
    fun <T : CharSequence> endsWithMany(input: String, casesensitive: Boolean, vararg candidates: T): T? = Buckets.stringFBuilder().forInstance { it.append(input).endsWithMany<T>(casesensitive, *candidates) }

    @JvmStatic
    fun trim(input: String, chars: String, left: Boolean?): String = Buckets.stringFBuilder().forInstance { it.append(input).trim(chars, left).toString() }

    /**
     * Creates a concatenation of the supplied Strings. This function allows elements to be null which means
     * that they're just be ignored.
     *
     * @param delimiter   A delimiter which might be used.
     * @param args        The list of Strings that has to be concatenated.
     *
     * @return   The concatenated String
     */
    @JvmStatic
    fun concatenate(delimiter: String?, vararg args: CharSequence?): String = concatenate(delimiter, Arrays.asList(*args))

    /**
     * Creates a concatenation of the supplied Strings. This function allows elements to be null which means
     * that they're just be ignored.
     *
     * @param delimiter   A delimiter which might be used.
     * @param args        The collection of Strings that has to be concatenated.
     *
     * @return   The concatenated String.
     */
    @JvmStatic
    fun <C : CharSequence?, L : Collection<C>?> concatenate(delimiter: String?, args: L): String {
        if (args == null || args.isEmpty()) {
            return Empty.NO_STRING
        }
        val del = delimiter ?: Empty.NO_STRING
        return Buckets.stringFBuilder().forInstance {
            val iterator = args.iterator()
            while (iterator.hasNext()) {
                val obj: C? = iterator.next()
                if (obj != null && obj.length > 0) {
                    it.append(del)
                    it.append(obj)
                }
            }
            if (it.length > 0 && del.length > 0) {
                it.delete(0, del.length)
            }
            it.toString()
        }
    }

    /**
     * Repeats the supplied text <param></param>n times.
     *
     * @param n      The number of concatenations that have to be performed.
     * @param text   The text that has to be repeated.
     *
     * @return   The concatenated reproduction string.
     */
    @JvmStatic
    fun repeat(n: @Min(0) Int, text: CharSequence?): String =
        if (n > 0 && text != null && text.length > 0) {
            Buckets.stringFBuilder().forInstance {
                var c = n
                while (c > 0) {
                    it.append(text)
                    c--
                }
                it.toString()
            }
        } else {
            Empty.NO_STRING
        }

    /**
     * Creates a textual presentation with a padding using the space character.
     *
     * @param text      The text that is supposed to be filled with padding.
     * @param limit     The maximum number of characters allowed.
     * @param left      `true` <=> Use left padding.
     *
     * @return   The text that is padded.
     */
    @JvmStatic
    fun padding(text: String?, limit: @Min(1) Int, left: Boolean): String = padding(text, limit, ' ', left)

    @JvmStatic
    fun fillString(count: Int, ch: Char): String = Buckets.stringFBuilder().forInstance { it.appendFilling(count, ch).toString() }

    /**
     * Creates a textual presentation with a padding.
     *
     * @param text      The text that is supposed to be filled with padding.
     * @param limit     The maximum number of characters allowed.
     * @param padding   The padding character.
     * @param left      `true` <=> Use left padding.
     *
     * @return   The text that is padded.
     */
    @JvmStatic
    fun padding(text: String?, limit: @Min(1) Int, padding: Char, left: Boolean): String {
        if (text == null) {
            return fillString(limit, padding)
        }
        return if (text.length >= limit) {
            text
        } else Buckets.stringFBuilder().forInstance {
            val diff = limit - text.length
            val padStr = fillString(diff, padding)
            if (left) {
                it.append(padStr)
            }
            it.append(text)
            if (!left) {
                it.append(padStr)
            }
            it.toString()
        }
    }

    /**
     * Returns a textual representation of the supplied object.
     *
     * @param obj    The object which textual representation is desired.
     *
     * @return   The textual representation of the supplied object.
     */
    @JvmStatic
    fun objectToString(obj: Any?): String =
        Buckets.stringFBuilder().forInstance {
            appendToString(it, obj)
            it.toString()
        }


    /**
     * Returns a textual representation of the supplied object.
     *
     * @param obj    The object which textual representation is desired.
     *
     * @return   The textual representation of the supplied object.
     */
    private fun <S : StringLike<*>> appendToString(receiver: S, obj: Any?) {
        if (obj == null) {
            receiver.append("null")
        } else if (obj is BooleanArray) {
            appendToStringBooleanArray<S>(receiver, obj)
        } else if (obj is CharArray) {
            appendToStringCharArray<S>(receiver, obj)
        } else if (obj is ByteArray) {
            appendToStringByteArray<S>(receiver, obj)
        } else if (obj is ShortArray) {
            appendToStringShortArray<S>(receiver, obj)
        } else if (obj is IntArray) {
            appendToStringIntArray<S>(receiver, obj)
        } else if (obj is LongArray) {
            appendToStringLongArray<S>(receiver, obj)
        } else if (obj is FloatArray) {
            appendToStringFloatArray<S>(receiver, obj)
        } else if (obj is DoubleArray) {
            appendToStringDoubleArray<S>(receiver, obj)
        } else if (obj.javaClass.isArray) {
            appendToStringObjectArray<S>(receiver, obj as Array<Any>)
        } else if (obj is Throwable) {
            appendToStringThrowable<S>(receiver, obj)
        } else {
            receiver.append(obj.toString())
        }
    }

    private fun <S : StringLike<*>> appendToStringThrowable(receiver: S, throwable: Throwable) {
        Buckets.stringWriter().forInstanceDo {
            PrintWriter(it).use { writer -> writer.println(throwable.toString()) }
            receiver.append(it.toString())
        }
    }

    private fun <S : StringLike<*>> appendToStringObjectArray(receiver: S, array: Array<Any>) {
        receiver.append('[')
        if (array.size > 0) {
            appendToString<S>(receiver, array[0])
            for (i in 1 until array.size) {
                receiver.append(',')
                appendToString<S>(receiver, array[i])
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringBooleanArray(receiver: S, array: BooleanArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append(array[0])
            for (i in 1 until array.size) {
                receiver.append(',').append(array[i])
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringCharArray(receiver: S, array: CharArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append('\'').append(array[0]).append('\'')
            for (i in 1 until array.size) {
                receiver.append(',').append('\'').append(array[i]).append('\'')
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringByteArray(receiver: S, array: ByteArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append("(byte)").append(array[0].toInt())
            for (i in 1 until array.size) {
                receiver.append(',').append("(byte)").append(array[i].toInt())
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringShortArray(receiver: S, array: ShortArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append("(short)").append(array[0].toInt())
            for (i in 1 until array.size) {
                receiver.append(',').append("(short)").append(array[i].toInt())
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringIntArray(receiver: S, array: IntArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append(array[0])
            for (i in 1 until array.size) {
                receiver.append(',').append(array[i])
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringLongArray(receiver: S, array: LongArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append(array[0]).append('l')
            for (i in 1 until array.size) {
                receiver.append(',').append(array[i]).append('l')
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringFloatArray(receiver: S, array: FloatArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append(array[0]).append('f')
            for (i in 1 until array.size) {
                receiver.append(',').append(array[i]).append('f')
            }
        }
        receiver.append(']')
    }

    private fun <S : StringLike<*>> appendToStringDoubleArray(receiver: S, array: DoubleArray) {
        receiver.append('[')
        if (array.size > 0) {
            receiver.append(array[0])
            for (i in 1 until array.size) {
                receiver.append(',').append(array[i])
            }
        }
        receiver.append(']')
    }

} /* ENDCLASS */
