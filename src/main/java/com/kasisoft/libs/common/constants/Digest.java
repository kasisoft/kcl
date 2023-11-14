package com.kasisoft.libs.common.constants;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.annotation.*;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.*;

import java.security.*;

/**
 * Collection of supported MessageDigest implementations.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specification(value = "https://docs.oracle.com/javase/10/docs/api/java/security/MessageDigest.html", date = "04-JUN-2020")
public final class Digest {

    public static final Digest               MD2;

    public static final Digest               MD5;

    public static final Digest               SHA1;

    public static final Digest               SHA256;

    public static final Digest               SHA384;

    public static final Digest               SHA512;

    private static final Map<String, Digest> DIGESTS;

    static {
        DIGESTS = new HashMap<>();
        MD2     = new Digest("MD2");
        MD5     = new Digest("MD5");
        SHA1    = new Digest("SHA-1");
        SHA256  = new Digest("SHA-256");
        SHA384  = new Digest("SHA-384");
        SHA512  = new Digest("SHA-512");
    }

    @NotBlank
    private String                algorithm;

    private Bucket<MessageDigest> bucket;

    /**
     * Initializes this digest for the supplied algorithm.
     *
     * @param algorithm
     *            The name of the hash algorithm.
     * @throws KclException
     *             The supplied alorithm isn't known.
     */
    public Digest(@NotBlank String algorithm) {
        try {
            MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new KclException(ex, error_unknown_digest_algorithm, algorithm);
        }
        this.algorithm = algorithm;
        bucket         = new Bucket<>(() -> createDigest(algorithm), Digest::resetDigest);
        synchronized (DIGESTS) {
            DIGESTS.put(algorithm, this);
        }
    }

    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Processes the supplied data blocks in order to calculate a hash.
     *
     * @param data
     *            The data used to be digested.
     * @return The hash value.
     */
    public @NotNull String digestToString(@NotNull byte[] ... data) {
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
    public @NotNull String digestToString(int count, @NotNull byte[] ... data) {
        return Buckets.bucketStringBuilder().forInstance($ -> {
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
    public @NotNull byte[] digest(@NotNull byte[] ... data) {
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
    public @NotNull byte[] digest(@Min(1) int count, @NotNull byte[] ... data) {
        var digest = bucket.allocate();
        for (var i = 0; i < count; i++) {
            for (var j = 0; j < data.length; j++) {
                digest.update(data[j]);
            }
        }
        var result = digest.digest();
        bucket.free(digest);
        return result;
    }

    @Override
    public String toString() {
        return algorithm;
    }

    public static @NotNull Digest[] values() {
        synchronized (DIGESTS) {
            return DIGESTS.values().toArray(new Digest[DIGESTS.size()]);
        }
    }

    /**
     * This helper function identifies the MessageDigest algorithm value which corresponds to the
     * supplied name. Be aware that this enumeration only supports the <b>required</b> digests.
     *
     * @param name
     *            The name of the digest which has to be identified. Case sensitivity doesn't matter
     *            here.
     * @return The digest value or empty if it cannot be identified.
     */
    public static Optional<Digest> findByName(String name) {
        for (var digest : Digest.values()) {
            if (digest.algorithm.equalsIgnoreCase(name)) {
                return Optional.of(digest);
            }
        }
        return Optional.empty();
    }

    private static @NotNull MessageDigest createDigest(@NotNull String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            // won't happen as this will be checked by the instantiation
            return null;
        }
    }

    private static @NotNull MessageDigest resetDigest(@NotNull MessageDigest digest) {
        digest.reset();
        return digest;
    }

} /* ENDCLASS */
