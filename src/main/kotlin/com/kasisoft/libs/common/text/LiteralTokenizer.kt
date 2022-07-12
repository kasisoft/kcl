package com.kasisoft.libs.common.text

import javax.validation.constraints.*

import java.util.*

/**
 * This tokenizer operates similar to the well known StringTokenizer class with the distinction that a complete literal
 * can be used for the tokenization process.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
class LiteralTokenizer(private val input: StringLike<*>, returnLiterals: Boolean, vararg delimiters: String): Enumeration<String?> {

    private var pos: Int
    private val literals: Array<String>
    private val doreturn: Boolean
    private var next: String?

    /**
     * Prepares this tokenizer to operate using delimiting literals.
     *
     * @param data             The String content that has to be tokenized.
     * @param delimiters       A list of delimiting literals.
     */
    constructor(data: String, vararg delimiters: String) : this(data, false, *delimiters) {}

    /**
     * Prepares this tokenizer to operate using delimiting literals.
     *
     * @param data             The String content that has to be tokenized.
     * @param delimiters       A list of delimiting literals.
     */
    constructor(data: StringLike<*>, vararg delimiters: String) : this(StringFBuilder(data), false, *delimiters) {}

    /**
     * Prepares this tokenizer to operate using delimiting literals.
     *
     * @param data             The String content that has to be tokenized.
     * @param returnliterals   `true` <=> Return delimiting literals as well.
     * @param delimiters       A list of delimiting literals.
     */
    constructor(data: String, returnLiterals: Boolean, vararg delimiters: String) : this(StringFBuilder(data), returnLiterals, *delimiters) {}

    /**
     * Prepares this tokenizer to operate using delimiting literals.
     *
     * @param data             The String content that has to be tokenized.
     * @param returnLiterals   `true` <=> Return delimiting literals as well.
     * @param delimiters       A list of delimiting literals.
     */
    init {
        literals = arrayOf(*delimiters)
        doreturn = returnLiterals
        pos      = 0
        next     = getNext()
    }

    override fun hasMoreElements(): Boolean = next != null

    override fun nextElement(): String? {
        val currentNext: String? = next
        if (currentNext == null) {
            return null
        }
        val result: String = currentNext
        next = getNext()
        return result
    }

    /**
     * Returns the next literal that has to be returned by this tokenizer.
     *
     * @return   The next literal that has to be returned by this tokenizer.
     */
    private fun getNext(): String? {
        if (pos == -1) {
            // there's no more content
            return null
        }
        val firstdelimiter = firstDelimiter()
        val oldpos = pos
        if (firstdelimiter == null) {
            // there are no longer delimiting literals, so the rest becomes the next value
            pos = -1
            return input.substring(oldpos)
        }
        var newpos = input.indexOf(firstdelimiter, pos)
        return if (newpos == pos) {
            // we're directly pointing to a delimiter
            if (doreturn) {
                // the user wants to get the delimiting literal
                newpos = pos + firstdelimiter.length
                pos = newpos
                if (pos >= input.length) {
                    pos = -1
                }
                input.substring(oldpos, newpos)
            } else {
                // the user wants to skip delimiting literals, so try to get the next literal after the delimiter
                pos = pos + firstdelimiter.length
                if (pos >= input.length) {
                    pos = -1
                }
                getNext()
            }
        } else {
            pos = newpos
            input.substring(oldpos, newpos)
        }
    }

    /**
     * Returns the delimiting literal that will be detected first.
     *
     * @return   The delimiting literal that will be detected first.
     */
    private fun firstDelimiter(): String? {
        var result: String? = null
        var next = Int.MAX_VALUE
        for (literal in literals) {
            val newnext = input.indexOf(literal, pos)
            if (newnext < next && newnext >= pos) {
                next = newnext
                result = literal
            }
        }
        return result
    }

} /* ENDCLASS */
