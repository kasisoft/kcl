/**
 * Name........: Encoding
 * Description.: Collection of supported encodings.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.io.*;

/**
 * Collection of supported encodings.
 */
public enum Encoding {

  
  /**
   * @spec [06-Jan-2010:KASI]   http://java.sun.com/j2se/1.3/docs/api/java/lang/package-summary.html#charenc
   */
  
  ASCII       ( "US-ASCII"    , false ),
  Cp1252      ( "Cp1252"      , false ),
  UTF8        ( "UTF-8"       , false ),
  UTF16       ( "UTF-16"      , true  ),
  UTF16BE     ( "UTF-16BE"    , false ),
  UTF16LE     ( "UTF-16LE"    , false ),
  ISO88591    ( "ISO-8859-1"  , false );
  
  private String    encoding;
  private boolean   bom;
  
  Encoding( String key, boolean requiresbom ) {
    encoding  = key;
    bom       = requiresbom;
  }
  
  /**
   * Returns <code>true</code> if the BOM (byte order mark) is required. If it's not required the
   * BOM might still be there.
   * 
   * @return   <code>true</code> <=> The BOM is required.
   */
  public boolean isBOMRequired() {
    return bom;
  }
  
  /**
   * Returns the encoding as used within the JRE.
   * 
   * @return   The encoding as used within the JRE. Neither <code>null</code> nor empty.
   */
  public String getEncoding() {
    return encoding;
  }
  
  /**
   * Encodes the supplied text.
   * 
   * @param text   The text that has to be encoded. Not <code>null</code>.
   * 
   * @return   The data which has to be encoded. Not <code>null</code>.
   */
  public byte[] encode( @KNotNull(name="text") String text ) {
    try {
      return text.getBytes( encoding );
    } catch( UnsupportedEncodingException ex ) {
      // cannot happen as the declared encodings are required according to the specs 
      return null;
    }
  }

  /**
   * Decodes the supplied data using this encoding.
   * 
   * @param data   The data providing the content. Not <code>null</code>.
   * 
   * @return   The decoded String. Not <code>null</code>.
   */
  public String decode( @KNotNull(name="data") byte[] data ) {
    try {
      return new String( data, encoding );
    } catch( UnsupportedEncodingException ex ) {
      // cannot happen as the declared encodings are required according to the specs 
      return null;
    }
  }
  
} /* ENDENUM */
