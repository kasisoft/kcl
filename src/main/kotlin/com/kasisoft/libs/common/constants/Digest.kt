package com.kasisoft.libs.common.constants

import com.kasisoft.libs.common.annotation.*

import com.kasisoft.libs.common.pools.*

import com.kasisoft.libs.common.*

import javax.validation.constraints.*

import java.security.*

import java.util.function.Consumer
import java.util.function.Supplier
import java.util.*

/**
 * Collection of supported MessageDigest implementations.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Specification(value = "https://docs.oracle.com/javase/10/docs/api/java/security/MessageDigest.html", date = "04-JUN-2020")
class Digest(val algorithm : String) {

    private val bucket : Bucket<MessageDigest>

    init {

        try {
            MessageDigest.getInstance(algorithm);
        } catch (ex : NoSuchAlgorithmException) {
            throw KclException(ex, "Unknown digest algorithm '%s'!", algorithm);
        }

        bucket = Bucket<MessageDigest>( Supplier<MessageDigest> { createDigest(algorithm) }, Consumer<MessageDigest> { resetDigest(it) } )

        synchronized (DigestCache) {
            DigestCache.DIGESTS.put(algorithm, this)
        }

    }

    companion object {

        @JvmField val MD2       = Digest("MD2")
        @JvmField val MD5       = Digest("MD5")
        @JvmField val SHA1      = Digest("SHA-1")
        @JvmField val SHA256    = Digest("SHA-256")
        @JvmField val SHA384    = Digest("SHA-384")
        @JvmField val SHA512    = Digest("SHA-512")

        @JvmStatic
        fun values() : List<Digest> {
            synchronized (DigestCache) {
                return DigestCache.DIGESTS.values.toList()
            }
        }

        /**
         * This helper function identifies the MessageDigest algorithm value which corresponds to the supplied name. Be
         * aware that this enumeration only supports the <b>required</b> digests.
         *
         * @param name   The name of the digest which has to be identified. Case sensitivity doesn't matter here.
         *
         * @return   The digest value or empty if it cannot be identified.
         */
        @JvmStatic
        fun findByName(name : String) : Digest? = DigestCache.DIGESTS.get(name)

        fun createDigest(algorithm : String) : MessageDigest? {
            try {
                return MessageDigest.getInstance(algorithm);
            } catch (ex : NoSuchAlgorithmException) {
                // won't happen as this will be checked by the instantiation
                return null;
            }
        }

        fun resetDigest(digest : MessageDigest) : MessageDigest {
            digest.reset();
            return digest;
        }

    } /* ENDOBJECT */

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param data   The data used to be digested.
     *
     * @return   The hash value.
     */
    fun digestToString(vararg data : ByteArray) = digestToString(1, *data)

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param count   The number of times used to run the digestion.
     * @param data    The data used to be digested.
     *
     * @return   The hash value.
     */
    fun digestToString(count : Int, vararg data : ByteArray) : String {
        return Buckets.stringFBuilder().forInstance {
            var checksum = digest(count, *data)
            for (b in checksum) {
                it.appendF("%02x", b)
            }
            it.toString()
        }
    }

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param data   The data used to be digested.
     *
     * @return   The hash value.
     */
    fun digest(vararg data : ByteArray) = digest(1, *data)

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param count   The number of times used to run the digestion.
     * @param data    The data used to be digested.
     *
     * @return   The hash value.
     */
    fun digest(@Min(1) count : Int, vararg data : ByteArray) : ByteArray {
        val digest = bucket.allocate();
        for (i in 0..count - 1) {
            for (j in 0..data.size - 1) {
                digest.update(data[j]);
            }
        }
        val result = digest.digest();
        bucket.free(digest);
        return result;
    }

    override fun toString() = algorithm;

    override fun equals(other : Any?) = (other is Digest) && (algorithm == other.algorithm)

    override fun hashCode() = algorithm.hashCode()

} /* ENDCLASS */

private object DigestCache {

     val DIGESTS = mutableMapOf<String, Digest>()

} /* ENDOBJECT */
