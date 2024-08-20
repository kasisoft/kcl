package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.text.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.security.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class Encryptor {

    private String          cipher;
    private String          algorithm;
    private String          secret;
    private Random          random;
    private SecretKey       key;
    private IvParameterSpec ivParameter;

    public Encryptor(@NotNull String cipher, @NotNull String algorithm) {
        this(cipher, algorithm, null, null);
    }

    public Encryptor(@NotNull String cipher, @NotNull String algorithm, String secret) {
        this(cipher, algorithm, secret, null);
    }

    public Encryptor(@NotNull String cipher, @NotNull String algorithm, String secret, byte[] salt) {
        verify(cipher, algorithm);
        this.random      = new Random();
        this.cipher      = cipher;
        this.algorithm   = algorithm;
        this.secret      = setupSecret(secret);
        this.key         = new SecretKeySpec(Base64.getDecoder().decode(this.secret), algorithm);
        this.ivParameter = new IvParameterSpec(setupSalt(salt));
    }

    public String getCipher() {
        return cipher;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getSecret() {
        return secret;
    }

    @NotNull
    public Optional<@NotNull byte[]> encrypt(byte[] data) {
        var result = Optional.<byte[]> empty();
        if (data != null) {
            try {
                var cipher = cipher();
                cipher.init(Cipher.ENCRYPT_MODE, key, ivParameter);
                result = Optional.ofNullable(cipher.doFinal(data));
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        }
        return result;
    }

    @NotNull
    public Optional<@NotNull String> encrypt(String data) {
        var result = Optional.<String> empty();
        if (data != null) {
            result = encrypt(Encoding.UTF8.encode(data)).map(Base64.getEncoder()::encodeToString);
        }
        return result;
    }

    @NotNull
    public Optional<byte[]> decrypt(byte[] data) {
        var result = Optional.<byte[]> empty();
        if (data != null) {
            try {
                var cipher = cipher();
                cipher.init(Cipher.DECRYPT_MODE, key, ivParameter);
                result = Optional.ofNullable(cipher.doFinal(data));
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        }
        return result;
    }

    @NotNull
    public Optional<String> decrypt(String data) {
        var result = Optional.<String> empty();
        if (data != null) {
            result = decrypt(Base64.getDecoder().decode(data)).map(Encoding.UTF8::decode);
        }
        return result;
    }

    private byte[] setupSalt(byte[] salt) {
        byte[] result = salt;
        if (result == null) {
            result = new byte[8];
            random.nextBytes(result);
        }
        return result;
    }

    @NotNull
    private String setupSecret(String secret) {
        String result = StringFunctions.cleanup(secret);
        if (result == null) {
            byte[] encoded = keyGenerator().generateKey().getEncoded();
            result = Base64.getEncoder().encodeToString(encoded);
        }
        return result;
    }

    @NotNull
    private KeyGenerator keyGenerator() {
        try {
            return KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            // won't happen as it had been tested before (see #verify)
            return null;
        }
    }

    @NotNull
    private Cipher cipher() {
        try {
            return Cipher.getInstance(cipher);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            // won't happen as it had been tested before (see #verify)
            return null;
        }
    }

    private void verify(@NotNull String cipher, @NotNull String algorithm) {
        try {
            Cipher.getInstance(cipher);
            KeyGenerator.getInstance(algorithm);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

} /* ENDCLASS */
