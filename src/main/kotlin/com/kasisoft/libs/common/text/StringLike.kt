package com.kasisoft.libs.common.text

import com.kasisoft.libs.common.pools.*;

import javax.validation.constraints.*;

import java.util.function.Function;
import java.util.function.*;
import java.util.*;

import java.util.regex.Pattern;
import java.util.regex.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
interface StringLike<T: StringLike<T>> : CharSequence, Comparable<T> {

    /**
     * @see StringBuilder.capacity
     */
    fun capacity(): @Min(1) Int

    /**
     * @see StringBuilder.ensureCapacity
     */
    fun ensureCapacity(minimum: @Min(1) Int): T

    /**
     * @see StringBuilder.trimToSize
     */
    fun trimToSize(): T

    /**
     * @see StringBuilder.setLength
     */
    fun setLength(newlength: @Min(0) Int): T

    /**
     * @see StringBuilder.codePointAt
     */
    fun codePointAt(index: Int): Int

    /**
     * @see StringBuilder.codePointBefore
     */
    fun codePointBefore(index: Int): Int

    /**
     * @see StringBuilder.codePointCount
     */
    fun codePointCount(begin: Int, end: Int): Int

    /**
     * @see StringBuilder.offsetByCodePoints
     */
    fun offsetByCodePoints(index: Int, codepointoffset: Int): Int

    /**
     * @see StringBuilder.getChars
     */
    fun getChars(start: Int, end: Int, destination: CharArray, destbegin: Int): T

    /**
     * @see StringBuilder.setCharAt
     */
    fun setCharAt(index: Int, ch: Char): T
    fun setCodepointAt(index: Int, codepoint: Int): T

    /**
     * @see StringBuilder.append
     */
    fun append(obj: Any): T

    /**
     * @see StringBuilder.append
     */
    fun append(sequence: CharSequence): T

    /**
     * @see StringBuilder.append
     */
    fun append(sequence: CharSequence, start: Int, end: Int): T

    /**
     * @see StringBuilder.append
     */
    fun append(charray: CharArray): T

    /**
     * @see StringBuilder.append
     */
    fun append(charray: CharArray, offset: Int, length: Int): T

    /**
     * @see StringBuilder.append
     */
    fun append(value: Boolean): T

    /**
     * @see StringBuilder.append
     */
    fun append(value: Char): T

    /**
     * @see StringBuilder.append
     */
    fun append(value: Int): T

    /**
     * Appends some values using a specific format pattern.
     *
     * @param format   The pattern to use.
     * @param args     The arguments for this pattern.
     *
     * @return   The current buffer.
     */
    fun appendF(fmt: String, vararg args: Any?): T {
        var toAdd = fmt
        if (args.isNotEmpty()) {
            toAdd = String.format(fmt, *args)
        }
        return append(toAdd)
    }

    /**
     * @see StringBuilder.appendCodePoint
     */
    fun appendCodePoint(codepoint: Int): T

    /**
     * @see StringBuilder.append
     */
    fun append(value: Long): T

    /**
     * @see StringBuilder.append
     */
    fun append(value: Float): T

    /**
     * @see StringBuilder.append
     */
    fun append(value: Double): T

    /**
     * @see StringBuilder.delete
     */
    fun delete(start: Int, end: Int): T

    /**
     * @see StringBuilder.deleteCharAt
     */
    fun deleteCharAt(index: Int): T

    /**
     * @see StringBuilder.replace
     */
    fun replace(start: Int, end: Int, str: String): T

    /**
     * @see StringBuilder.substring
     */
    fun substring(start: Int): String

    /**
     * @see StringBuilder.substring
     */
    fun substring(start: Int, end: Int): String

    /**
     * @see StringBuilder.insert
     */
    fun insert(index: Int, charray: CharArray, offset: Int, length: Int): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, obj: Any): T

    /**
     * Inserts some values using a specific format pattern.
     *
     * @param offset   The location where to insert the formatted content.
     * @param fmt      The pattern to use.
     * @param args     The arguments for this pattern.
     *
     * @return   The current buffer.
     */
    fun insertF(offset: Int, fmt: String, vararg args: Any): T {
        var toAdd = fmt
        if (args.isNotEmpty()) {
            toAdd = String.format(fmt, *args)
        }
        return insert(offset, toAdd)
    }

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: CharArray): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: CharSequence): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: CharSequence, start: Int, end: Int): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: Boolean): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: Char): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: Int): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: Long): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: Float): T

    /**
     * @see StringBuilder.insert
     */
    fun insert(offset: Int, value: Double): T

    /**
     * @see StringBuilder.indexOf
     */
    fun indexOf(str: String): Int

    /**
     * @see StringBuilder.indexOf
     */
    fun indexOf(str: String, index: Int): Int

    fun indexOf(vararg characters: Char): Int = indexOf(0, *characters)

    fun indexOf(index: Int, vararg characters: Char): Int {
        var result = -1
        if (characters.isNotEmpty()) {
            val str = toString()
            for (ch in characters) {
                val idx = str.indexOf(ch, index)
                if (idx != -1) {
                    result = if (result == -1) {
                        idx
                    } else {
                        Math.min(idx, result)
                    }
                }
                if (result == 0) {
                    break
                }
            }
        }
        return result
    }

    /**
     * Like [StringBuilder.indexOf] with the difference that this function provides the position of the
     * leftmost literal which could be found.
     *
     * @param literals   A list of literals that will be checked.
     *
     * @return   The index of the leftmost found literal or -1 if none matched.
     */
    fun indexOf(vararg literals: String): Int = indexOf(0, *literals)

    /**
     * Like [StringBuilder.indexOf] with the difference that this function provides the position of the
     * leftmost literal which could be found.
     *
     * @param index      The index used as the starting point for the lookup.
     * @param literals   A list of literals that will be checked.
     *
     * @return   The index of the leftmost found literal or -1 if none matched.
     */
    fun indexOf(index: Int, vararg literals: String): Int {
        var result = -1
        for (literal in literals) {
            val pos = indexOf(literal, index)
            if (pos != -1) {
                result = if (result == -1) {
                    pos
                } else {
                    Math.min(result, pos)
                }
            }
        }
        return result
    }

    /**
     * @see StringBuilder.lastIndexOf
     */
    fun lastIndexOf(str: String): Int

    /**
     * @see StringBuilder.lastIndexOf
     */
    fun lastIndexOf(str: String, index: Int): Int

    fun lastIndexOf(vararg characters: Char): Int = lastIndexOf(length, *characters)

    fun lastIndexOf(index: Int, vararg characters: Char): Int {
        var result = -1
        if (characters.isNotEmpty()) {
            val str = toString()
            for (ch in characters) {
                val idx = str.lastIndexOf(ch, index)
                if (idx != -1) {
                    result = if (result == -1) {
                        idx
                    } else {
                        Math.max(idx, result)
                    }
                }
            }
        }
        return result
    }

    /**
     * Like [StringBuilder.lastIndexOf] with the difference that this function provides the position of
     * the rightmost literal which could be found.
     *
     * @param literals   A list of literals that will be checked.
     *
     * @return   The index of the rightmost found literal or -1 if none matched.
     */
    fun lastIndexOf(vararg literals: String): Int = lastIndexOf(-1, *literals)

    /**
     * Like [StringBuilder.lastIndexOf] with the difference that this function provides the position of
     * the rightmost literal which could be found.
     *
     * @param index      The index used as the starting point for the lookup.
     * @param literals   A list of literals that will be checked.
     *
     * @return   The index of the rightmost found literal or -1 if none matched.
     */
    fun lastIndexOf(index: Int, vararg literals: String): Int {
        var result = -1
        for (literal in literals) {
            val pos = lastIndexOf(literal, index)
            if (pos != -1) {
                result = if (result == -1) {
                    pos
                } else {
                    Math.max(result, pos)
                }
            }
        }
        return result
    }

    /**
     * @see StringBuilder.reverse
     */
    fun reverse(): T

    /**
     * This function removes leading whitespace from this buffer.
     */
    fun trimLeading(): T {
        while (length > 0) {
            val codePoint = codePointAt(0)
            if (!Character.isWhitespace(codePoint)) {
                break
            }
            val charCount = Character.charCount(codePoint)
            delete(0, charCount)
        }
        return this as T
    }

    /**
     * This function removes trailing whitespace from this buffer.
     */
    fun trimTrailing(): T {
        while (length > 0) {
            val len = length
            val codePoint = codePointAt(len - 1)
            if (!Character.isWhitespace(codePoint)) {
                break
            }
            val charCount = Character.charCount(codePoint)
            delete(len - charCount, len)
        }
        return this as T
    }

    /**
     * This function removes leading and trailing whitespace from this buffer.
     */
    fun trim(): T = trimLeading().trimTrailing()

    /**
     * This function removes leading whitespace from this buffer.
     *
     * @param chars   The whitespace characters.
     */
    fun trimLeading(chars: String): T {
        while (length > 0) {
            val ch = get(0)
            if (chars.indexOf(ch) == -1) {
                break
            }
            deleteCharAt(0)
        }
        return this as T
    }

    /**
     * This function removes trailing whitespace from this buffer.
     *
     * @param chars   The whitespace characters.
     */
    fun trimTrailing(chars: String): T {
        while (length > 0) {
            val length = length
            val ch = get(length - 1)
            if (chars.indexOf(ch) == -1) {
                break
            }
            deleteCharAt(length - 1)
        }
        return this as T
    }

    /**
     * Trims this instance depending on the provided settings.
     *
     * @param chars   The whitespace characters.
     * @param left    `null` <=> Trim left and right,
     * `true` <=> Trim left,
     * `false` <=> Trim right.
     */
    fun trim(chars: String, left: Boolean?): T {
        if (left == null || left) {
            trimLeading(chars)
        }
        if (left == null || !left) {
            trimTrailing(chars)
        }
        return this as T
    }

    /**
     * Returns `true` if the content of this buffer starts with the supplied literal.
     *
     * @param totest   The text used for the comparison.
     *
     * @return   `true` <=> The literal starts with the supplied literal.
     */
    fun startsWith(totest: CharSequence): Boolean = startsWith(true, totest)

    /**
     * Returns `true` if the content of this buffer starts with the supplied literal.
     *
     * @param casesensitive   `true` <=> Performs a case sensitive comparison.
     * @param totest          The text used for the comparison.
     *
     * @return   `true` <=> The literal starts with the supplied literal.
     */
    fun startsWith(casesensitive: Boolean, seq: CharSequence): Boolean {
        if (seq.length > length) {
            return false
        }
        val totest = seq.toString()
        val part   = substring(0, totest.length)
        return if (casesensitive) {
            part == totest
        } else {
            part.equals(totest, ignoreCase = true)
        }
    }

    /**
     * Returns `true` if the content of this buffer ends with the supplied literal.
     *
     * @param casesensitive   `true` <=> Performs a case sensitive comparison.
     * @param candidates      The candidates to be tested at the end.
     *
     * @return   The sequence that's at the start or null.
     */
    fun <R: CharSequence> startsWithMany(vararg candidates: R): R? = startsWithMany(true, *candidates)

    /**
     * Returns `true` if the content of this buffer ends with the supplied literal.
     *
     * @param casesensitive   `true` <=> Performs a case sensitive comparison.
     * @param candidates      The candidates to be tested at the end.
     *
     * @return   The sequence that's at start or null.
     */
    fun <R: CharSequence> startsWithMany(casesensitive: Boolean, vararg candidates: R): R? {
        for (seq in candidates) {
            if (startsWith(casesensitive, seq)) {
                return seq
            }
        }
        return null
    }

    /**
     * Returns `true` if the content of this buffer ends with the supplied literal.
     *
     * @param totest   The text used for the comparison.
     *
     * @return   `true` <=> The literal ends with the supplied literal.
     */
    fun endsWith(totest: String): Boolean = endsWith(true, totest)

    /**
     * Returns `true` if the content of this buffer ends with the supplied literal.
     *
     * @param casesensitive   `true` <=> Performs a case sensitive comparison.
     * @param seq             The text used for the comparison.
     *
     * @return   `true` <=> The literal ends with the supplied literal.
     */
    fun endsWith(casesensitive: Boolean, seq: CharSequence): Boolean {
        if (seq.length > length) {
            return false
        }
        val totest = seq.toString()
        val part = substring(length - seq.length)
        return if (casesensitive) {
            part == totest
        } else {
            part.equals(totest, ignoreCase = true)
        }
    }

    /**
     * Returns `true` if the content of this buffer ends with the supplied literal.
     *
     * @param casesensitive   `true` <=> Performs a case sensitive comparison.
     * @param candidates      The candidates to be tested at the end.
     *
     * @return   The sequence that's at the end or null.
     */
    fun <R: CharSequence> endsWithMany(vararg candidates: R): R? = endsWithMany(true, *candidates)

    /**
     * Returns `true` if the content of this buffer ends with the supplied literal.
     *
     * @param casesensitive   `true` <=> Performs a case sensitive comparison.
     * @param candidates      The candidates to be tested at the end.
     *
     * @return   The sequence that's at the end or null.
     */
    fun <R: CharSequence> endsWithMany(casesensitive: Boolean, vararg candidates: R): R? {
        for (seq in candidates) {
            if (endsWith(casesensitive, seq)) {
                return seq
            }
        }
        return null
    }

    /**
     * Returns `true` if the content of this buffer equals the supplied literal.
     *
     * @param totest   The text used for the comparison.
     *
     * @return   `true` <=> The literal is equal.
     */
    fun equals(totest: String): Boolean = equals(true, totest)

    /**
     * Returns `true` if the content of this buffer equals the supplied literal.
     *
     * @param casesensitive   `true` <=> Performs a case sensitive comparison.
     * @param totest          The text used for the comparison.
     *
     * @return   `true` <=> The literal is equal.
     */
    fun equals(casesensitive: Boolean, totest: String): Boolean =
        if (casesensitive) {
            toString() == totest
        } else {
            toString().equals(totest, ignoreCase = true)
        }

    /**
     * Removes a collection of characters from this buffer.
     *
     * @param toremove   A list of characters which have to be removed.
     *
     * @return   The altered input.
     */
    fun remove(toremove: String): T {
        for (i in length - 1 downTo 0) {
            if (toremove.indexOf(get(i)) != -1) {
                deleteCharAt(i)
            }
        }
        return this as T
    }

    /**
     * Returns a splitted representation of this buffer except the delimiting characters. In difference to the function
     * [String.split] this one doesn't use a regular expression.
     *
     * @param delimiters   A list of characters providing the delimiters for the splitting.
     *
     * @return   A splitted list without the delimiting character.
     */
    fun split(delimiters: String): Array<String> {
        return Buckets.arrayList<String>().forInstance(Function<ArrayList<String>, Array<String>> {
            val tokenizer = StringTokenizer(toString(), delimiters, false)
            while (tokenizer.hasMoreTokens()) {
                it.add(tokenizer.nextToken())
            }
            it.toTypedArray()
        })
    }

    /**
     * Like [.split] with the difference that this function accepts a regular expression for the splitting.
     *
     * @param regex   A regular expression used for the splitting.
     *
     * @return   A splitted list without fragments matching the supplied regular expression.
     */
    fun splitRegex(regex: String): Array<String> = splitRegex(Pattern.compile(regex))

    /**
     * Like [.split] with the difference that this function accepts a regular expression for the splitting.
     *
     * @param pattern   A pattern providing the regular expression used for the splitting.
     *
     * @return   A splitted list without fragments matching the supplied regular expression.
     */
    fun splitRegex(pattern: Pattern): Array<String> {
        return Buckets.arrayList<String>().forInstance {
            val matcher = pattern.matcher(this)
            var last = 0
            var match = false
            while (matcher.find()) {
                match = true
                if (matcher.start() > last) {
                    it.add(substring(last, matcher.start()))
                }
                last = matcher.end()
            }
            if (match && last < length) {
                it.add(substring(last))
            }
            if (!match) {
                // there was not at least one match
                it.add(toString())
            }
            it.toTypedArray()
        }
    }

    /**
     * @see String.replace
     * @param from   The character which has to be replaced.
     * @param to     The character which has to be used instead.
     *
     * @return   This buffer without `from` characters.
     */
    fun replace(from: Char, to: Char): T {
        for (i in 0 until length) {
            if (get(i) == from) {
                setCharAt(i, to)
            }
        }
        return this as T
    }

    /**
     * @see String.replace
     * @param fromCodepoint   The codepoint which has to be replaced.
     * @param toCodepoint     The codepoint which has to be used instead.
     *
     * @return   This buffer without `from` characters.
     */
    fun replace(fromCodepoint: Int, toCodepoint: Int): T {
        for (i in 0 until length) {
            if (codePointAt(i) == fromCodepoint) {
                setCodepointAt(i, toCodepoint)
            }
        }
        return this as T
    }

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param regex         The regular expression used to select the fragments that will be replaced.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceLiterallyAll(search: String, replacement: String): T {
        Buckets.arrayList<Int>().forInstanceDo {
            var pos = indexOf(search)
            while (pos != -1) {
                it.add(pos)
                it.add(pos + search.length)
                pos = indexOf(pos + search.length, search)
            }
            var i = it.size - 2
            while (i >= 0) {
                val start = it[i]
                val end = it[i + 1]
                delete(start, end)
                insert(start, replacement)
                i -= 2
            }
        }
        return this as T
    }

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param regex         The regular expression used to select the fragments that will be replaced.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceAll(regex: String, replacement: String): T = replaceAll(Pattern.compile(regex), replacement)

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param pattern       The Pattern providing the regular expression for the substitution.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceAll(pattern: Pattern, replacement: String): T {
        Buckets.arrayList<Int>().forInstanceDo {
            val matcher = pattern.matcher(this)
            while (matcher.find()) {
                it.add(matcher.start())
                it.add(matcher.end())
            }
            var i = it.size - 2
            while (i >= 0) {
                val start = it[i]
                val end = it[i + 1]
                delete(start, end)
                insert(start, replacement)
                i -= 2
            }
        }
        return this as T
    }

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param regex                 The regular expression used to select the fragments that will be replaced.
     * @param replacementSupplier   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceAll(regex: String, replacementSupplier: Function<String, String?>): T = replaceAll(Pattern.compile(regex), replacementSupplier)

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param pattern               The Pattern providing the regular expression for the substitution.
     * @param replacementSupplier   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceAll(pattern: Pattern, replacementSupplier: Function<String, String?>): T {
        val ranges = Buckets.arrayList<Int>().allocate()
        val substitutions = Buckets.arrayList<String?>().allocate()
        val matcher = pattern.matcher(this)

        // we're collecting the substitutions from left to right, so the supplying function can assume this and use an
        // internal state if desired
        while (matcher.find()) {
            ranges.add(matcher.start())
            ranges.add(matcher.end())
            val text = substring(matcher.start(), matcher.end())
            substitutions.add(replacementSupplier.apply(text))
        }

        // perform the substitutions
        var i = ranges.size - 2
        var j = substitutions.size - 1
        while (i >= 0) {
            val start = ranges[i]
            val end = ranges[i + 1]
            delete(start, end)
            if (substitutions[j] != null) {
                insert(start, substitutions[j] as String)
            }
            i -= 2
            j--
        }
        Buckets.arrayList<String?>().free(substitutions)
        Buckets.arrayList<Int>().free(ranges)
        return this as T
    }

    /**
     * Replaces all occurrences of the supplied keys with the corresponding values.
     *
     * @param replacements   The substitution map.
     *
     * @return   This buffer.
     */
    fun replaceAll(replacements: Map<String, String?>): T = replaceAll(replacements, null)

    /**
     * Replaces all occurrences of a regular expression with a specified replacement.
     *
     * @param replacements   The substitution map.
     * @param fmt            A formatting string to tweak the key literal. Default is '%s' (an alternative example: '${%s}')
     *
     * @return   This buffer.
     */
    fun replaceAll(replacements: Map<String, String?>, fmt: String?): T {

        val substitutions = Buckets.hashMap<String, String?>().allocate()
        val builder = Buckets.stringFBuilder().allocate()
        val regions = Buckets.arrayList<Int>().allocate()

        // setup the substitution map
        if (fmt != null && "%s" != fmt) {
            replacements.forEach { (k: String, v: String?) -> substitutions[String.format(fmt, k)] = v }
        } else {
            substitutions.putAll(replacements)
        }

        // build a big OR of all keys
        substitutions.keys.forEach(Consumer { builder.append('|').append(Pattern.quote(it)) })
        builder.setCharAt(0, '(')
        builder.append(')')

        // collect regions of matches
        val pattern = Pattern.compile(builder.toString())
        val matcher = pattern.matcher(this)
        while (matcher.find()) {
            regions.add(matcher.start())
            regions.add(matcher.end())
        }

        // substitute matches
        var i = regions.size - 2
        while (i >= 0) {
            val start = regions[i]
            val end = regions[i + 1]
            val key = substring(start, end)
            delete(start, end)
            if (substitutions[key] != null) {
                insert(start, substitutions[key] as String)
            }
            i -= 2
        }
        Buckets.hashMap<String, String?>().free(substitutions)
        Buckets.stringFBuilder().free(builder)
        Buckets.arrayList<Int>().free(regions)
        return this as T
    }

    /**
     * Like [.replaceAll] but only the first occurrence of the regular expression will be replaced.
     *
     * @param regex         The regular expression used to select the fragments that will be replaced.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceFirst(regex: String, replacement: String): T = replaceFirst(Pattern.compile(regex), replacement)

    /**
     * Like [.replaceAll] but only the first occurrence of the regular expression will be replaced.
     *
     * @param pattern       The Pattern providing the regular expression for the substitution.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceFirst(pattern: Pattern, replacement: String): T {
        val matcher = pattern.matcher(this)
        if (matcher.find()) {
            delete(matcher.start(), matcher.end())
            insert(matcher.start(), replacement)
        }
        return this as T
    }

    /**
     * Like [.replaceAll] but only the last occurrence of the regular expression will be replaced.
     *
     * @param regex         The regular expression used to select the fragments that will be replaced.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceLast(regex: String, replacement: String): T = replaceLast(Pattern.compile(regex), replacement)

    /**
     * Like [.replaceAll] but only the last occurrence of the regular expression will be replaced.
     *
     * @param pattern       The Pattern providing the regular expression for the substitution.
     * @param replacement   The replacement which has to be used instead.
     *
     * @return   This buffer.
     */
    fun replaceLast(pattern: Pattern, replacement: String): T {
        val matcher = pattern.matcher(this)
        var start = -1
        var end = -1
        while (matcher.find()) {
            start = matcher.start()
            end = matcher.end()
        }
        if (start != -1) {
            delete(start, end)
            insert(start, replacement)
        }
        return this as T
    }

    fun firstUp(): T {
        val len = length
        if (len > 0) {
            val first = get(0)
            val upper = first.uppercaseChar()
            if (first != upper) {
                setCharAt(0, upper)
            }
        }
        return this as T
    }

    fun firstDown(): T {
        val len = length
        if (len > 0) {
            val first = get(0)
            val lower = first.lowercaseChar()
            if (first != lower) {
                setCharAt(0, lower)
            }
        }
        return this as T
    }

    /**
     * Creates a camelcase representation of the supplied sequence.
     *
     * @return   The camelcase representation.
     */
    fun camelCase(): T {
        val len = length
        var i = len - 2
        var j = len - 1
        while (i >= 0) {
            val current = get(i)
            val next = get(j)
            if (!Character.isLetter(current)) {
                setCharAt(j, next.uppercaseChar())
                deleteCharAt(i)
            }
            i--
            j--
        }
        if (length > 0) {
            firstDown()
        }
        return this as T
    }

    fun toLowerCase(): T {
        val lower = toString().lowercase(Locale.getDefault())
        setLength(0)
        append(lower)
        return this as T
    }

    fun toUpperCase(): T {
        val upper = toString().uppercase(Locale.getDefault())
        setLength(0)
        append(upper)
        return this as T
    }

    fun appendIfMissing(seq: CharSequence): T = appendIfMissing(true, seq)

    fun appendIfMissing(casesensitive: Boolean, seq: CharSequence): T {
        if (!endsWith(casesensitive, seq)) {
            append(seq)
        }
        return this as T
    }

    fun prependIfMissing(seq: CharSequence): T = prependIfMissing(true, seq)

    fun prependIfMissing(casesensitive: Boolean, seq: CharSequence): T {
        if (!startsWith(casesensitive, seq)) {
            insert(0, seq)
        }
        return this as T
    }

    fun removeEnd(seq: CharSequence): T = removeEnd(true, seq)

    fun removeEnd(casesensitive: Boolean, seq: CharSequence): T {
        if (endsWith(casesensitive, seq)) {
            delete(-seq.length, 0)
        }
        return this as T
    }

    fun removeStart(seq: CharSequence): T = removeStart(true, seq)

    fun removeStart(casesensitive: Boolean, seq: CharSequence): T {
        if (startsWith(casesensitive, seq)) {
            delete(0, seq.length)
        }
        return this as T
    }

    fun replaceRegions(open: String, replacement: String): T = replaceRegions(open, open) { replacement }

    fun replaceRegions(open: String, close: String, replacement: String): T = replaceRegions(open, close) { replacement }

    fun replaceRegions(open: String, replacement: Function<String, CharSequence?>): T = replaceRegions(open, open, replacement)

    fun replaceRegions(open: String, close: String?, replacement: Function<String, CharSequence?>): T {
        var close = close
        if (close == null) {
            close = open
        }
        var start = 0
        var idxOpen = indexOf(open, start)
        var idxClose = indexOf(close, idxOpen + 1)
        while (idxOpen != -1 && idxClose != -1) {
            val inner = substring(idxOpen + open.length, idxClose)
            val value = replacement.apply(inner)
            delete(idxOpen, idxClose + close.length)
            start = if (value != null) {
                insert(idxOpen, value)
                idxOpen + value.length
            } else {
                idxOpen
            }
            idxOpen = indexOf(open, start)
            idxClose = indexOf(close, idxOpen + 1)
        }
        return this as T
    }

    fun appendFilling(count: @Min(1) Int, ch: Char): T {
        val charray = CharArray(count)
        Arrays.fill(charray, ch)
        return append(charray)
    }

} /* ENDINTERFACE */
