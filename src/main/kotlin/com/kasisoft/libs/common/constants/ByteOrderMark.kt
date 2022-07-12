package com.kasisoft.libs.common.constants

import com.kasisoft.libs.common.utils.*
import javax.validation.constraints.*
import java.util.*

/**
 * Constants the different byte order marks.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
enum class ByteOrderMark(val bom: ByteArray) {

    UTF8(byteArrayOf(0xef.toByte(), 0xbb.toByte(), 0xbf.toByte())),

    UTF16BE(byteArrayOf(0xfe.toByte(), 0xff.toByte())),

    UTF16LE(byteArrayOf(0xff.toByte(), 0xfe.toByte())),

    UTF32BE(byteArrayOf(0x00.toByte(), 0x00.toByte(), 0xfe.toByte(), 0xff.toByte())),

    UTF32LE(byteArrayOf(0xff.toByte(), 0xfe.toByte(), 0x00.toByte(), 0x00.toByte()));

    /**
     * Returns <code>true</code> if the supplied data starts with this BOM.
     *
     * @param data The data to be tested.
     * @param offset An offset where the start has to begin. Must be a positive number.
     *
     * @return <code>true</code> <=> The supplied data starts with this BOM.
     */
    fun startsWith(data: ByteArray): Boolean = PrimitiveFunctions.compare(data, bom)

    companion object {

        /**
         * Returns the ByteOrderMark located at a specific location of the supplied data.
         *
         * @param data The data to be tested.
         * @param offset The location where to start the test. Must be positive.
         *
         * @return The ByteOrderMark if it could be identified.
         */
        @JvmStatic
        fun identify(data: ByteArray): ByteOrderMark? {
            for (it in ByteOrderMark.values()) {
                if (it.startsWith(data)) {
                    return it;
                }
            }
            return null;
        }

    } /* ENDOBJECT */

} /* ENDENUM */
