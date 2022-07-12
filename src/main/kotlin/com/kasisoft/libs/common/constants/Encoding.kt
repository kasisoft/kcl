package com.kasisoft.libs.common.constants

import com.kasisoft.libs.common.annotation.*

import java.nio.charset.*
import java.nio.*

/**
 * Collection of supported encodings.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Specification(value = "https://docs.oracle.com/javase/10/docs/api/java/nio/charset/Charset.html", date = "04-JUN-2020")
class Encoding(val encoding : String, val bomRequired : Boolean, val byteOrderMark : ByteOrderMark?) {

    val charset : Charset = Charset.forName(encoding)

    init {
        synchronized (EncodingCache) {
            EncodingCache.ENCODINGS.put(encoding, this)
        }
    }

    companion object {

        @JvmField val ASCII       = Encoding("US-ASCII"  , false, null)
        @JvmField val UTF8        = Encoding("UTF-8"     , false, ByteOrderMark.UTF8)
        @JvmField val UTF16       = Encoding("UTF-16"    , true , null)
        @JvmField val UTF16BE     = Encoding("UTF-16BE"  , false, ByteOrderMark.UTF16BE)
        @JvmField val UTF16LE     = Encoding("UTF-16LE"  , false, ByteOrderMark.UTF16LE)
        @JvmField val ISO88591    = Encoding("ISO-8859-1", false, null)
        @JvmField val IBM437      = Encoding("IBM437"    , false, null)

        @JvmStatic
        fun values() : List<Encoding> {
            synchronized (EncodingCache.ENCODINGS) {
                return EncodingCache.ENCODINGS.values.toList()
            }
        }

        /**
         * This helper function identifies the encoding value which corresponds to the supplied name. Be
         * aware that this enumeration only supports the <b>required</b> encodings.
         *
         * @param name   The name of the encoding which has to be identified. Case sensitivity doesn't matter here.
         *
         * @return   The encoding value if available.
         */
        @JvmStatic
        fun findByName(name : String) : Encoding? {
            synchronized (EncodingCache) {
                return EncodingCache.ENCODINGS.get(name)
            }
        }

        @JvmStatic
        fun getEncoding(encoding : Encoding?) = if(encoding == null) UTF8 else encoding;

    } /* ENDOBJECT */

    /**
     * Encodes the supplied text.
     *
     * @param text   The text that has to be encoded.
     *
     * @return   The data which has to be encoded.
     */
    fun encode(text : String) : ByteArray {
        var buffer = charset.encode(CharBuffer.wrap(text));
        var result = ByteArray(buffer.limit());
        buffer.get(result);
        return result;
    }

    /**
     * Decodes the supplied data using this encoding.
     *
     * @param data   The data providing the content.
     *
     * @return   The decoded String.
     */
    fun decode(data : ByteArray) = charset.decode(ByteBuffer.wrap(data)).toString()

    override fun toString() = encoding;

    override fun equals(other : Any?) = (other is Encoding) && (encoding == other.encoding)

    override fun hashCode() = encoding.hashCode()

} /* ENDCLASS */

private object EncodingCache {

    val ENCODINGS   = mutableMapOf<String, Encoding>()

} /* ENDOBJECT */

