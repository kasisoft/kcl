/**
 * Name........: Encoding
 * Description.: Collection of supported encodings.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.io.*;

/**
 * Collection of supported encodings.
 */
public enum Encoding {

  
  /**
   * @spec [06-Jan-2010:KASI]   http://java.sun.com/j2se/1.3/docs/api/java/lang/package-summary.html#charenc
   */
  
  ASCII       ( "US-ASCII"    , false , null                  ),
  Cp1252      ( "Cp1252"      , false , null                  ),
  UTF8        ( "UTF-8"       , false , ByteOrderMark.UTF8    ),
  UTF16       ( "UTF-16"      , true  , null                  ),
  UTF16BE     ( "UTF-16BE"    , false , ByteOrderMark.UTF16BE ),
  UTF16LE     ( "UTF-16LE"    , false , ByteOrderMark.UTF16LE ),
  ISO88591    ( "ISO-8859-1"  , false , null                  );
  
  private String          encoding;
  private boolean         bom;
  private ByteOrderMark   byteordermark;
  
  Encoding( String key, boolean requiresbom, ByteOrderMark mark ) {
    encoding      = key;
    bom           = requiresbom;
    byteordermark = mark;
  }

  /**
   * Opens a Reader for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The reader if the file could be opened.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public Reader open( @KFile(name="file") File file ) {
    try {
      InputStream instream  = new FileInputStream( file );
      return new InputStreamReader( instream, encoding );
    } catch( FileNotFoundException        ex ) {
      throw new FailureException( FailureCode.IO, ex );
    } catch( UnsupportedEncodingException ex ) {
      // won't happen as we only support guarantueed encodings
      return null;
    }
  }
  
  /**
   * Returns the Byte Order Mark associated with this character set encoding. The result maybe
   * <code>null</code> (f.e. byte character sets).
   * 
   * @return   The Byte Order Mark associated with this character set encoding. Maybe <code>null</code>.
   */
  public ByteOrderMark getByteOrderMark() {
    return byteordermark;
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
