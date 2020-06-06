package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.text.StringFunctions;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.security.NoSuchAlgorithmException;

import java.util.function.Consumer;

import java.util.Base64;
import java.util.Optional;
import java.util.Random;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Encryptor {
  
  @Getter
  String                cipher;
  
  @Getter
  String                algorithm;
  
  @Getter
  String                secret;
  
  Random                random;
  SecretKey             key;
  IvParameterSpec       ivParameter;
  Consumer<Exception>   errorHandler;

  public Encryptor(@NotNull String cipher, @NotNull String algorithm) {
    this(cipher, algorithm, null, null);
  }

  public Encryptor(@NotNull String cipher, @NotNull String algorithm, @Null String secret) {
    this(cipher, algorithm, secret, null);
  }
  
  public Encryptor(@NotNull String cipher, @NotNull String algorithm, @Null String secret, @Null byte[] salt) {
    verify(cipher, algorithm);
    this.random       = new Random();
    this.cipher       = cipher;
    this.algorithm    = algorithm;
    this.secret       = setupSecret(secret);
    this.key          = new SecretKeySpec(Base64.getDecoder().decode(this.secret), algorithm);
    this.ivParameter  = new IvParameterSpec(setupSalt(salt));
    this.errorHandler = $ -> {};
  }
  
  public @NotNull Encryptor withErrorHandler(@Null Consumer<Exception> errorHandler) {
    this.errorHandler = errorHandler != null ? errorHandler : $ -> {};
    return this;
  }
  
  public @NotNull Optional<byte[]> encrypt(@Null byte[] data) {
    var result = Optional.<byte[]>empty();
    if (data != null) {
      try {
        var cipher = cipher();
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameter);
        result = Optional.ofNullable(cipher.doFinal(data));
      } catch (Exception ex) {
        errorHandler.accept(ex);
      }
    }
    return result;
  }

  public @NotNull Optional<String> encrypt(@Null String data) {
    var result = Optional.<String>empty();
    if (data != null) {
      result = encrypt(Encoding.UTF8.encode(data)).map(Base64.getEncoder()::encodeToString);
    }
    return result;
  }

  public @NotNull Optional<byte[]> decrypt(@Null byte[] data) {
    var result = Optional.<byte[]>empty();
    if (data != null) {
      try {
        var cipher = cipher();
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameter);
        result = Optional.ofNullable(cipher.doFinal(data));
      } catch (Exception ex) {
        errorHandler.accept(ex);
      }
    }
    return result;
  }

  public @NotNull Optional<String> decrypt(@Null String data) {
    var result = Optional.<String>empty();
    if (data != null) {
      result = decrypt(Base64.getDecoder().decode(data)).map(Encoding.UTF8::decode);
    }
    return result;
  }

  private byte[] setupSalt(@Null byte[] salt) {
    byte[] result = salt;
    if (result == null) {
      result = new byte[8];
      random.nextBytes(result);
    }
    return result;
  }
  
  private @NotNull String setupSecret(@Null String secret) {
    String result = StringFunctions.cleanup(secret);
    if (result == null) {
      byte[] encoded = keyGenerator().generateKey().getEncoded();
      result         = Base64.getEncoder().encodeToString(encoded);
    }
    return result;
  }
  
  private KeyGenerator keyGenerator() {
    try {
      return KeyGenerator.getInstance(algorithm);
    } catch (NoSuchAlgorithmException ex) {
      // won't happen as it had been tested before
      return null;
    }
  }
  
  private Cipher cipher() {
    try {
      return Cipher.getInstance(cipher);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
      // won't happen as it had been tested before
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
