package com.kasisoft.libs.common.old.constants;

import lombok.experimental.*;

import lombok.*;

import com.kasisoft.libs.common.old.annotation.*;
import com.kasisoft.libs.common.old.base.*;
import com.kasisoft.libs.common.old.util.*;

import java.security.*;

import java.util.*;

/**
 * Collection of supported MessageDigest implementations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specification(value = "http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest", date = "10-Jun-2016")
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = "algorithm")
public final class Digest {
  
  public static final Digest   MD2;
  public static final Digest   MD5;
  public static final Digest   SHA1;
  public static final Digest   SHA256;
  public static final Digest   SHA384;
  public static final Digest   SHA512;
  
  private static final Map<String, Digest>   DIGESTS;
  
  static {
    DIGESTS   = new HashMap<>();
    MD2       = new Digest( "MD2"     );
    MD5       = new Digest( "MD5"     );
    SHA1      = new Digest( "SHA-1"   );
    SHA256    = new Digest( "SHA-256" );
    SHA384    = new Digest( "SHA-384" );
    SHA512    = new Digest( "SHA-512" );
  }
  
  /** Neither <code>null</code> nor empty. */
  @Getter String          algorithm;
  
  Bucket<MessageDigest>   bucket;

  
  /**
   * Initializes this digest for the supplied algorithm.
   *  
   * @param algorithm   The name of the hash algorithm. Neither <code>null</code> nor empty.
   * 
   * @throws KclException   The supplied alorithm isn't known.
   */
  public Digest( @NonNull String algorithm ) {
    try {
      MessageDigest.getInstance( algorithm );
    } catch( NoSuchAlgorithmException ex ) {
      throw KclException.wrap( ex );
    }
    this.algorithm  = algorithm;
    bucket          = new Bucket<>( new DigestFactory( algorithm ) );
    DIGESTS.put( algorithm, this );
  }
  
  /**
   * Processes the supplied data blocks in order to calculate a hash.
   * 
   * @param data   The data used to be digested. Not <code>null</code>.
   * 
   * @return   The hash value. Neither <code>null</code> nor empty.
   */
  public String digestToString( @NonNull byte[] ... data ) {
    return digestToString( 1, data );
  }

  /**
   * Processes the supplied data blocks in order to calculate a hash.
   *
   * @param count   The number of times used to run the digestion.
   * @param data    The data used to be digested. Not <code>null</code>.
   * 
   * @return   The hash value. Neither <code>null</code> nor empty.
   */
  public String digestToString( int count, @NonNull byte[] ... data ) {
    byte[]        checksum = digest( count, data );
    StringBuilder builder  = new StringBuilder();
    for( byte b : checksum ) {
      String asbyte = Integer.toString( ( b & 0xff ), 16 );
      if( asbyte.length() == 1 ) {
        builder.append( '0' );
      }
      builder.append( asbyte );
    }
    return builder.toString();
  }

  /**
   * Processes the supplied data blocks in order to calculate a hash.
   * 
   * @param data   The data used to be digested. Not <code>null</code>.
   * 
   * @return   The hash value. Neither <code>null</code> nor empty.
   */
  public byte[] digest( @NonNull byte[] ... data ) {
    return digest( 1, data );
  }

  /**
   * Processes the supplied data blocks in order to calculate a hash.
   *
   * @param count   The number of times used to run the digestion.
   * @param data    The data used to be digested. Not <code>null</code>.
   * 
   * @return   The hash value. Neither <code>null</code> nor empty.
   */
  public byte[] digest( int count, @NonNull byte[] ... data ) {
    MessageDigest digest = null;
    try {
      digest = bucket.allocate();
      for( int i = 0; i < count; i++ ) {
        for( int j = 0; j < data.length; j++ ) {
          digest.update( data[j] );
        }
      }
      return digest.digest();
    } finally {
      if( digest != null ) {
        bucket.free( digest );
      }
    }
  }

  public static Digest[] values() {
    return DIGESTS.values().toArray( new Digest[ DIGESTS.size() ] );
  }
  
  /**
   * This helper function identifies the MessageDigest algorithm value which corresponds to the supplied name. Be
   * aware that this enumeration only supports the <b>required</b> digests.
   * 
   * @param name   The name of the digest which has to be identified. Case sensitivity doesn't matter here.
   *               Neither <code>null</code> nor empty.
   *               
   * @return   The digest value or <code>null</code> if it cannot be identified.
   */
  public static Digest valueByName( @NonNull String name ) {
    for( val digest : Digest.values() ) {
      if( digest.algorithm.equalsIgnoreCase( name ) ) {
        return digest;
      }
    }
    return null;
  }

  @AllArgsConstructor
  private static class DigestFactory implements BucketFactory<MessageDigest> {
    
    String   algorithm;

    @Override
    public MessageDigest create() {
      try {
        return MessageDigest.getInstance( algorithm );
      } catch( NoSuchAlgorithmException ex ) {
        // won't happen as this will be checked by the instantiation
        return null;
      }
    }

    @Override
    public MessageDigest reset( MessageDigest object ) {
      object.reset();
      return object;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
