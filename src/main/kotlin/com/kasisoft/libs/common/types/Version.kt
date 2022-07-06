package com.kasisoft.libs.common.types

import com.kasisoft.libs.common.text.*

import com.kasisoft.libs.common.*

/**
 * A simple descriptional datastructure for a version.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
data class Version(val major: Int, val minor: Int, val micro: Int?, val qualifier: String?): Comparable<Version> {

    val text: String

    companion object {

        @JvmStatic
        fun parse(version: String, hasMicro: Boolean? = null, hasQualifier: Boolean? = null): Version {

            /** @todo [07-JUL-2022:KASI]   Use Regex */

            try {

                var input  = StringFBuilder(version)
                var major  = Integer.parseInt(nextPart(input, '.'))
                var minor  = Integer.parseInt(nextPart(input, '.'))

                var micro: Int? = null
                var qualifier: String? = null

                if (hasMicro != null) {

                    if (hasMicro) {
                        micro  = Integer.valueOf(nextPart(input, '.', '_'))
                    }

                    if ((hasQualifier != null) && hasQualifier) {
                        qualifier = StringFunctions.cleanup(input.toString()) ?: throw KclException("missing qualifier")
                    }

                } else {

                    // this is our flexible approach where we're trying to match as much as possible
                    var part = nextPart(input, '.', '_')
                    try {
                        micro     = Integer.valueOf(part)
                        qualifier = StringFunctions.cleanup(input.toString())
                    } catch (ex: NumberFormatException) {
                        // not a valid number so it's obviously the qualifier
                        qualifier = part
                    }

                }

                // text = toText();
                return Version(major, minor, micro, qualifier)

            } catch (ex: Exception) {
                throw KclException.wrap(ex, "cannot parse version '$version'")
            }

        }

        private fun nextPart(input: StringFBuilder, vararg characters: Char): String? {
            var result: String? = null
            var pos             = input.indexOf(*characters);
            if (pos == -1) {
                result = input.toString();
                input.setLength(0);
            } else {
                result = input.substring(0, pos);
                input.delete(0, pos + 1);
            }
            return StringFunctions.cleanup(result);
        }

    } /* ENDOBJECT */

    init {
        text = toText()
    }

    /**
     * Creates a textual presentation of this version.
     *
     * @param qualifierdelim   The delimiter which has to be used for the qualifier (sometimes you might wann use '_').
     *
     * @return   A textual presentation of this version.
     */
    fun toText(qualifierdelim: Char = '.'): String {
        val result = StringBuilder()
        result.append(major)
        result.append('.')
        result.append(minor)
        if (micro != null) {
            result.append('.')
            result.append(micro)
        }
        if (qualifier != null) {
            result.append(qualifierdelim)
            result.append(qualifier)
        }
        return result.toString()
    }

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = text

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Version
        return text == other.text
    }

    override fun compareTo(other: Version): Int {
        var result = major.compareTo(other.major)
        if (result == 0) {
            result = minor.compareTo(other.minor)
        }
        if (result == 0) {
            if ((micro != null) && (other.micro != null)) {
                result = micro.compareTo(other.micro)
            } else if ((micro != null) || (other.micro != null)) {
                result = if (micro != null) -1 else 1
            }
        }
        if (result == 0) {
            result = text.compareTo(other.text)
        }
        return result
    }

} /* ENDCLASS */
