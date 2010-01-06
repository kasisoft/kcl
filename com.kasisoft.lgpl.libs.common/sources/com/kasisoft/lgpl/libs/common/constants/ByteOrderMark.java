/**
 * Name........: ByteOrderMark
 * Description.: Constants representing the different byte order marks.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * Constants the different byte order marks.
 */
public enum ByteOrderMark {

  UTF8    ( new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf } ) ,
  UTF16BE ( new byte[] { (byte) 0xfe, (byte) 0xff } ) ,
  UTF16LE ( new byte[] { (byte) 0xff, (byte) 0xfe } ) ;
  
  private byte[]   bomsequence;
  
  ByteOrderMark( byte[] sequence ) {
    bomsequence = sequence;
  }
  
  /**
   * Returns the byte order mark allowing to identify the character encoding.
   * 
   * @return   The byte order mark allowing to identify the character encoding. Not <code>null</code>.
   */
  public byte[] getBOM() {
    return bomsequence;
  }

  /**
   * Returns <code>true</code> if the supplied data starts with this BOM.
   * 
   * @param data   The data to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied data starts with this BOM.
   */
  public boolean startsWith( @KNotNull(name="data") byte[] data ) {
    return startsWith( data, 0 );
  }
  
  /**
   * Returns <code>true</code> if the supplied data starts with this BOM.
   * 
   * @param data     The data to be tested. Not <code>null</code>.
   * @param offset   An offset where the start has to begin. Must be a positive number.
   * 
   * @return   <code>true</code> <=> The supplied data starts with this BOM.
   */
  public boolean startsWith( 
    @KNotNull(name="data")   byte[]   data, 
    @KIPositive(zero=true)   int      offset 
  ) {
    for( int i = 0; (i < bomsequence.length) && (offset < data.length); i++, offset++ ) {
      if( data[ offset ] != bomsequence[i] ) {
        return false;
      }
    }
    return offset < data.length;
  }
  
  /**
   * Returns the ByteOrderMark located at the beginning of the supplied data.
   * 
   * @param data   The data to be tested. Not <code>null</code>.
   * 
   * @return   The ByteOrderMark if it could be identified. Maybe <code>null</code>.
   */
  public static final ByteOrderMark identify( @KNotNull(name="data") byte[] data ) {
    return identify( data, 0 );
  }
  
  /**
   * Returns the ByteOrderMark located at a specific location of the supplied data.
   * 
   * @param data     The data to be tested. Not <code>null</code>.
   * @param offset   The location where to start the test. Must be positive.
   * 
   * @return   The ByteOrderMark if it could be identified. Maybe <code>null</code>.
   */
  public static final ByteOrderMark identify( 
    @KNotNull(name="data")   byte[]   data, 
    @KIPositive(zero=true)   int      offset 
  ) {
    while( offset < data.length ) {
      if( data[ offset ] == UTF8.bomsequence[0] ) {
        if( UTF8.startsWith( data, offset ) ) {
          return UTF8;
        }
      } else if( data[ offset ] == UTF16BE.bomsequence[0] ) {
        if( UTF16BE.startsWith( data, offset ) ) {
          return UTF16BE;
        }
      } else if( data[ offset ] == UTF16LE.bomsequence[0] ) {
        if( UTF16LE.startsWith( data, offset ) ) {
          return UTF16LE;
        }
      }
      offset++;
    }
    return null;
  }
  
} /* ENDENUM */
