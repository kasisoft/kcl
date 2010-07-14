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
   * @return   The reader if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public Reader openReader( @KFile(name="file") File file ) {
    try {
      return openReader( new FileInputStream( file ) );
    } catch( FileNotFoundException ex ) {
      throw new FailureException( FailureCode.IO, ex );
    }
  }

  /**
   * Opens a Reader for a specific InputStream using this encoding.
   * 
   * @param instream   The InputStream that has to be accessed using this encoding. Must be a valid file.
   *  
   * @return   The InputStream if it can be accessed. Not <code>null</code>.
   */
  public Reader openReader( @KNotNull(name="instream") InputStream instream ) {
    try {
      return new BufferedReader( new InputStreamReader( instream, encoding ) );
    } catch( UnsupportedEncodingException ex ) {
      // won't happen as we only support guarantueed encodings
      return null;
    }
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public Writer openWriter( @KFile(name="file") File file ) {
    try {
      return openWriter( new FileOutputStream( file ) );
    } catch( FileNotFoundException ex ) {
      throw new FailureException( FailureCode.IO, ex );
    }
  }

  /**
   * Opens a Writer for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   */
  public Writer openWriter( @KNotNull(name="outstream") OutputStream outstream ) {
    try {
      return new BufferedWriter( new OutputStreamWriter( outstream, encoding ) );
    } catch( UnsupportedEncodingException ex ) {
      // won't happen as we only support guarantueed encodings
      return null;
    }
  }

  /**
   * Opens a PrintStream for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The PrintStream if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public PrintStream openPrintStream( @KFile(name="file") File file ) {
    try {
      return openPrintStream( new FileOutputStream( file ) );
    } catch( FileNotFoundException        ex ) {
      throw new FailureException( FailureCode.IO, ex );
    }
  }

  /**
   * Opens a PrintStream for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The PrintStream if the OutputStream could be accessed. Not <code>null</code>.
   */
  public PrintStream openPrintStream( @KNotNull(name="outstream") OutputStream outstream ) {
    try {
      return new PrintStream( outstream, true, encoding );
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
  
  /**
   * Returns the default encoding.
   * 
   * @return   The default encoding. Not <code>null</code>.
   */
  public static final Encoding getDefault() {
    return UTF8;
  }
  
  /**
   * Opens a Reader for a specific file using this encoding.
   * 
   * @param file       The file that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding
   *                   {@link #UTF8} is used.
   *  
   * @return   The reader if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static final Reader openReader( 
    @KFile(name="file") File file, Encoding encoding 
  ) {
    if( encoding == null ) {
      return getDefault().openReader( file );
    } else {
      return encoding.openReader( file );
    }
  }

  /**
   * Opens a Reader for a specific InputStream using this encoding.
   * 
   * @param instream   The InputStream that has to be opened using this encoding. Not <code>null</code>.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding
   *                   {@link #UTF8} is used.
   *  
   * @return   The reader if the InputStream could be accessed. Not <code>null</code>.
   * 
   * @throws FailureException if accessing the InputStream failed for some reason.
   */
  public static final Reader openReader( 
    @KNotNull(name="instream") InputStream instream, Encoding encoding 
  ) {
    if( encoding == null ) {
      return getDefault().openReader( instream );
    } else {
      return encoding.openReader( instream );
    }
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param file       The file that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding
   *                   {@link #UTF8} is used.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static final Writer openWriter( @KNotNull(name="file") File file, Encoding encoding ) {
    if( encoding == null ) {
      return getDefault().openWriter( file );
    } else {
      return encoding.openWriter( file );
    }
  }

  /**
   * Opens a Writer for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Not <code>null</code>.
   * @param encoding    The encoding that has to be used. If <code>null</code> the default encoding
   *                    {@link #UTF8} is used.
   *  
   * @return   The writer if the OutputStream could be accessed. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static final Writer openWriter( 
    @KNotNull(name="outstream") OutputStream outstream, Encoding encoding 
  ) {
    if( encoding == null ) {
      return getDefault().openWriter( outstream );
    } else {
      return encoding.openWriter( outstream );
    }
  }

  /**
   * Opens a PrintStream for a specific file using this encoding.
   * 
   * @param file       The file that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding
   *                   {@link #UTF8} is used.
   *  
   * @return   The PrintStream if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static final PrintStream openPrintStream( 
    @KNotNull(name="file") File file, Encoding encoding 
  ) {
    if( encoding == null ) {
      return getDefault().openPrintStream( file );
    } else {
      return encoding.openPrintStream( file );
    }
  }

  /**
   * Opens a PrintStream for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be accessed using this encoding. Not <code>null</code>.
   * @param encoding    The encoding that has to be used. If <code>null</code> the default encoding
   *                    {@link #UTF8} is used.
   *  
   * @return   The PrintStream if the OutputStream could be accessed. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static final PrintStream openPrintStream( 
    @KNotNull(name="outstream") OutputStream outstream, Encoding encoding 
  ) {
    if( encoding == null ) {
      return getDefault().openPrintStream( outstream );
    } else {
      return encoding.openPrintStream( outstream );
    }
  }

  /**
   * This helper function identifies the encoding value which corresponds to the supplied name. Be
   * aware that this enumeration only supports the <b>required</b> encodings.
   * 
   * @param name   The name of the encoding which has to be identified. Case sensitivity doesn't
   *               matter here.
   *               Neither <code>null</code> nor empty.
   *               
   * @return   The encoding value or <code>null</code> if it cannot be identified.
   */
  public static final Encoding valueByName( @KNotEmpty(name="name") String name ) {
    for( Encoding encoding : Encoding.values() ) {
      if( encoding.encoding.equalsIgnoreCase( name ) ) {
        return encoding;
      }
    }
    return null;
  }

} /* ENDENUM */
