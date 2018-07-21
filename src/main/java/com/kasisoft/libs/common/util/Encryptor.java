package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.text.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.security.*;

import java.util.function.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

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
  
  SecretKey             key;
  IvParameterSpec       ivParameter;
  Consumer<Exception>   errorHandler;

  public Encryptor( @NonNull String cipher, @NonNull String algorithm ) {
    this( cipher, algorithm, null, null );
  }

  public Encryptor( @NonNull String cipher, @NonNull String algorithm, String secret ) {
    this( cipher, algorithm, secret, null );
  }
  
  public Encryptor( @NonNull String cipher, @NonNull String algorithm, String secret, byte[] salt ) {
    verify( cipher, algorithm );
    this.cipher       = cipher;
    this.algorithm    = algorithm;
    this.secret       = setupSecret ( secret );
    this.key          = new SecretKeySpec( Base64.getDecoder().decode( this.secret ), algorithm );
    this.ivParameter  = new IvParameterSpec( setupSalt( salt ) );
    this.errorHandler = $ -> {};
  }
  
  public Encryptor withErrorHandler( Consumer<Exception> errorHandler ) {
    this.errorHandler = errorHandler != null ? errorHandler : $ -> {};
    return this;
  }
  
  public Optional<byte[]> encrypt( byte[] data ) {
    Optional<byte[]> result = Optional.empty();
    if( data != null ) {
      try {
        Cipher cipher = cipher();
        cipher.init( Cipher.ENCRYPT_MODE, key, ivParameter );
        result = Optional.ofNullable( cipher.doFinal( data ) );
      } catch( Exception ex ) {
        errorHandler.accept( ex );
      }
    }
    return result;
  }

  public Optional<String> encrypt( String data ) {
    Optional<String> result = Optional.empty();
    if( data != null ) {
      result = encrypt( Encoding.UTF8.encode( data ) )
        .map( Base64.getEncoder()::encodeToString );
    }
    return result;
  }

  public Optional<byte[]> decrypt( byte[] data ) {
    Optional<byte[]> result = Optional.empty();
    if( data != null ) {
      try {
        Cipher cipher = cipher();
        cipher.init( Cipher.DECRYPT_MODE, key, ivParameter );
        result = Optional.ofNullable( cipher.doFinal( data ) );
      } catch( Exception ex ) {
        errorHandler.accept( ex );
      }
    }
    return result;
  }

  public Optional<String> decrypt( String data ) {
    Optional<String> result = Optional.empty();
    if( data != null ) {
      result = decrypt( Base64.getDecoder().decode( data ) )
        .map( Encoding.UTF8::decode );
    }
    return result;
  }

  private byte[] setupSalt( byte[] salt ) {
    byte[] result = salt;
    if( result == null ) {
      result = new byte[8];
      for( int i = 0; i < result.length; i++ ) {
        result[i] = (byte) (Math.random() * 256);
      }
    }
    return result;
  }
  
  private String setupSecret( String secret ) {
    String result = StringFunctions.cleanup( secret );
    if( result == null ) {
      byte[] encoded = keyGenerator().generateKey().getEncoded();
      result         = Base64.getEncoder().encodeToString( encoded );
    }
    return result;
  }
  
  private KeyGenerator keyGenerator() {
    try {
      return KeyGenerator.getInstance( algorithm );
    } catch( NoSuchAlgorithmException ex ) {
      // won't happen as it had been tested before
      return null;
    }
  }
  
  private Cipher cipher() {
    try {
      return Cipher.getInstance( cipher );
    } catch( NoSuchAlgorithmException | NoSuchPaddingException ex ) {
      // won't happen as it had been tested before
      return null;
    }
  }
  
  private void verify( @NonNull String cipher, @NonNull String algorithm ) {
    try {
      Cipher.getInstance( cipher );
      KeyGenerator.getInstance( algorithm );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }
  
} /* ENDCLASS */
