package com.kasisoft.libs.common.constants;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.annotation.*;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.security.*;

/**
 * Collection of supported MessageDigest implementations.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Specification(value = "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/security/MessageDigest.html", date = "14-NOV-2023")
public enum Digest {

    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private String                  algorithm;
    private Bucket<MessageDigest>   bucket;

    /**
     * Initializes this digest for the supplied algorithm.
     *
     * @param algorithm
     *            The name of the hash algorithm.
     * @throws KclException
     *             The supplied alorithm isn't known.
     */
    Digest(@NotBlank String algorithm) {
        this.algorithm = algorithm;
        // make sure to verify the existence of the algorithm
         getInstance();
    }

    @NotBlank
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String toString() {
        return algorithm;
    }

    private synchronized Bucket<MessageDigest> bucket() {
        if (bucket == null) {
            bucket = new Bucket<MessageDigest>(this::getInstance, MessageDigest::reset);
        }
        return bucket;
    }

    @NotNull
    private MessageDigest getInstance() {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            // shouldn't happen but better be safe
            throw new KclException(ex, error_unknown_digest_algorithm.formatted(algorithm));
        }
    }

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param data
     *            The data used to be digested.
     * @return The hash value.
     */
    @NotNull
    public String digestToString(@NotNull byte[] ... data) {
        return digestToString(1, data);
    }

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param count
     *            The number of times used to run the digestion.
     * @param data
     *            The data used to be digested.
     * @return The hash value.
     */
    @NotNull
    public String digestToString(int count, @NotNull byte[] ... data) {
        return Buckets.stringBuilder().forInstance($ -> {
            var checksum = digest(count, data);
            for (var b : checksum) {
                var asbyte = Integer.toString((b & 0xff), 16);
                if (asbyte.length() == 1) {
                    $.append('0');
                }
                $.append(asbyte);
            }
            return $.toString();
        });
    }

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param data
     *            The data used to be digested.
     * @return The hash value.
     */
    @NotNull
    public byte[] digest(@NotNull byte[] ... data) {
        return digest(1, data);
    }

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param count
     *            The number of times used to run the digestion.
     * @param data
     *            The data used to be digested.
     * @return The hash value.
     */
    @NotNull
    public byte[] digest(@Min(1) int count, @NotNull byte[] ... data) {
        return bucket().forInstance($digest -> {
            for (var i = 0; i < count; i++) {
                for (var j = 0; j < data.length; j++) {
                    $digest.update(data[j]);
                }
            }
            return $digest.digest();
        });
    }

} /* ENDCLASS */
